package genius;

import genius.model.*;
import genius.service.*;

import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final DataService dataService = new DataService();
    private static final LyricEditor lyricEditor = new LyricEditor(dataService);

    public static void main(String[] args) {
        initializeSampleAdmin();

        while (true) {
            System.out.println("\n--- سیستم مدیریت موزیک ---");
            System.out.println("1. ثبت نام");
            System.out.println("2. ورود");
            System.out.println("3. خروج");
            System.out.print("انتخاب کنید: ");

            int choice = readInt();
            switch (choice) {
                case 1 -> register();
                case 2 -> login();
                case 3 -> System.exit(0);
                default -> System.out.println("ورودی نامعتبر!");
            }
        }
    }

    private static void initializeSampleAdmin() {
        User admin = new Admin("admin", "0000");
        dataService.registerUser(admin);
    }


    private static void register() {
        System.out.print("نام کاربری: ");
        String username = scanner.nextLine();

        System.out.print("رمز عبور: ");
        String password = scanner.nextLine();

        System.out.print("نقش (USER/ARTIST): ");
        String role = scanner.nextLine().toUpperCase();

        if (!role.equals("USER") && !role.equals("ARTIST")) {
            System.out.println(" فقط USER یا ARTIST مجاز هستند.");
            return;
        }


        User newUser = switch (role) {
            case "ARTIST" -> new Artist(username, password);
            case "ADMIN" -> new Admin(username, password);
            default -> new User(username, password, "USER");
        };

        if (dataService.registerUser(newUser)) {
            System.out.println("ثبت‌نام موفق بود");
        } else {
            System.out.println("نام کاربری تکراری است");
        }
    }

    private static void login() {
        System.out.print("نام کاربری : ");
        String username = scanner.nextLine();

        System.out.print("رمز عبور : ");
        String password = scanner.nextLine();

        User user = dataService.authenticate(username, password);
        if (user == null) {
            System.out.println("ورود ناموفق");
            return;
        }

        System.out.println("\nخوش آمدید " + user.getUsername() + " (" + user.getRole() + ")");
        userMenu(user);
    }

    private static void userMenu(User user) {
        while (true) {
            System.out.println("\n--- منوی اصلی ---");
            System.out.println("1. ایجاد آهنگ جدید");
            System.out.println("2. ویرایش متن آهنگ");
            System.out.println("3. مشاهده درخواست‌های ویرایش");
            System.out.println("4. لیست آهنگ‌ها");
            System.out.println("0. خروج");
            System.out.print("انتخاب کنید: ");

            int choice = readInt();
            switch (choice) {
                case 1 -> createSong(user);
                case 2 -> editSongLyrics(user);
                case 3 -> showEditRequests(user);
                case 4 -> listAllSongs(user);
                case 0 -> { return; }
                default -> System.out.println("Eror");
            }
        }
    }

    private static void createSong(User user) {
        Artist artist;
        if (user instanceof Artist) {
            artist = (Artist) user;
        } else if (user instanceof Admin) {
            List<Artist> artists = dataService.getAllArtists();
            if (artists.isEmpty()) {
                System.out.println("هیچ هنرمندی ثبت نشده");
                return;
            }

            System.out.println("انتخاب هنرمند :");
            for (int i = 0; i < artists.size(); i++) {
                System.out.println((i + 1) + ". " + artists.get(i).getUsername());
            }

            int choice = readInt();
            if (choice < 1 || choice > artists.size()) {
                System.out.println("Eror");
                return;
            }

            artist = artists.get(choice - 1);
        } else {
            System.out.println("دسترسی ایجاد آهنگ ندارید");
            return;
        }

        System.out.print("عنوان آهنگ : ");
        String title = scanner.nextLine();

        System.out.print("متن آهنگ : ");
        String lyrics = scanner.nextLine();

        Song song = new Song(title, artist, lyrics);
        dataService.addSong(song);
        System.out.println("آهنگ ثبت شد");
    }

    private static void editSongLyrics(User user) {
        List<Song> songs = switch (user.getRole()) {
            case "ADMIN", "USER" -> dataService.getAllSongs();
            case "ARTIST" -> ((Artist) user).getSongs();
            default -> Collections.emptyList();
        };

        if (songs.isEmpty()) {
            System.out.println("آهنگی برای نمایش وجود ندارد");
            return;
        }

        for (int i = 0; i < songs.size(); i++) {
            System.out.printf("%d. %s - %s%n", (i + 1), songs.get(i).getTitle(), songs.get(i).getArtist().getUsername());
        }

        System.out.print("شماره آهنگ : ");
        int songIndex = readInt();
        if (songIndex < 1 || songIndex > songs.size()) {
            System.out.println("انتخاب نامعتبر");
            return;
        }

        Song selectedSong = songs.get(songIndex - 1);
        System.out.println("متن فعلی:\n" + selectedSong.getLyrics());

        System.out.print("\nمتن جدید: ");
        String newLyrics = scanner.nextLine();

        if (newLyrics.isBlank()) return;

        if ("USER".equals(user.getRole())) {
            lyricEditor.submitEdit(selectedSong, user, newLyrics);
            System.out.println("درخواست ویرایش ثبت شد");
        } else {
            selectedSong.updateLyrics(newLyrics);
            System.out.println("متن به‌روزرسانی شد");
        }
    }

    private static void showEditRequests(User user) {
        if (!user.getRole().equals("ADMIN") && !(user instanceof Artist)) {
            System.out.println("شما مجاز به دیدن درخواست‌ها نیستید.");
            return;
        }

        List<LyricEditRequest> requests = dataService.getPendingRequests(user);
        if (requests.isEmpty()) {
            System.out.println("درخواستی برای بررسی وجود ندارد.");
            return;
        }

        for (int i = 0; i < requests.size(); i++) {
            LyricEditRequest req = requests.get(i);
            System.out.printf("%d. آهنگ: %s | ویرایشگر: %s%n", i + 1, req.getSong().getTitle(), req.getEditor().getUsername());
            System.out.println("متن پیشنهادی:\n" + req.getProposedLyrics());
            System.out.println("----------------------------");
        }

        System.out.print("شماره درخواست برای تایید (0 برای بازگشت): ");
        int choice = readInt();

        if (choice > 0 && choice <= requests.size()) {
            boolean success = lyricEditor.approveEdit(requests.get(choice - 1), user);
            System.out.println(success ? "درخواست تایید شد!" : "خطا در تایید.");
        }
    }

    private static void listAllSongs(User user) {
        List<Song> songs = switch (user.getRole()) {
            case "ARTIST" -> ((Artist) user).getSongs();
            default -> dataService.getAllSongs();
        };

        if (songs.isEmpty()) {
            System.out.println("آهنگی ثبت نشده");
            return;
        }

        System.out.println("----- لیست آهنگ‌ها -----");
        for (Song song : songs) {
            System.out.printf("عنوان: %s | هنرمند: %s%nمتن:%n%s%n--------------------%n",
                    song.getTitle(), song.getArtist().getUsername(), song.getLyrics());
        }
    }

    private static int readInt() {
        while (!scanner.hasNextInt()) {
            scanner.nextLine();
            System.out.println("لطفاً یک عدد وارد کنید");
        }
        return scanner.nextInt();
    }

}
