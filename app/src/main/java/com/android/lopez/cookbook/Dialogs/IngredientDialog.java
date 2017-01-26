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

import java.util.ArrayList;

/**
 * Created by JacoboAdrian on 10/22/2016.
 */

public class IngredientDialog extends DialogFragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    SearchIngredientAdapter mAdapter;
    SearchView searchView;
    private static ArrayList<IngredientObject> ingredientList = new ArrayList<IngredientObject>();
    Context context;
    Button btnAddIngredient;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.ingredient_dialog, container, false);

        //SET BUTTON ONCLICK AND VISIBILITY TO GONE
        btnAddIngredient = (Button) view.findViewById(R.id.btnAddIngredient);
        btnAddIngredient.setVisibility(View.GONE);

        //DECLARE TOOLBAR//
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_add_ingredient));

        //DECLARE SEARCH VIEW//
        searchView = (SearchView) view.findViewById(R.id.searchIngredients);
        searchView.setIconified(false);

        //DECLARE RECYCLER VIEW
        recyclerView = (RecyclerView) view.findViewById(R.id.recIngredientSearch);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        setIngredientData();
        mAdapter = new SearchIngredientAdapter(ingredientList, context);
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

        try {
            Toast.makeText(context, "Successfully added " + ingredient + " to Ingredient Database", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
