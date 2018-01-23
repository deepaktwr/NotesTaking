package proj.me.usecase.repository;

import proj.me.entity.Note;
import proj.me.usecase.exception.ExceptionBundle;
import proj.me.usecase.executor.BackgroundExecutor;
import proj.me.usecase.executor.ForegroundExecutor;

/**
 * Created by root on 23/1/18.
 */

public class NoteInteractorImpl implements NoteInteractor {

    NoteRepository noteRepository;
    BackgroundExecutor backgroundExecutor;
    ForegroundExecutor foregroundExecutor;

    public NoteInteractorImpl(NoteRepository noteRepository, BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor){
        this.noteRepository = noteRepository;
        this.backgroundExecutor = backgroundExecutor;
        this.foregroundExecutor = foregroundExecutor;
    }

    private int noteExecutionType;
    private Callback callback;
    private int noteId;

    @Override
    public void fetchNote(int noteId, Callback callback) {
        noteExecutionType = EXECUTION_TYPE_FETCH;
        this.noteId = noteId;
        this.callback = callback;
        backgroundExecutor.execute(this, TASK_ID);
    }

    private Note note;

    @Override
    public void createNote(Note note, Callback callback) {
        noteExecutionType = EXECUTION_TYPE_CREATE;
        this.note = note;
        this.callback = callback;
        backgroundExecutor.execute(this, TASK_ID);
    }

    @Override
    public void deleteNote(int noteId, Callback callback) {
        noteExecutionType = EXECUTION_TYPE_DELETE;
        this.noteId = noteId;
        this.callback = callback;
        backgroundExecutor.execute(this, TASK_ID);
    }

    @Override
    public void updateNote(Note note, Callback callback) {
        noteExecutionType = EXECUTION_TYPE_UPDATE;
        this.note = note;
        this.callback = callback;
        backgroundExecutor.execute(this, TASK_ID);
    }

    @Override
    public void run() {
        switch(noteExecutionType){
            case EXECUTION_TYPE_CREATE:
                noteRepository.createNote(note, repositoryCallback);
                break;
            case EXECUTION_TYPE_FETCH:
                noteRepository.fetchNote(noteId, repositoryCallback);
                break;
            case EXECUTION_TYPE_UPDATE:
                noteRepository.updateNote(note, repositoryCallback);
                break;
            case EXECUTION_TYPE_DELETE:
                noteRepository.deleteNote(noteId, repositoryCallback);
                break;
        }
    }

    NoteRepository.Callback repositoryCallback = new NoteRepository.Callback() {
        @Override
        public void noteResultSuccess(Note note) {
            backgroundExecutor.finish(TASK_ID);
            notifySuccess(note);
        }

        @Override
        public void noteResultFailed(ExceptionBundle exceptionBundle) {
            backgroundExecutor.finish(TASK_ID);
            notifyError(exceptionBundle);
        }
    };

    void notifySuccess(final Note note){
        foregroundExecutor.post(new Runnable() {
            @Override
            public void run() {
                callback.noteResultSuccess(note, noteExecutionType);
            }
        });
    }

    void notifyError(final ExceptionBundle exceptionBundle){
        foregroundExecutor.post(new Runnable() {
            @Override
            public void run() {
                callback.noteResultFailed(exceptionBundle, noteExecutionType);
            }
        });
    }
}
