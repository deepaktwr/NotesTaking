package proj.me.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 15/8/17.
 */

public abstract class LoaderBundle implements Parcelable {
    protected int selectionArgsCount;

    protected LoaderBundle(){}

    protected LoaderBundle(Parcel in){
        selectionArgsCount = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(selectionArgsCount);
    }

    public int getSelectionArgsCount() {
        return selectionArgsCount;
    }
}
