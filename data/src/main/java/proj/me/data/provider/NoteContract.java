package proj.me.data.provider;

import android.content.ContentResolver;
import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by root on 23/1/18.
 */

public class NoteContract {
    public static final String AUTHORITY = "proj.me.notetaking.note";
    public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY);

    public static final int NOTES_DIR = 1;
    public static final int NOTES_ID = 2;

    public static final UriMatcher URI_MATCHER;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

        URI_MATCHER.addURI(AUTHORITY, Notes.CONTENT_PATH, NOTES_DIR);
        URI_MATCHER.addURI(AUTHORITY, Notes.CONTENT_PATH+"/#", NOTES_ID);
    }

    public static final class Notes implements BaseColumns{
        public static final String CONTENT_PATH = "notes";
        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(NoteContract.CONTENT_URI, CONTENT_PATH);
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "vnd.proj.me.notetaking.note."+CONTENT_PATH;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "vnd.proj.me.notetaking.note."+CONTENT_PATH;

        public static final String TITLE = "title";
        public static final String TEXT = "text";
        public static final String MEDIA_PATH = "media_path";
        public static final String CREATED_TIME = "created_time";
        public static final String UPDATED_TIME = "updated_time";

        public static final String[] ALL_PROJECTION = {_ID, TITLE, TEXT, MEDIA_PATH, CREATED_TIME, UPDATED_TIME};

        public static final String SORT_ORDER_DEFAULT = _ID + " ASC";
    }

    public interface Schema {
        String DB_NAME = "notes.db";
        int VERSION = 1;

        String TABLE_NOTES = Notes.CONTENT_PATH;
        String[] TABLE_NOTES_PROJECTIONS = Notes.ALL_PROJECTION;

        String createTableNotes();
    }
}
