package com.android.lopez.cookbook.recipes;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import com.android.lopez.cookbook.Dialogs.IngredientDialog;
import com.android.lopez.cookbook.R;
import com.android.lopez.cookbook.SQLiteDatabase.IngredientObject;

import java.util.ArrayList;

public class NewRecipeActivity extends AppCompatActivity {
    Button btnAddIngredient;
    private ArrayList<IngredientObject> ingredientList = new ArrayList<IngredientObject>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnAddIngredient = (Button) findViewById(R.id.btnAddIngredient);
        btnAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                IngredientDialog dialog = new IngredientDialog();
                //Shows as a small dialog
                /*dialog.show(fragmentManager, "IngredientDialog");*/

                //Show as (almost) full screen fragment
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                dialog.show(transaction, "Dialog");

                //Show as fullscreen fragment (as per developer.android.com)
                /*FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(transaction.TRANSIT_FRAGMENT_OPEN);
                transaction.add(android.R.id.content, dialog).addToBackStack(null).commit();*/



            }
        });

    }

    public ArrayList<IngredientObject> getIngredientList() {
        return ingredientList;
    }

    public void setIngredient(IngredientObject newIngredient) {
        ingredientList.add(newIngredient);
    }

}
