package proj.me.data.repository;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import proj.me.data.provider.NoteContract;
import proj.me.entity.Note;
import proj.me.usecase.exception.ExceptionBundle;
import proj.me.usecase.repository.NoteRepository;

/**
 * Created by root on 23/1/18.
 */

public class NoteRepositoryImpl implements NoteRepository {

    Context applicationContext;

    public NoteRepositoryImpl(Context context){
        applicationContext = context.getApplicationContext();
    }

    @Override
    public void fetchNote(int noteId, Callback callback) {
        Cursor cursor = applicationContext.getContentResolver().query(ContentUris.withAppendedId(NoteContract.Notes.CONTENT_URI, noteId), null, null, null, null);
        if(cursor == null || !cursor.moveToFirst()){
            callback.noteResultFailed(new NoteExceptionBundle(new IllegalArgumentException("note not found for this id"), "no note available for this id"));
            return;
        }

        Note note = new Note();
        note.setId(cursor.getInt(cursor.getColumnIndex(NoteContract.Notes._ID)));
        note.setTitle(cursor.getString(cursor.getColumnIndex(NoteContract.Notes.TITLE)));
        note.setText(cursor.getString(cursor.getColumnIndex(NoteContract.Notes.TEXT)));
        note.setPathUri(cursor.getString(cursor.getColumnIndex(NoteContract.Notes.MEDIA_PATH)));
        note.setCreateTimestamp(cursor.getLong(cursor.getColumnIndex(NoteContract.Notes.CREATED_TIME)));
        note.setUpdateTimestamp(cursor.getLong(cursor.getColumnIndex(NoteContract.Notes.UPDATED_TIME)));

        callback.noteResultSuccess(note);
    }

    @Override
    public void createNote(Note note, Callback callback) {
        if(note == null){
            callback.noteResultFailed(new NoteExceptionBundle(new IllegalArgumentException("note must not be null"), "note must not be null"));
            return;
        }

        if(note.getId() != -1) {
            Cursor cursor = applicationContext.getContentResolver().query(ContentUris.withAppendedId(NoteContract.Notes.CONTENT_URI, note.getId()), null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                updateNote(note, callback);
                return;
            }
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteContract.Notes.TITLE, note.getTitle());
        contentValues.put(NoteContract.Notes.TEXT, note.getText());
        contentValues.put(NoteContract.Notes.MEDIA_PATH, note.getPathUri());
        contentValues.put(NoteContract.Notes.CREATED_TIME, System.currentTimeMillis());
        //contentValues.put(NoteContract.Notes.UPDATED_TIME, note.getUpdateTimestamp());

        applicationContext.getContentResolver().insert(NoteContract.Notes.CONTENT_URI, contentValues);

        callback.noteResultSuccess(null);
    }

    @Override
    public void deleteNote(int noteId, Callback callback) {
        Cursor cursor = applicationContext.getContentResolver().query(ContentUris.withAppendedId(NoteContract.Notes.CONTENT_URI, noteId), null, null, null, null);
        if(cursor == null || !cursor.moveToFirst()){
            callback.noteResultFailed(new NoteExceptionBundle(new IllegalArgumentException("note not found for this id"), "no note available for this id"));
            return;
        }

        applicationContext.getContentResolver().delete(ContentUris.withAppendedId(NoteContract.Notes.CONTENT_URI, noteId), null, null);

        callback.noteResultSuccess(null);
    }

    @Override
    public void updateNote(Note note, Callback callback) {
        if(note == null){
            callback.noteResultFailed(new NoteExceptionBundle(new IllegalArgumentException("note must not be null"), "note must not be null"));
            return;
        }

        Cursor cursor = applicationContext.getContentResolver().query(ContentUris.withAppendedId(NoteContract.Notes.CONTENT_URI, note.getId()), null, null, null, null);
        if(cursor == null || !cursor.moveToFirst()){
            callback.noteResultFailed(new NoteExceptionBundle(new IllegalArgumentException("note not found for this id"), "no note available for this id"));
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteContract.Notes.TITLE, note.getTitle());
        contentValues.put(NoteContract.Notes.TEXT, note.getText());
        contentValues.put(NoteContract.Notes.MEDIA_PATH, note.getPathUri());
        //contentValues.put(NoteContract.Notes.CREATED_TIME, note.getCreateTimestamp());
        contentValues.put(NoteContract.Notes.UPDATED_TIME, System.currentTimeMillis());

        applicationContext.getContentResolver().update(ContentUris.withAppendedId(NoteContract.Notes.CONTENT_URI, note.getId()), contentValues, null, null);

        callback.noteResultSuccess(null);
    }

    private static class NoteExceptionBundle implements ExceptionBundle{
        Exception exception;
        String cause;

        NoteExceptionBundle(Exception exception, String cause){
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
