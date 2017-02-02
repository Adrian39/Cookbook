package com.android.lopez.cookbook.Dialogs;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowId;
import android.widget.Button;
import android.widget.Toast;

import com.android.lopez.cookbook.Filters.IngredientFilter;
import com.android.lopez.cookbook.R;
import com.android.lopez.cookbook.RecyclerViewAdapters.SearchIngredientAdapter;
import com.android.lopez.cookbook.SQLiteDatabase.DBAdapter;
import com.android.lopez.cookbook.SQLiteDatabase.IngredientObject;
import com.android.lopez.cookbook.recipes.NewRecipeActivity;

import java.util.ArrayList;

/**
 * Created by JacoboAdrian on 10/22/2016.
 */

public class IngredientDialog extends DialogFragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    Context context;
    Button btnAddIngredient;
    SearchIngredientAdapter mAdapter;
    SearchView searchView;
    Toolbar toolbar;
    private static ArrayList<IngredientObject> ingredientList = new ArrayList<IngredientObject>();
    private NewRecipeActivity currentRecipe = (NewRecipeActivity) getActivity();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.ingredient_dialog, container, false);

        //INITIATE UI VARIABLES
        btnAddIngredient = (Button) view.findViewById(R.id.btnAddIngredient);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        searchView = (SearchView) view.findViewById(R.id.searchIngredients);
        recyclerView = (RecyclerView) view.findViewById(R.id.recIngredientSearch);

        //SET BUTTON ONCLICK AND VISIBILITY TO GONE
        btnAddIngredient.setVisibility(View.GONE);
        btnAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addIngredientToDataBase(searchView.getQuery().toString());
            }
        });

        //SET TOOLBAR'S TITLE
        toolbar.setTitle(getString(R.string.title_add_ingredient));

        //SET SEARCH VIEW'S ICON TO FALSE
        searchView.setIconified(false);

        //SET RECYCLER VIEW'S ADAPTER AND LAYOUT
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        setIngredientData();
        mAdapter = new SearchIngredientAdapter(ingredientList, context, currentRecipe);
        recyclerView.setAdapter(mAdapter);

        //ON QUERY LISTENER - LISTENER FOR THE SEARCH BAR
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mAdapter.getFilter().filter(query);
                if (mAdapter.getItemCount() < 1){
                    recyclerView.setVisibility(View.GONE);
                    btnAddIngredient.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    btnAddIngredient.setVisibility(View.GONE);
                }
                return true;
            }
        });

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        context = activity;
    }

    //Function clears ingredient list and re-populates it with updated ingredient list.
    public void setIngredientData(){
        ingredientList.clear();
        DBAdapter dbAdapter = new DBAdapter(context);
        IngredientObject ingredient;
        Cursor cursor = dbAdapter.getAllIngredients();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            ingredient = new IngredientObject();
            ingredient.setMyID(id);
            ingredient.setMyName(name);
            ingredientList.add(ingredient);
        }
    }

    void addIngredientToDataBase(String ingredient){

        //Add ingredient to database
        long newIngredientID;
        DBAdapter dbAdapter = new DBAdapter(context);
        newIngredientID = dbAdapter.insertIngredientData(ingredient);

        //Not sure if it is necessary to clear list
        ingredientList.clear();

        //Add ingredient to recipe list
        IngredientObject newIngredient = new IngredientObject();
        newIngredient.setMyName(ingredient);
        newIngredient.setMyID(newIngredientID);
        currentRecipe.setIngredient(newIngredient);

        //Close dialog after clicking button
        IngredientDialog.this.dismiss();
    }

}
