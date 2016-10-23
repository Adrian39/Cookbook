package com.android.lopez.cookbook.Dialogs;

import android.app.DialogFragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.lopez.cookbook.R;
import com.android.lopez.cookbook.RecyclerViewAdapters.SearchIngredientAdapter;
import com.android.lopez.cookbook.SQLiteDatabase.DBAdapter;
import com.android.lopez.cookbook.SQLiteDatabase.IngredientObject;

import java.util.List;

/**
 * Created by JacoboAdrian on 10/22/2016.
 */

public class IngredientDialog extends DialogFragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    SearchView searchView;
    List<IngredientObject> ingredientList;
    Context context = getContext();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.ingredient_dialog, null);

        searchView = (SearchView) view.findViewById(R.id.srchIngredients);

        recyclerView = (RecyclerView) view.findViewById(R.id.recIngredientSearch);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SearchIngredientAdapter(ingredientList);
        recyclerView.setAdapter(mAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getIngredients(newText);
                return false;
            }
        });

        return view;
    }

    private void getIngredients(String searchTerms){
        ingredientList.clear();
        DBAdapter dbAdapter = new DBAdapter(context);
        dbAdapter.openDB();
        IngredientObject ingredient;
        Cursor cursor = dbAdapter.searchIngredients(searchTerms);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            ingredient = new IngredientObject();
            ingredient.setMyID(id);
            ingredient.setMyName(name);
            ingredientList.add(ingredient);
        }
        dbAdapter.closeDB();
    }

}
