package genius.model;

public class Song {
    private String title;
    private String lyrics;
    private Artist artist;

    public Song(String title, Artist artist, String lyrics) {
        this.title = title;
        this.artist = artist;
        this.lyrics = lyrics;
        artist.addSong(this);
    }

    public void updateLyrics(String newLyrics) {
        this.lyrics = newLyrics;
    }

    public String getTitle() {
        return title;
    }

    public String getLyrics() {
        return lyrics;
    }

    public Artist getArtist() {
        return artist;
    }
}
