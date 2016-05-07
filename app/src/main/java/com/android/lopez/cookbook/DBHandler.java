package com.android.lopez.cookbook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by JacoboAdrian on 5/4/2016.
 */
public class DBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "cookbook";     //Database name
    private static final int SCHEME_VERSION = 1;

    public DBHandler(Context context) {
        super(context, DB_NAME, null, SCHEME_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
