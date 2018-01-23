package proj.me.presentation.loader;

import android.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 23/1/18.
 */

public class LoaderPresentorImpl implements LoaderPresentor {

    private Map<Integer, LoaderInitializer> loaderInitializerMap;

    public LoaderPresentorImpl() {
        loaderInitializerMap = new HashMap<>();
    }

    @Override
    public LoaderPresentor initializeTypeLoader(LoaderInitializer loaderInitializer){
        loaderInitializerMap.put(loaderInitializer.getLoaderId(), loaderInitializer);
        loaderInitializer.initializeLoader();
        return this;
    }

    @Override
    public CursorLoader getTypeCursorLoader(int loaderId, Bundle args){
        return loaderInitializerMap.get(loaderId).getCursorLoader(args);
    }

    @Override
    public void processTypeCursor(int loaderId, Cursor data) {
        loaderInitializerMap.get(loaderId).processCursor(data);
    }
}
