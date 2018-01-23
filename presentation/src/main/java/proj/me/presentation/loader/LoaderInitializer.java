package proj.me.presentation.loader;

import android.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;

/**
 * Created by root on 23/1/18.
 */

public interface LoaderInitializer {

    int NOTE_TYPE_LOADER_ID = 1;
    String NOTE_TYPE_BUNDLE_KEY = "note_bundle";

    int NOTES_TYPE_LOADER_ID = 2;
    String NOTES_TYPE_BUNDLE_KEY = "notes_bundle";


    interface Callback {
        void initLoaderWith(final int loaderId);
    }

    int getLoaderId();
    String getLoaderBundleKey();
    void initializeLoader();
    CursorLoader getCursorLoader(final Bundle args);
    void processCursor(Cursor data);
}
