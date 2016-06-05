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

    private static final String TABLE_1_NAME = "Recipes", T1_KEY_ID = "_id", T1_NAME = "Name",
            T1_CATEGORY = "Category", T1_TIME = "Time", T1_PREPARATION = "Preparation",
            T1_SERVINGS = "Servings", T1_IMAGE_FILE = "ImageFile";      //Table 1 = Recipe Database

    private static final String TABLE_2_NAME = "Ingredients", T2_KEY_ID = "_id", T2_NAME = "Name",
            T2_CATEGORY = "Category", T2_IN_STOCK = "InStock", T2_NEEDED = "Needed";                    //Table 2 = Ingredient Database

    private static final String TABLE_3_NAME = "RecipeIngredient", T3_FK_RECIPE = "RecipeID",
            T3_FK_INGREDIENT = "IngredientID", T3_QUANTITY = "Quantity";                                //Table 3 = RecipeIngredientLink Database


    public DBHandler(Context context) {
        super(context, DB_NAME, null, SCHEME_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //----TABLE 1 QUERY: RECIPES TABLE----//
        db.execSQL("CREATE TABLE " + TABLE_1_NAME + "(" + T1_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                T1_NAME + " VARCHAR(50), " +
                T1_CATEGORY + " VARCHAR(50), " +
                T1_TIME + " VARCHAR(50), " +
                T1_PREPARATION + " TEXT, " +
                T1_SERVINGS + " INTEGER, " +
                T1_IMAGE_FILE + " BLOB);");

        //----TABLE 2 QUERY: INGREDIENTS TABLE----//
        db.execSQL("CREATE TABLE " + TABLE_2_NAME + "(" + T2_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                T2_NAME + " VARCHAR(50), " +
                T2_CATEGORY + " VARCHAR(50), " +
                T2_IN_STOCK + " INTEGER, " +
                T2_NEEDED + " INTEGER);");

        //----TABLE 3 QUERY: RECIPE/INGREDIENT LINK TABLE----//
        db.execSQL("CREATE TABLE " + TABLE_3_NAME + "(" + T3_FK_INGREDIENT + " INTEGER, " +
                T3_FK_RECIPE + " INTEGER, " +
                T3_QUANTITY + " INTEGER);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_1_NAME);
        onCreate(db);
    }
}
