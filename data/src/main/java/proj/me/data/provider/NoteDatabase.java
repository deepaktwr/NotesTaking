package proj.me.data.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by root on 23/1/18.
 */

public class NoteDatabase extends SQLiteOpenHelper implements NoteContract.Schema {

    public NoteDatabase(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTableNotes());
    }

    @Override
    public String createTableNotes() {
        return "create table "+ TABLE_NOTES + " ( "
                + TABLE_NOTES_PROJECTIONS[0] + " integer primary key AUTOINCREMENT , "
                + TABLE_NOTES_PROJECTIONS[1] + " text , "
                + TABLE_NOTES_PROJECTIONS[2] + " text , "
                + TABLE_NOTES_PROJECTIONS[3] + " text , "
                + TABLE_NOTES_PROJECTIONS[4] + " integer , "
                + TABLE_NOTES_PROJECTIONS[5] + " integer , "
                + " UNIQUE ( "
                + TABLE_NOTES_PROJECTIONS[1]+ " , "
                + TABLE_NOTES_PROJECTIONS[4]
                + " )"
                + " );";
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
