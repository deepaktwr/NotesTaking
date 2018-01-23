package proj.me.presentation.usecase;

import proj.me.entity.Note;
import proj.me.usecase.exception.ExceptionBundle;
import proj.me.usecase.repository.NoteInteractor;

/**
 * Created by root on 23/1/18.
 */

public class NotePresentorImpl implements NotePresentor {

    NoteInteractor noteInteractor;
    Callback callback;

    public NotePresentorImpl(NoteInteractor noteInteractor, Callback callback){
        this.noteInteractor = noteInteractor;
        this.callback = callback;
    }

    @Override
    public void fetchNote(int noteId) {
        if(callback != null) callback.startLoading();
        noteInteractor.fetchNote(noteId, interactorCallback);
    }

    @Override
    public void createNote(Note note) {
        if(callback != null) callback.startLoading();
        noteInteractor.createNote(note, interactorCallback);
    }

    @Override
    public void updateNote(Note note) {
        if(callback != null) callback.startLoading();
        noteInteractor.updateNote(note, interactorCallback);
    }

    @Override
    public void deleteNote(int noteId) {
        if(callback != null) callback.startLoading();
        noteInteractor.deleteNote(noteId, interactorCallback);
    }

    @Override
    public void viewInForeground() {}

    @Override
    public void viewInBackground() {}

    NoteInteractor.Callback interactorCallback = new NoteInteractor.Callback() {
        @Override
        public void noteResultSuccess(Note note, int type) {
            if(callback == null) return;
            callback.finishLoading();
            callback.noteResultSuccess(note, type);
        }

        @Override
        public void noteResultFailed(ExceptionBundle exceptionBundle, int type) {
            if(callback == null) return;
            callback.finishLoading();
            callback.noteResultFailed(exceptionBundle, type);
        }
    };
}
