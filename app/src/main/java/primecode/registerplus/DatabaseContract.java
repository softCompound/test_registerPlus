package primecode.registerplus;

import android.provider.BaseColumns;

/**
 * Created by nagendralimbu on 30/01/2017.
 */

final class DatabaseContract {

     static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DatabaseEntry.TABLE_NAME + " (" +
                    DatabaseEntry._ID + " INTEGER PRIMARY KEY," +
                    DatabaseEntry.COLUMN_NAME_FULLNAME + " TEXT," +
                    DatabaseEntry.COLUMN_NAME_ADDRESS + " TEXT)" +
                    DatabaseEntry.COLUMN_NAME_INQUIRY + " TEXT)" +
                    DatabaseEntry.COLUMN_NAME_TIME + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

     static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DatabaseEntry.TABLE_NAME;

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DatabaseContract() {}

     static class DatabaseEntry implements BaseColumns{
         static final int DATABASE_VERSION = 1;
         static final String DATABASE_NAME = "RegisterPlus.db";

         static final String TABLE_NAME = "tableone";
         static final String COLUMN_NAME_FULLNAME = "fullname";
         static final String COLUMN_NAME_ADDRESS = "address";
         static final String COLUMN_NAME_INQUIRY = "inquiry";
         static final String COLUMN_NAME_TIME = "time";


     }


}
