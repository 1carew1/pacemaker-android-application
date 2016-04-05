package org.pacemaker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import org.pacemaker.controllers.Dashboard;

/**
 * Created by colmcarew on 01/04/16.
 */
public class SQLLiteDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "pacemaker";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "DatabaseHelper";

    // Database creation sql statement
    private static final String USER_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + SQLLiteDataSource.USER_TABLE + " ("
                    + SQLLiteDataSource.USER_ID + " INTEGER PRIMARY KEY, "
                    + SQLLiteDataSource.FIRST_NAME + " TEXT NOT NULL,"
                    + SQLLiteDataSource.LAST_NAME + " TEXT NOT NULL"
                    + ");";

    public SQLLiteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
      //  database.execSQL("DROP TABLE IF EXISTS " + SQLLiteDataSource.USER_TABLE);
        Log.i(TAG, USER_TABLE_CREATE);
        database.execSQL(USER_TABLE_CREATE);
    }

    // Method is called during an upgrade of the database,
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.i(TAG, "Updating " + SQLLiteDataSource.USER_TABLE + " table");
        database.execSQL("DROP TABLE IF EXISTS " + SQLLiteDataSource.USER_TABLE);
        onCreate(database);
    }

}