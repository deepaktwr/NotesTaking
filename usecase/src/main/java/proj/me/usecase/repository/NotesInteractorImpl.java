package proj.me.usecase.repository;

import java.util.List;

import proj.me.entity.Note;
import proj.me.usecase.exception.ExceptionBundle;
import proj.me.usecase.executor.BackgroundExecutor;
import proj.me.usecase.executor.ForegroundExecutor;

/**
 * Created by root on 23/1/18.
 */

public class NotesInteractorImpl implements NotesInteractor {

    NotesRepository notesRepository;
    BackgroundExecutor backgroundExecutor;
    ForegroundExecutor foregroundExecutor;

    public NotesInteractorImpl(NotesRepository notesRepository, BackgroundExecutor backgroundExecutor,ForegroundExecutor foregroundExecutor){
        this.notesRepository = notesRepository;
        this.backgroundExecutor = backgroundExecutor;
        this.foregroundExecutor = foregroundExecutor;
    }

    private Callback callback;

    @Override
    public void fetchAllNotes(Callback callback) {
        this.callback = callback;
        backgroundExecutor.execute(this, TASK_ID);
    }

    @Override
    public void run() {
        notesRepository.fetchAllNotes(repositoryCallback);
    }

    NotesRepository.Callback repositoryCallback = new NotesRepository.Callback() {
        @Override
        public void notesResultSuccess(List<Note> notes) {
            backgroundExecutor.finish(TASK_ID);
            notifySuccess(notes);
        }

        @Override
        public void notesResultFailed(ExceptionBundle exceptionBundle) {
            backgroundExecutor.finish(TASK_ID);
            notifyError(exceptionBundle);
        }
    };

    void notifySuccess(final List<Note> notes){
        foregroundExecutor.post(new Runnable() {
            @Override
            public void run() {
                callback.notesResultSuccess(notes);
            }
        });
    }

    void notifyError(final ExceptionBundle exceptionBundle){
        foregroundExecutor.post(new Runnable() {
            @Override
            public void run() {
                callback.notesResultFailed(exceptionBundle);
            }
        });
    }
}
