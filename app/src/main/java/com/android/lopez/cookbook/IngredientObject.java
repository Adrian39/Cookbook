package com.android.lopez.cookbook;

/**
 * Created by JacoboAdrian on 4/17/2016.
 */
public class IngredientObject {
    private String myID;
    private String myName;
    private String Category;
    private int myStock, myNeeded;

    public String getMyID() {
        return myID;
    }

    public void setMyID(String myID) {
        this.myID = myID;
    }

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public int getMyStock() {
        return myStock;
    }

    public void setMyStock(int myStock) {
        this.myStock = myStock;
    }

    public int getMyNeeded() {
        return myNeeded;
    }

    public void setMyNeeded(int myNeeded) {
        this.myNeeded = myNeeded;
    }

}
