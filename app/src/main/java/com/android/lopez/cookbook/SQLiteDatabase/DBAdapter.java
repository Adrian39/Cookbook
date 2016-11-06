package com.android.lopez.cookbook.SQLiteDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Message;
import android.util.StringBuilderPrinter;
import android.widget.Toast;

import java.util.List;

/**
 * Created by JacoboAdrian on 5/4/2016.
 */
public class DBAdapter {

    DBHelper dbHelper;
    private Context context;

    public void openDB(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(db.isOpen()){
            Toast.makeText(context, "Successfully opened database", Toast.LENGTH_LONG).show();
        }
    }

    public DBAdapter(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
    }

    public boolean insertRecipeData(String name, String category, int time, String preparation, int servings) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.T1_NAME, name);
            contentValues.put(DBHelper.T1_CATEGORY, category);
            contentValues.put(DBHelper.T1_TIME, time);
            contentValues.put(DBHelper.T1_PREPARATION, preparation);
            contentValues.put(DBHelper.T1_SERVINGS, servings);
            db.insert(DBHelper.TABLE_1_NAME, null, contentValues);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean insertIngredientData(String name, String category) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.T2_NAME, name);
            contentValues.put(DBHelper.T2_CATEGORY, category);
            db.insert(DBHelper.TABLE_2_NAME, null, contentValues);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean insertRecIngData(int ingredientID, int recipeID, int amountNeeded) {

        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.T3_FK_INGREDIENT, ingredientID);
            contentValues.put(DBHelper.T3_FK_RECIPE, recipeID);
            contentValues.put(DBHelper.T3_QUANTITY, amountNeeded);
            db.insert(DBHelper.TABLE_3_NAME, null, contentValues);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //SEARCH AND FILTERS
    public Cursor searchIngredients(String searchTerm) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor;
        String[] columns = {DBHelper.T2_KEY_ID, DBHelper.T2_NAME};
        if (searchTerm != null && searchTerm.length() > 0) {
            String query = "SELECT * FROM " + DBHelper.TABLE_2_NAME + " WHERE " + DBHelper.T2_NAME + " LIKE '%" + searchTerm + "%'";
            cursor = db.rawQuery(query, null);
            return cursor;
        }

        cursor = db.query(DBHelper.TABLE_2_NAME, columns, null, null, null, null, null);
        return cursor;
    }

    public Cursor getAllIngredients() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor;
        String[] columns = {DBHelper.T2_KEY_ID, DBHelper.T2_NAME};

        String query = "SELECT * FROM " + DBHelper.TABLE_2_NAME;
        cursor = db.rawQuery(query, null);
        return cursor;
    }

    static class DBHelper extends SQLiteOpenHelper {

        private static final String DB_NAME = "cookbook";     //Database name
        private static final int SCHEME_VERSION = 7;

        private static final String TABLE_1_NAME = "Recipes", T1_KEY_ID = "_id", T1_NAME = "Name",
                T1_CATEGORY = "Category", T1_TIME = "Time", T1_PREPARATION = "Preparation",
                T1_SERVINGS = "Servings", T1_IMAGE_FILE = "ImageFile";      //Table 1 = Recipe Database

        private static final String TABLE_2_NAME = "Ingredients", T2_KEY_ID = "_id", T2_NAME = "Name",
                T2_CATEGORY = "Category", T2_IN_STOCK = "InStock", T2_NEEDED = "Needed";                    //Table 2 = Ingredient Database

        private static final String TABLE_3_NAME = "RecipeIngredient", T3_FK_RECIPE = "RecipeID",
                T3_FK_INGREDIENT = "IngredientID", T3_QUANTITY = "Quantity";                                //Table 3 = RecipeIngredientLink Database

        private Context context;


        public DBHelper(Context context) {
            super(context, DB_NAME, null, SCHEME_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //----TABLE 1 QUERY: RECIPES TABLE----//

            try {
                db.execSQL("CREATE TABLE " + TABLE_1_NAME + "(" + T1_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        T1_NAME + " VARCHAR(50), " +
                        T1_CATEGORY + " VARCHAR(50), " +
                        T1_TIME + " INTEGER, " +
                        T1_PREPARATION + " TEXT, " +
                        T1_SERVINGS + " INTEGER, " +
                        T1_IMAGE_FILE + " BLOB);");
                Toast.makeText(context, "Successfully opened database", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                Toast.makeText(context, "An error has occurred when attempting to create the 'Recipes Table'", Toast.LENGTH_SHORT).show();
            }

            //----TABLE 2 QUERY: INGREDIENTS TABLE----//
            try {
                db.execSQL("CREATE TABLE " + TABLE_2_NAME + "(" + T2_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        T2_NAME + " VARCHAR(50), " +
                        T2_CATEGORY + " VARCHAR(50), " +
                        T2_IN_STOCK + " INTEGER, " +
                        T2_NEEDED + " INTEGER);");
                Toast.makeText(context, "Successfully opened database", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                Toast.makeText(context, "An error has occurred when attempting to create the 'Ingredient Table'", Toast.LENGTH_SHORT).show();
            }

            //----TABLE 3 QUERY: RECIPE/INGREDIENT LINK TABLE----//
            try {
                db.execSQL("CREATE TABLE " + TABLE_3_NAME + "(" + T3_FK_INGREDIENT + " INTEGER, " +
                        T3_FK_RECIPE + " INTEGER, " +
                        T3_QUANTITY + " INTEGER);");
                Toast.makeText(context, "Successfully opened database", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                Toast.makeText(context, "An error has occurred when attempting to create the 'Link Table'", Toast.LENGTH_SHORT).show();
            }

            try {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.T1_NAME, "Test");
                contentValues.put(DBHelper.T1_CATEGORY, "Test");
                contentValues.put(DBHelper.T1_TIME, 1);
                contentValues.put(DBHelper.T1_PREPARATION, "Test");
                contentValues.put(DBHelper.T1_SERVINGS, 1);
                db.insert(DBHelper.TABLE_1_NAME, null, contentValues);
                Toast.makeText(context, "Successfully added test recipe", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.T2_NAME, "Alpha");
                contentValues.put(DBHelper.T2_CATEGORY, "Test");
                contentValues.put(DBHelper.T2_IN_STOCK, 1);
                contentValues.put(DBHelper.T2_NEEDED, 1);
                db.insert(DBHelper.TABLE_2_NAME, null, contentValues);
                Toast.makeText(context, "Successfully added test ingredient", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.T2_NAME, "Bravo");
                contentValues.put(DBHelper.T2_CATEGORY, "Test2");
                contentValues.put(DBHelper.T2_IN_STOCK, 1);
                contentValues.put(DBHelper.T2_NEEDED, 1);
                db.insert(DBHelper.TABLE_2_NAME, null, contentValues);
                Toast.makeText(context, "Successfully second test ingredient", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.T2_NAME, "Charlie");
                contentValues.put(DBHelper.T2_CATEGORY, "Test3");
                contentValues.put(DBHelper.T2_IN_STOCK, 1);
                contentValues.put(DBHelper.T2_NEEDED, 1);
                db.insert(DBHelper.TABLE_2_NAME, null, contentValues);
                Toast.makeText(context, "Successfully third test ingredient", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.T2_NAME, "Echo");
                contentValues.put(DBHelper.T2_CATEGORY, "Test4");
                contentValues.put(DBHelper.T2_IN_STOCK, 1);
                contentValues.put(DBHelper.T2_NEEDED, 1);
                db.insert(DBHelper.TABLE_2_NAME, null, contentValues);
                Toast.makeText(context, "Successfully fourth test ingredient", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_1_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_2_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_3_NAME);
                onCreate(db);
            } catch (SQLException e) {
                Toast.makeText(context, "An error has occurred when attempting to delete the Database", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
