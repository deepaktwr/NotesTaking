package proj.me.data.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by root on 23/1/18.
 */

public class NoteProvider extends ContentProvider {

    private NoteDatabase noteDatabase;

    @Override
    public boolean onCreate() {
        noteDatabase = new NoteDatabase(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        switch(NoteContract.URI_MATCHER.match(uri)){
            case NoteContract.NOTES_DIR:
                return NoteContract.Notes.CONTENT_TYPE;
            case NoteContract.NOTES_ID:
                return NoteContract.Notes.CONTENT_ITEM_TYPE;
        }
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase sqLiteDatabase = noteDatabase.getReadableDatabase();
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();

        String whereClause = null;
        String defaultSortOrder = null;

        switch(NoteContract.URI_MATCHER.match(uri)){
            case NoteContract.NOTES_ID:
                whereClause = NoteContract.Notes._ID + " = " + uri.getLastPathSegment();
            case NoteContract.NOTES_DIR:
                sqLiteQueryBuilder.setTables(NoteContract.Schema.TABLE_NOTES);
                defaultSortOrder = NoteContract.Notes.SORT_ORDER_DEFAULT;
                break;
            default:
                throw new IllegalArgumentException("uri not supported for query " + uri);
        }

        if (whereClause != null) sqLiteQueryBuilder.appendWhere(whereClause);

        Cursor cursor = sqLiteQueryBuilder.query(sqLiteDatabase, projection, selection, selectionArgs, null, null,
                TextUtils.isEmpty(sortOrder) ? defaultSortOrder : sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase sqLiteDatabase = null;
        long itemId = -2;

        switch(NoteContract.URI_MATCHER.match(uri)){
            case NoteContract.NOTES_DIR:
                sqLiteDatabase = noteDatabase.getWritableDatabase();
                itemId = sqLiteDatabase.insertWithOnConflict(NoteContract.Schema.TABLE_NOTES, null, values, SQLiteDatabase.CONFLICT_IGNORE);
                break;
            case NoteContract.NOTES_ID:
                sqLiteDatabase = noteDatabase.getWritableDatabase();
                itemId = sqLiteDatabase.insertWithOnConflict(NoteContract.Schema.TABLE_NOTES, null, values, SQLiteDatabase.CONFLICT_IGNORE);
                break;
        }

        if (itemId != -2) return getUriForId(itemId, uri);

        throw new IllegalArgumentException("uri not supported for insertion " + uri);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = noteDatabase.getWritableDatabase();

        int updateCount = 0;
        String additionalWhereClause = "";

        switch(NoteContract.URI_MATCHER.match(uri)){
            case NoteContract.NOTES_ID:
                additionalWhereClause = NoteContract.Notes._ID + " = " + uri.getLastPathSegment()
                        + (TextUtils.isEmpty(selection) ? "" : " AND " + selection);
            case NoteContract.NOTES_DIR:
                updateCount = sqLiteDatabase.update(NoteContract.Schema.TABLE_NOTES,
                        values, TextUtils.isEmpty(additionalWhereClause) ? selection : additionalWhereClause, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("uri not supported for update " + uri);
        }

        if (updateCount > 0) getContext().getContentResolver().notifyChange(uri, null);

        return updateCount;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = noteDatabase.getWritableDatabase();

        int deleteCount = 0;
        String additionalWhereClause = "";

        switch(NoteContract.URI_MATCHER.match(uri)){
            case NoteContract.NOTES_ID:
                additionalWhereClause = NoteContract.Notes._ID + " = " + uri.getLastPathSegment()
                        + (TextUtils.isEmpty(selection) ? "" : " AND " + selection);
            case NoteContract.NOTES_DIR:
                deleteCount = sqLiteDatabase.delete(NoteContract.Schema.TABLE_NOTES,
                        TextUtils.isEmpty(additionalWhereClause) ? selection : additionalWhereClause, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("uri not supported for deletion " + uri);
        }

        if (deleteCount > 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return deleteCount;
    }

    Uri getUriForId(long id, Uri uri) {
        if (id < 0) return null;
        Uri itemUri = ContentUris.withAppendedId(uri, id);
        getContext().getContentResolver().notifyChange(itemUri, null);
        return itemUri;
    }
}
