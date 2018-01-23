package proj.me.presentation.usecase;

import proj.me.entity.Note;
import proj.me.usecase.exception.ExceptionBundle;

/**
 * Created by root on 23/1/18.
 */

public interface NotePresentor extends Presentor {

    interface Callback extends View {
        void noteResultSuccess(Note note, int type);
        void noteResultFailed(ExceptionBundle exceptionBundle, int type);
    }

    void fetchNote(int noteId);
    void createNote(Note note);
    void updateNote(Note note);
    void deleteNote(int noteId);
}
