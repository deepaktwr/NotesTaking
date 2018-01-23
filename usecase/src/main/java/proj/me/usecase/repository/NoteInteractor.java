package proj.me.usecase.repository;

import proj.me.entity.Note;
import proj.me.usecase.Interactor;
import proj.me.usecase.exception.ExceptionBundle;

/**
 * Created by root on 23/1/18.
 */

public interface NoteInteractor extends Interactor{
    int TASK_ID = 1;

    int EXECUTION_TYPE_FETCH = 1;
    int EXECUTION_TYPE_CREATE = 2;
    int EXECUTION_TYPE_DELETE = 3;
    int EXECUTION_TYPE_UPDATE = 4;

    interface Callback {
        void noteResultSuccess(Note note, int type);
        void noteResultFailed(ExceptionBundle exceptionBundle, int type);
    }

    void fetchNote(int noteId, Callback callback);
    void createNote(Note note, Callback callback);
    void deleteNote(int noteId, Callback callback);
    void updateNote(Note note, Callback callback);

}
