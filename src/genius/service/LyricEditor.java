package genius.service;

import genius.model.*;

public class LyricEditor {
    private final DataService dataService;

    public LyricEditor(DataService dataService) {
        this.dataService = dataService;
    }

    public LyricEditRequest submitEdit(Song song, User editor, String newLyrics) {
        if ("USER".equals(editor.getRole())) {
            LyricEditRequest request = new LyricEditRequest(song, editor, newLyrics);
            dataService.addEditRequest(request);
            return request;
        }
        return null;
    }

    public boolean approveEdit(LyricEditRequest request, User approver) {
        if (approver instanceof Admin ||
                (approver instanceof Artist artist && artist.ownsSong(request.getSong()))) {

            request.getSong().updateLyrics(request.getProposedLyrics());
            request.approve();
            return true;
        }
        return false;
    }
}
