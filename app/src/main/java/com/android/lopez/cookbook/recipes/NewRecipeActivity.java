package com.android.lopez.cookbook.recipes;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.lopez.cookbook.Dialogs.IngredientDialog;
import com.android.lopez.cookbook.Dialogs.TimeDialog;
import com.android.lopez.cookbook.R;
import com.android.lopez.cookbook.RecyclerViewAdapters.IngredientEditorAdapter;
import com.android.lopez.cookbook.SQLiteDatabase.DBAdapter;
import com.android.lopez.cookbook.SQLiteDatabase.IngredientObject;

import org.w3c.dom.Text;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import static android.content.res.ColorStateList.valueOf;

public class NewRecipeActivity extends AppCompatActivity {
    //DECLARE VARIABLES
    private Button btnAddIngredient, btnSetTime, btnSetServings;
    private TextView txtIngredientTitle;
    private TextView txtDisplayTime;
    private TextView txtDisplayServings;
    private TextView txtRecipeName;
    private TextView txtPreparation;
    private ImageView icoCamera;
    private ImageView icoGallery;
    private ImageView imgRecipePhoto;
    private ArrayList<IngredientObject> ingredientList = new ArrayList<IngredientObject>();
    private RecyclerView ingredientRecyclerView;
    private RecyclerView.LayoutManager myLayoutManager;
    private IngredientEditorAdapter myAdapter;
    private Context context;
    private String recipeName = "";
    private String recipePreparation = "";
    private String imageURI;
    private int preparationTime = 0;
    private int servingsNumber = 0;
    private static final int RESULT_LOAD_IMAGE = 1;
    private long recipeID;
    private SeekBar servingsSeekBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);
        context = getApplicationContext();

        //INITIALIZE TEXT VIEWS
        txtDisplayTime = (TextView) findViewById(R.id.txtPreparationTime);
        txtDisplayServings = (TextView) findViewById(R.id.txtNumberOfServings);
        txtRecipeName = (TextView) findViewById(R.id.txtRecipeName);
        txtPreparation = (TextView) findViewById(R.id.txtRecipePreparation);

        //INITIALIZE IMAGE VIEW
        imgRecipePhoto = (ImageView) findViewById(R.id.imgRecipePhoto);

        //INITIALIZE SEEK BAR
        servingsSeekBar = (SeekBar) findViewById(R.id.seekBarServings);
        servingsSeekBar.setVisibility(View.GONE);

        //INITIALIZE TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //INITIALIZE FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recipeName = txtRecipeName.getText().toString();
                recipePreparation = txtPreparation.getText().toString();
                DBAdapter dbAdapter = new DBAdapter(context);

                //ADD RECIPE TO RECIPE DATABASE
                if (imageURI == "" || imageURI == null){
                    recipeID = dbAdapter.insertRecipeData(recipeName, preparationTime,
                            recipePreparation, servingsNumber);
                }
                else{
                    recipeID = dbAdapter.insertRecipeData(recipeName, preparationTime,
                            recipePreparation, servingsNumber, imageURI);
                    Toast.makeText(context, "Added URI: " + imageURI, Toast.LENGTH_SHORT).show();
                }

                //ADD INGREDIENTS AND RECIPE TO RELATIONAL DATABASE TABLE
                IngredientObject currentIngredient;
                for (int i = 0; i < ingredientList.size(); i++) {
                    currentIngredient = ingredientList.get(i);
                    dbAdapter.insertRecIngData(currentIngredient.getMyID(), recipeID);
                }

                Snackbar.make(view, "Recipe has been added to the database", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                NewRecipeActivity.this.finish();
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

        //SET LISTENER FOR SET_SERVINGS BUTTON
        btnSetServings = (Button) findViewById(R.id.btnSetServings);
        btnSetServings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                servingsSeekBar.setVisibility(servingsSeekBar.isShown()
                        ? View.GONE
                        : View.VISIBLE);
            }
        });

        //SET LISTENER FOR SERVINGS SEEK BAR
        servingsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                servingsNumber = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                txtDisplayServings.setText(getText(R.string.display_servings) + " " + servingsNumber);
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

        //SET LISTENER FOR CAMERA BUTTON
        icoCamera = (ImageView) findViewById(R.id.icoCamera);
        icoCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CAMERA ACTIVITY

            }
        });


        //SET LISTENER FOR GALLERY PHOTO
        icoGallery = (ImageView) findViewById(R.id.icoGallery);
        icoGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        //THIS WORKS
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data){

            Uri selectedImage = data.getData();

            imageURI = selectedImage.toString();
            //imgRecipePhoto.setImageURI(Uri.parse(imageURI));
            imgRecipePhoto.setImageURI(selectedImage);
            imgRecipePhoto.setScaleType(ImageView.ScaleType.FIT_XY);


        }

        /*if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null !=data){
            Uri selectedImageUri = data.getData();
            String[] projection = {MediaStore.Images.Media.DATA};
            @SuppressWarnings("deprecation")
            Cursor cursor = getContentResolver().query(selectedImageUri, projection, null, null, null);
            cursor.moveToFirst();

            int column_index = cursor.getColumnIndex(projection[0]);
            imagePath = cursor.getString(column_index);
            cursor.close();

            Toast.makeText(context, imagePath, Toast.LENGTH_SHORT).show();
            //imgRecipePhoto.setImageBitmap(BitmapFactory.decodeFile(imagePath));

            imageFile = new File(imagePath);

            if (imageFile.exists()){

                Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                imgRecipePhoto.setImageBitmap(imageBitmap);
                //
            }
            else{
                Toast.makeText(context, "Image could not be loaded", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(context, "You have not selected and image", Toast.LENGTH_SHORT).show();
        }*/
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

    //SET PREPARATION TIME TO MINUTES AND SAVES IT TO NEW RECIPE OBJECT
    public void setPreparationTime(int timeInMinutes) {
        preparationTime = timeInMinutes;
        txtDisplayTime.setText(getString(R.string.display_preparation_time)
                + " " + timeInMinutes
                + " " + getString(R.string.time_minutes_abbreviation));
    }
}
