package primecode.registerplus;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nagendralimbu on 30/01/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, DatabaseContract.DatabaseEntry.DATABASE_NAME, null, DatabaseContract.DatabaseEntry.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseContract.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    public boolean insertData(SQLiteDatabase database, String fullName, String address, String inquiry) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.DatabaseEntry.COLUMN_NAME_FULLNAME, fullName);
        contentValues.put(DatabaseContract.DatabaseEntry.COLUMN_NAME_ADDRESS, address);
        contentValues.put(DatabaseContract.DatabaseEntry.COLUMN_NAME_INQUIRY, inquiry);
        //database current timestamp value is set to DEFAULT
        // Insert the new row, returning the primary key value of the new row
        long row = database.insert(DatabaseContract.DatabaseEntry.TABLE_NAME, null, contentValues);
        if(row > 0) return true;

        return false;
    }
}
