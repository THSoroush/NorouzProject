package genius.service;

import genius.model.*;

import java.util.*;

public class DataService {
    private final Map<String, User> users = new HashMap<>();
    private final List<Song> songs = new ArrayList<>();
    private final List<LyricEditRequest> editRequests = new ArrayList<>();

    public boolean registerUser(User user) {
        if (users.containsKey(user.getUsername())) return false;


        User newUser = switch (user.getRole()) {
            case "ARTIST" -> (user instanceof Artist ? user : new Artist(user.getUsername(), "temp"));
            case "ADMIN" -> (user instanceof Admin ? user : new Admin(user.getUsername(), "temp"));
            default -> user;
        };

        users.put(user.getUsername(), newUser);
        return true;
    }

    public User authenticate(String username, String password) {
        User user = users.get(username);
        return (user != null && user.checkPassword(password)) ? user : null;
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public void addEditRequest(LyricEditRequest request) {
        editRequests.add(request);
    }

    public List<LyricEditRequest> getPendingRequests(User approver) {
        List<LyricEditRequest> result = new ArrayList<>();
        for (LyricEditRequest req : editRequests) {
            if (!req.isApproved() &&
                    (approver instanceof Admin ||
                            (approver instanceof Artist artist && artist.ownsSong(req.getSong())))) {
                result.add(req);
            }
        }
        return result;
    }

    public List<Song> getAllSongs() {
        return new ArrayList<>(songs);
    }

    public List<Artist> getAllArtists() {
        List<Artist> artists = new ArrayList<>();
        for (User user : users.values()) {
            if (user instanceof Artist artist) {
                artists.add(artist);
            }
        }
        return artists;
    }
}
