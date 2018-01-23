package proj.me.data.entity;

import android.os.Parcel;

/**
 * Created by root on 23/1/18.
 */

public class NoteLoaderBundle extends LoaderBundle {

    private int noteId;
    public NoteLoaderBundle(int noteId){
        if(noteId > 0){
            this.noteId = noteId;
            selectionArgsCount += 1;
        }
    }

    public NoteLoaderBundle(Parcel in){
        super(in);
        noteId = in.readInt();
    }

    public int getNoteId() {
        return noteId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(noteId);
    }

    public static final Creator<NoteLoaderBundle> CREATOR = new Creator<NoteLoaderBundle>() {
        @Override
        public NoteLoaderBundle createFromParcel(Parcel parcel) {
            return new NoteLoaderBundle(parcel);
        }

        @Override
        public NoteLoaderBundle[] newArray(int size) {
            return new NoteLoaderBundle[size];
        }
    };
}
