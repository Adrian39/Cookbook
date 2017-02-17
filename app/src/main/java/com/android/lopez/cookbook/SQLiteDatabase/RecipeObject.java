package com.android.lopez.cookbook.SQLiteDatabase;

import com.android.lopez.cookbook.SQLiteDatabase.IngredientObject;

import java.io.File;
import java.util.List;

/**
 * Created by JacoboAdrian on 4/17/2016.
 */
public class RecipeObject {
    private String myName;
    private String myCategory;
    private String myPreparation;
    private String myImageString;
    private long myID;
    private int myTime, myServings;
    private List<Integer> myAmounts;
    private List<IngredientObject> myIngredients;
    private byte[] myImageByteArray;

    public byte[] getMyImageByteArray() {
        return myImageByteArray;
    }

    public void setMyImageByteArray(byte[] myImageByteArray) {
        this.myImageByteArray = myImageByteArray;
    }

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

    public String getMyImageString() {
        return myImageString;
    }

    public void setMyImageString(String myImageString) {
        this.myImageString = myImageString;
    }

    public int getMyTime() {
        return myTime;
    }

    public void setMyTime(int myTime) {
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
