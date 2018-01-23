package proj.me.presentation.usecase;

import java.util.List;

import proj.me.entity.Note;
import proj.me.usecase.exception.ExceptionBundle;

/**
 * Created by root on 23/1/18.
 */

public interface NotesPresentor extends Presentor {

    interface Callback extends View {
        void notesResultSuccess(List<Note> notes);
        void notesResultfailed(ExceptionBundle exceptionBundle);
    }

    void fetchAllNotes();

}
