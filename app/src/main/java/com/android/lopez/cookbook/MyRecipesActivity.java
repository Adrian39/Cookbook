package com.android.lopez.cookbook;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.lopez.cookbook.SQLiteDatabase.DBAdapter;
import com.android.lopez.cookbook.SQLiteDatabase.RecipeObject;
import com.android.lopez.cookbook.recipes.NewRecipeActivity;

import java.util.ArrayList;

public class MyRecipesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static RecyclerView mRecyclerView;
    private static SwipeRefreshLayout mRefreshLayout;
    private static CookbookViewAdapter mAdapter;
    private static ArrayList<RecipeObject> mRecipeList = new ArrayList<RecipeObject>();
    private static Context mContext;
    private static Spinner mCategorySpinner;

    public static void setRecipeData() {
        mRecipeList.clear();
        DBAdapter dbAdapter = new DBAdapter(mContext);
        RecipeObject newRecipe;
        Cursor cursor = dbAdapter.getAllRecipes();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int time = cursor.getInt(3);
            int servings = cursor.getInt(5);

            //ADD IMAGE FROM DB ONCE YOU FIGURE OUT HOW TO DO IT

            newRecipe = new RecipeObject();
            newRecipe.setMyID(id);
            newRecipe.setMyName(name);
            newRecipe.setMyTime(time);
            newRecipe.setMyServings(servings);
            mRecipeList.add(newRecipe);
        }
    }

    public static ArrayList<RecipeObject> getRecipeData() {
        return mRecipeList;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes);
        mContext = getApplicationContext();

        //----SQL HELPER----//

        //----TOOLBAR----//
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //--REFRESH LAYOUT--//
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshRecipes);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
        mRefreshLayout.setOnRefreshListener(this);

        //----RECYCLER VIEW----//
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerRecipes);
        setRecipeData();
        mAdapter = new CookbookViewAdapter(mContext, getRecipeData());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //----FAB----//
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFABClick();
            }
        });

        //----NAVIGATION DRAWER----//
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_recipes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onRefresh() {
        Toast.makeText(mContext, "You refreshed the view!", Toast.LENGTH_LONG).show();
    }

    public void onFABClick() {
        //DBAdapter adapter = new DBAdapter(mContext);
        //adapter.openDB();
        Intent intent = new Intent(this, NewRecipeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRestart(){
        super.onRestart();
        setRecipeData();
        mAdapter.udateRecipeList(mRecipeList);
        mAdapter.notifyDataSetChanged();
    }

}
