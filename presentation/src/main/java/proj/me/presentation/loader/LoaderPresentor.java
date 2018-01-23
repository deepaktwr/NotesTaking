package proj.me.presentation.loader;

import android.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;

/**
 * Created by root on 23/1/18.
 */

public interface LoaderPresentor {
    LoaderPresentor initializeTypeLoader(LoaderInitializer loaderInitializer);
    CursorLoader getTypeCursorLoader(int loaderId, Bundle args);
    void processTypeCursor(int loaderId, Cursor data);
}
