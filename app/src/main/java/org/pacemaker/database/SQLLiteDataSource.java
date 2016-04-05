package org.pacemaker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.pacemaker.models.User;

/**
 * Created by colmcarew on 01/04/16.
 */
public class SQLLiteDataSource {
    private SQLLiteDatabaseHelper dbHelper;

    private SQLiteDatabase database;

    public final static String USER_TABLE = "user"; // name of table
    public final static String USER_ID = "userId"; // id value for employee
    public final static String FIRST_NAME = "firstName";  // name of employee
    public final static String LAST_NAME = "lastName";  // name of employee

    public SQLLiteDataSource(Context context) {
        dbHelper = new SQLLiteDatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public long saveUser(User user) {
        dbHelper.onCreate(database);
        deleteUsersFromDb();
        ContentValues values = new ContentValues();
        values.put(USER_ID, user.id);
        values.put(FIRST_NAME, user.firstname);
        values.put(LAST_NAME, user.lastname);
        return database.insert(USER_TABLE, null, values);
    }

    public Cursor getUser() {
        String[] cols = new String[]{USER_ID, FIRST_NAME, LAST_NAME};
        //There should only ever be 1 user logged in
        return database.query(true, USER_TABLE, cols,
                null,
                null, null, null, null, "1");
    }

    public void deleteUsersFromDb() {
        database.execSQL("DELETE FROM " + USER_TABLE + ";");
    }


}
