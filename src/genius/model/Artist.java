package genius.model;

import java.util.ArrayList;
import java.util.List;

public class Artist extends User {
    private List<Song> songs = new ArrayList<>();

    public Artist(String username, String password) {
        super(username, password, "ARTIST");
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public List<Song> getSongs() {
        return songs;
    }

    public boolean ownsSong(Song song) {
        return songs.contains(song);
    }
}
