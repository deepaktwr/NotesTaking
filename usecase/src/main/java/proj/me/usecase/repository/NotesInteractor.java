package proj.me.usecase.repository;

import java.util.List;

import proj.me.entity.Note;
import proj.me.usecase.Interactor;
import proj.me.usecase.exception.ExceptionBundle;

/**
 * Created by root on 23/1/18.
 */

public interface NotesInteractor extends Interactor {

    int TASK_ID = 2;

    interface Callback {
        void notesResultSuccess(List<Note> notes);
        void notesResultFailed(ExceptionBundle exceptionBundle);
    }

    void fetchAllNotes(Callback callback);

}
