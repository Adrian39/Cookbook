package com.android.lopez.cookbook.SQLiteDatabase;

import com.android.lopez.cookbook.SQLiteDatabase.IngredientObject;

import java.io.File;
import java.util.List;

/**
 * Created by JacoboAdrian on 4/17/2016.
 */
public class RecipeObject {
    private String myName, myCategory, myTime, myPreparation;
    private long myID;
    private int myServings;
    private File myImageFile;
    private List<Integer> myAmounts;
    private List<IngredientObject> myIngredients;

    public long getMyID() {
        return myID;
    }

    public void setMyID(long myID) {
        this.myID = myID;
    }

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public String getMyCategory() {
        return myCategory;
    }

    public void setMyCategory(String myCategory) {
        this.myCategory = myCategory;
    }

    public String getMyTime() {
        return myTime;
    }

    public void setMyTime(String myTime) {
        this.myTime = myTime;
    }

    public String getMyPreparation() {
        return myPreparation;
    }

    public void setMyPreparation(String myPreparation) {
        this.myPreparation = myPreparation;
    }

    public int getMyServings() {
        return myServings;
    }

    public void setMyServings(int myServings) {
        this.myServings = myServings;
    }

    public File getMyImageFile() {
        return myImageFile;
    }

    public void setMyImageFile(File myImageFile) {
        this.myImageFile = myImageFile;
    }

    public List<Integer> getMyAmounts() {
        return myAmounts;
    }

    public void setMyAmounts(List<Integer> myAmounts) {
        this.myAmounts = myAmounts;
    }

    public List<IngredientObject> getMyIngredients() {
        return myIngredients;
    }

    public void setMyIngredients(List<IngredientObject> myIngredients) {
        this.myIngredients = myIngredients;
    }

}
