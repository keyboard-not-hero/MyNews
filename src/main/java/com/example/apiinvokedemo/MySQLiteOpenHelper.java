package com.example.apiinvokedemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by thompson on 16-10-19.
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper{
    private static final String CREATE_TABLE_ONE = "create table launone (title text,time text,content text)";
    private static final String CREATE_TABLE_TWO = "create table launtwo ("
            + "title text,"
            + "time text,"
            + "content text)";
    private static final String CREATE_TABLE_THREE = "create table launthree (title text,time text,content text)";
    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                              int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ONE);
        db.execSQL(CREATE_TABLE_TWO);
        db.execSQL(CREATE_TABLE_THREE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
