package proj.me.presentation.usecase;

import java.util.List;

import proj.me.entity.Note;
import proj.me.usecase.exception.ExceptionBundle;
import proj.me.usecase.repository.NotesInteractor;

/**
 * Created by root on 23/1/18.
 */

public class NotesPresentorImpl implements NotesPresentor {

    NotesInteractor notesInteractor;
    Callback callback;

    public NotesPresentorImpl(NotesInteractor notesInteractor, Callback callback){
        this.notesInteractor = notesInteractor;
        this.callback = callback;
    }

    @Override
    public void fetchAllNotes() {
        if(callback != null) callback.startLoading();
        notesInteractor.fetchAllNotes(interactorCallback);
    }

    @Override
    public void viewInForeground() {}
    @Override
    public void viewInBackground() {}

    NotesInteractor.Callback interactorCallback = new NotesInteractor.Callback() {
        @Override
        public void notesResultSuccess(List<Note> notes) {
            if(callback == null) return;
            callback.finishLoading();
            callback.notesResultSuccess(notes);
        }

        @Override
        public void notesResultFailed(ExceptionBundle exceptionBundle) {
            if(callback == null) return;
            callback.finishLoading();
            callback.notesResultfailed(exceptionBundle);
        }
    };
}
