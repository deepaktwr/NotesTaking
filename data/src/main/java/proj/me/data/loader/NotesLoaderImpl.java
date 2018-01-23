package proj.me.data.loader;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import proj.me.data.entity.NotesLoaderBundle;
import proj.me.data.provider.NoteContract;
import proj.me.entity.Note;
import proj.me.presentation.loader.LoaderCallback;
import proj.me.presentation.loader.LoaderInitializer;

/**
 * Created by root on 23/1/18.
 */

public class NotesLoaderImpl implements LoaderInitializer {

    Context applicationContext;
    LoaderCallback.Notes callback;
    public NotesLoaderImpl(Context context, LoaderCallback.Notes callback){
        applicationContext = context.getApplicationContext();
        this.callback = callback;
    }

    @Override
    public int getLoaderId() {
        return NOTES_TYPE_LOADER_ID;
    }

    @Override
    public String getLoaderBundleKey() {
        return NOTES_TYPE_BUNDLE_KEY;
    }

    @Override
    public void initializeLoader() {
        callback.initLoaderWith(NOTES_TYPE_LOADER_ID);
    }

    @Override
    public CursorLoader getCursorLoader(Bundle args) {
        String selection = null;
        String[] selectionArgs = null;

        NotesLoaderBundle notesLoaderBundle = args == null || !args.containsKey(NOTES_TYPE_BUNDLE_KEY) ? null : (NotesLoaderBundle) args.getParcelable(NOTES_TYPE_BUNDLE_KEY);

        if(notesLoaderBundle != null && notesLoaderBundle.getSelectionArgsCount() > 0){
            selection = "";
            selectionArgs = new String[notesLoaderBundle.getSelectionArgsCount()];
            int index = 0;

            if(notesLoaderBundle.getCreatedTime() != -1){
                selection += NoteContract.Notes.CREATED_TIME +" > ? ";
                selectionArgs[index++] = ""+notesLoaderBundle.getCreatedTime();
            }
        }

        String[] selectionsProjection = {NoteContract.Notes._ID, NoteContract.Notes.TITLE, NoteContract.Notes.TEXT, NoteContract.Notes.MEDIA_PATH, NoteContract.Notes.CREATED_TIME, NoteContract.Notes.UPDATED_TIME};
        String sortOrder = NoteContract.Notes.SORT_ORDER_DEFAULT;
        return new CursorLoader(applicationContext, NoteContract.Notes.CONTENT_URI, selectionsProjection, selection, selectionArgs, sortOrder);
    }

    @Override
    public void processCursor(Cursor data) {
        List<Note> noteList = new ArrayList<>();

        if(data.moveToFirst()){
            do{
                Note note = new Note();
                note.setId(data.getInt(data.getColumnIndex(NoteContract.Notes._ID)));
                note.setTitle(data.getString(data.getColumnIndex(NoteContract.Notes.TITLE)));
                note.setText(data.getString(data.getColumnIndex(NoteContract.Notes.TEXT)));
                note.setPathUri(data.getString(data.getColumnIndex(NoteContract.Notes.MEDIA_PATH)));
                note.setCreateTimestamp(data.getLong(data.getColumnIndex(NoteContract.Notes.CREATED_TIME)));
                note.setUpdateTimestamp(data.getLong(data.getColumnIndex(NoteContract.Notes.UPDATED_TIME)));

                noteList.add(note);
            }while(data.moveToNext());
        }

        callback.notesDetailsChanged(noteList);
    }
}
