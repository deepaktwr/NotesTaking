package proj.me.usecase.repository;

import proj.me.entity.Note;
import proj.me.usecase.exception.ExceptionBundle;

/**
 * Created by root on 23/1/18.
 */

public interface NoteRepository {

    interface Callback {
        void noteResultSuccess(Note note);
        void noteResultFailed(ExceptionBundle exceptionBundle);
    }

    void fetchNote(int noteId, Callback callback);
    void createNote(Note note, Callback callback);
    void deleteNote(int noteId, Callback callback);
    void updateNote(Note note, Callback callback);
}
