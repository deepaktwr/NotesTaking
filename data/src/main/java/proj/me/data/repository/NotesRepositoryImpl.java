package proj.me.data.repository;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import proj.me.data.provider.NoteContract;
import proj.me.entity.Note;
import proj.me.usecase.exception.ExceptionBundle;
import proj.me.usecase.repository.NotesRepository;

/**
 * Created by root on 23/1/18.
 */

public class NotesRepositoryImpl implements NotesRepository {

    Context applicationContext;

    public NotesRepositoryImpl(Context context){
        applicationContext = context.getApplicationContext();
    }

    @Override
    public void fetchAllNotes(Callback callback) {
        Cursor cursor = applicationContext.getContentResolver().query(NoteContract.Notes.CONTENT_URI, null, null, null, null);

        if(cursor == null || !cursor.moveToFirst()){
            callback.notesResultFailed(new NotesExceptionBundle(new IllegalAccessException("notes not available"), "no notes available"));
            return;
        }

        List<Note> noteList = new ArrayList<>();

        do{
            Note note = new Note();
            note.setId(cursor.getInt(cursor.getColumnIndex(NoteContract.Notes._ID)));
            note.setTitle(cursor.getString(cursor.getColumnIndex(NoteContract.Notes.TITLE)));
            note.setText(cursor.getString(cursor.getColumnIndex(NoteContract.Notes.TEXT)));
            note.setPathUri(cursor.getString(cursor.getColumnIndex(NoteContract.Notes.MEDIA_PATH)));
            note.setCreateTimestamp(cursor.getLong(cursor.getColumnIndex(NoteContract.Notes.CREATED_TIME)));
            note.setUpdateTimestamp(cursor.getLong(cursor.getColumnIndex(NoteContract.Notes.UPDATED_TIME)));

            noteList.add(note);
        }while(cursor.moveToNext());

        callback.notesResultSuccess(noteList);
    }

    private static class NotesExceptionBundle implements ExceptionBundle{
        Exception exception;
        String cause;

        NotesExceptionBundle(Exception exception, String cause){
            this.exception = exception;
            this.cause = cause;
        }


        @Override
        public Exception getException() {
            return exception;
        }

        @Override
        public String getExceptionDetail() {
            return cause;
        }
    }
}
