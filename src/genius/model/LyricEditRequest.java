package genius.model;

public class LyricEditRequest {
    private Song song;
    private User editor;
    private String proposedLyrics;
    private boolean approved;

    public LyricEditRequest(Song song, User editor, String proposedLyrics) {
        this.song = song;
        this.editor = editor;
        this.proposedLyrics = proposedLyrics;
    }

    public void approve() {
        this.approved = true;
    }

    public boolean isApproved() {
        return approved;
    }

    public Song getSong() {
        return song;
    }

    public User getEditor() {
        return editor;
    }

    public String getProposedLyrics() {
        return proposedLyrics;
    }
}
