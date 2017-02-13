package com.android.lopez.cookbook.recipes;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.lopez.cookbook.Dialogs.IngredientDialog;
import com.android.lopez.cookbook.Dialogs.TimeDialog;
import com.android.lopez.cookbook.R;
import com.android.lopez.cookbook.RecyclerViewAdapters.IngredientEditorAdapter;
import com.android.lopez.cookbook.SQLiteDatabase.IngredientObject;

import java.util.ArrayList;

public class NewRecipeActivity extends AppCompatActivity {
    Button btnAddIngredient;
    TextView txtIngredientTitle;
    private ArrayList<IngredientObject> ingredientList = new ArrayList<IngredientObject>();
    private RecyclerView ingredientRecyclerView;
    private RecyclerView.LayoutManager myLayoutManager;
    private IngredientEditorAdapter myAdapter;
    private Context context;
    private Button btnSetTime;
    private TextView txtDisplayTime;
    private int preparationTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);
        context = getApplicationContext();

        txtDisplayTime = (TextView) findViewById(R.id.txtPreparationTime);

        //INITIALIZE TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //INITIALIZE FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ADD BUTTON AND SET LISTENER
        btnAddIngredient = (Button) findViewById(R.id.btnAddIngredient);
        btnAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                IngredientDialog dialog = new IngredientDialog();
                //Shows as a small dialog
                dialog.show(fragmentManager, "IngredientDialog");

                //Show as (almost) full screen fragment
                /*FragmentTransaction transaction = fragmentManager.beginTransaction();
                dialog.show(transaction, "Dialog");*/

                //Show as fullscreen fragment (as per developer.android.com)
                /*FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(transaction.TRANSIT_FRAGMENT_OPEN);
                transaction.add(android.R.id.content, dialog).addToBackStack(null).commit();*/
            }
        });

        //SET INGREDIENT RECYCLER VIEW
        ingredientRecyclerView = (RecyclerView) findViewById(R.id.ingredientsInNewRecipe);
        ingredientRecyclerView.setVisibility(View.GONE);
        myLayoutManager = new LinearLayoutManager(context);
        ingredientRecyclerView.setLayoutManager(myLayoutManager);
        myAdapter = new IngredientEditorAdapter(ingredientList, context);
        ingredientRecyclerView.setAdapter(myAdapter);

        //SET ON CLICK LISTENER TO INGREDIENT TITLE TO DISPLAY INGREDIENT LIST
        txtIngredientTitle = (TextView) findViewById(R.id.txtIngredients);
        txtIngredientTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EXPAND INGREDIENT LIST
                ingredientRecyclerView.setVisibility(ingredientRecyclerView.isShown()
                        ? View.GONE
                        : View.VISIBLE);

                //TEST MESSAGE TO SHOW ALL INGREDIENTS ON OBJECT
                for (int i = 0; i < ingredientList.size(); i++) {
                    Toast.makeText(context, ingredientList.get(i).getMyName(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //SET LISTENER FOR SET_TIME BUTTON
        btnSetTime = (Button) findViewById(R.id.btnSetTime);
        btnSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment setTimeFragment = new TimeDialog();
                setTimeFragment.show(getFragmentManager(), "TimePicker");
            }
        });

    }

    public ArrayList<IngredientObject> getIngredientList() {
        return ingredientList;
    }

    public void setIngredient(IngredientObject newIngredient) {
        //ADD INGREDIENTS TO CURRENT RECIPE OBJECT
        ingredientList.add(newIngredient);
        //TEST MESSAGE THAT INGREDIENT HAS BEEN ADDED TO LOCAL LIST
        Toast.makeText(getApplicationContext(), newIngredient.getMyName()
                        + " has been added to this recipe. ID: " + newIngredient.getMyID(),
                Toast.LENGTH_LONG).show();

        //NOTIFY ADAPTER THAT THERE ARE NEW INGREDIENTS
        myAdapter.getNewIngredientList(ingredientList);
        myAdapter.notifyDataSetChanged();
    }

    public void setPreparationTime(int timeInMinutes){
        preparationTime = timeInMinutes;
        txtDisplayTime.setText(getString(R.string.display_preparation_time)
                + " " + timeInMinutes
                + " " + getString(R.string.time_minutes_abbreviation));
    }
}
