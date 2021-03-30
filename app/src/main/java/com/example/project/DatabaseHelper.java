package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private SQLiteDatabase database;
    // Table Name
    public static final String TABLE_NAME = "MOMENTS";
    // Table columns
    public static final String _ID = "_id";
    public static final String CATEGORY = "category";
    public static final String TITLE = "title";
    public static final String MDATE = "date";
    public static final String COMMENTS = "comments";
    // Database Information
    static final String DB_NAME = "PreciousMoments.DB";
    // database version
    static final int DB_VERSION = 1;
    // Creating table query
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CATEGORY + " TEXT NOT NULL, " + TITLE +
            " TEXT NOT NULL, " + MDATE + " TEXT, " + COMMENTS + " CHAR(250));";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int addReminder(Reminder reminder){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_TITLE , reminder.getTitle());
        values.put(KEY_DATE , reminder.getDate());
        values.put(KEY_TIME , reminder.getTime());
        values.put(KEY_REPEAT , reminder.getRepeat());
        values.put(KEY_REPEAT_NO , reminder.getRepeatNo());
        values.put(KEY_REPEAT_TYPE, reminder.getRepeatType());
        values.put(KEY_ACTIVE, reminder.getActive());

        // Inserting Row
        long ID = db.insert(TABLE_REMINDERS, null, values);
        db.close();
        return (int) ID;
    }
}
