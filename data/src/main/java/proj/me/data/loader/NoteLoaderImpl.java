package proj.me.data.loader;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;

import proj.me.data.entity.NoteLoaderBundle;
import proj.me.data.provider.NoteContract;
import proj.me.entity.Note;
import proj.me.presentation.loader.LoaderCallback;
import proj.me.presentation.loader.LoaderInitializer;

/**
 * Created by root on 23/1/18.
 */

public class NoteLoaderImpl implements LoaderInitializer {

    Context applicationContext;
    LoaderCallback.NoteDetails callback;

    public NoteLoaderImpl(Context context, LoaderCallback.NoteDetails callback){
        applicationContext = context.getApplicationContext();
        this.callback = callback;
    }
    @Override
    public int getLoaderId() {
        return NOTE_TYPE_LOADER_ID;
    }

    @Override
    public String getLoaderBundleKey() {
        return NOTE_TYPE_BUNDLE_KEY;
    }

    @Override
    public void initializeLoader() {
        callback.initLoaderWith(NOTE_TYPE_LOADER_ID);
    }

    @Override
    public CursorLoader getCursorLoader(Bundle args) {
        String selection = null;
        String[] selectionArgs = null;

        NoteLoaderBundle noteLoaderBundle = args == null || !args.containsKey(NOTE_TYPE_BUNDLE_KEY) ? null : (NoteLoaderBundle) args.getParcelable(NOTE_TYPE_BUNDLE_KEY);

        if(noteLoaderBundle != null && noteLoaderBundle.getSelectionArgsCount() > 0){
            selection = "";
            selectionArgs = new String[noteLoaderBundle.getSelectionArgsCount()];
            int index = 0;

            if(noteLoaderBundle.getNoteId() > 0){
                selection += NoteContract.Notes._ID +" = ? ";
                selectionArgs[index++] = ""+noteLoaderBundle.getNoteId();
            }

        }

        String[] selectionsProjection = {NoteContract.Notes._ID, NoteContract.Notes.TITLE, NoteContract.Notes.TEXT};
        String sortOrder = NoteContract.Notes.SORT_ORDER_DEFAULT;

        return new CursorLoader(applicationContext, NoteContract.Notes.CONTENT_URI, selectionsProjection, selection, selectionArgs, sortOrder);
    }

    @Override
    public void processCursor(Cursor data) {
        Note note = null;

        if(data.moveToFirst()){
            note = new Note();

            note.setId(data.getInt(data.getColumnIndex(NoteContract.Notes._ID)));
            note.setTitle(data.getString(data.getColumnIndex(NoteContract.Notes.TITLE)));
            note.setText(data.getString(data.getColumnIndex(NoteContract.Notes.TEXT)));
            note.setPathUri(data.getString(data.getColumnIndex(NoteContract.Notes.MEDIA_PATH)));
            note.setCreateTimestamp(data.getLong(data.getColumnIndex(NoteContract.Notes.CREATED_TIME)));
            note.setUpdateTimestamp(data.getLong(data.getColumnIndex(NoteContract.Notes.UPDATED_TIME)));
        }

        callback.noteDetailsChanged(note);
    }
}
