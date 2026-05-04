package com.example.smartqueueapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME    = "smartqueue.db";
    private static final int    DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE tokens (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "token_number INTEGER," +
                "service TEXT," +
                "wait_time INTEGER," +
                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)");
}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tokens");
        onCreate(db);
    }

    public void insertToken(int tokenNumber, String service, int waitTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("token_number", tokenNumber);
        values.put("service",      service);
        values.put("wait_time",    waitTime);
        db.insert("tokens", null, values);
        db.close();
    }
}