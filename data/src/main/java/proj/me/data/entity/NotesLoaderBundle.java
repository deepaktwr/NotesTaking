package proj.me.data.entity;

import android.os.Parcel;

/**
 * Created by root on 23/1/18.
 */

public class NotesLoaderBundle extends LoaderBundle {

    private long createdTime;

    public NotesLoaderBundle(long createdTime){
        if(createdTime != -1) {
            this.createdTime = createdTime;
            selectionArgsCount += 1;
        }
    }

    public NotesLoaderBundle(Parcel in){
        super(in);
        createdTime = in.readLong();
    }

    public long getCreatedTime() {
        return createdTime;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(createdTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotesLoaderBundle> CREATOR = new Creator<NotesLoaderBundle>() {
        @Override
        public NotesLoaderBundle createFromParcel(Parcel parcel) {
            return new NotesLoaderBundle(parcel);
        }

        @Override
        public NotesLoaderBundle[] newArray(int size) {
            return new NotesLoaderBundle[size];
        }
    };
}
