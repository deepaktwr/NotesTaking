package proj.me.notetaking;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;

import proj.me.presentation.loader.LoaderPresentor;

public abstract class BaseActivityWithLoader extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    protected LoaderPresentor loaderPresentor;
    protected Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initializeLoader();
        initializePresentor();
    }

    protected abstract void initializeLoader();
    protected abstract void initializePresentor();

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(loaderPresentor == null) return null;
        return loaderPresentor.getTypeCursorLoader(id, args);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(loaderPresentor != null && data != null) loaderPresentor.processTypeCursor(loader.getId(), data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

}