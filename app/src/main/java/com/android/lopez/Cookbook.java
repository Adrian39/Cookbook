package com.android.lopez;

import android.app.Application;

import com.android.lopez.cookbook.SQLiteDatabase.DBAdapter;

/**
 * Created by adria on 2/6/2017.
 */
public class Cookbook extends Application {
    private DBAdapter dbAdapter;

    @Override
    public void onCreate(){
        dbAdapter = new DBAdapter(getApplicationContext());
        dbAdapter.openDB();
    }
}
