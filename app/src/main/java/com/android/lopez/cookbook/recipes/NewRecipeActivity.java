package com.android.lopez.cookbook.recipes;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.lopez.cookbook.Dialogs.IngredientDialog;
import com.android.lopez.cookbook.Dialogs.TimeDialog;
import com.android.lopez.cookbook.MarshmallowPermissions.MarshmallowPermissions;
import com.android.lopez.cookbook.R;
import com.android.lopez.cookbook.RecyclerViewAdapters.IngredientEditorAdapter;
import com.android.lopez.cookbook.SQLiteDatabase.DBAdapter;
import com.android.lopez.cookbook.SQLiteDatabase.IngredientObject;
import com.android.lopez.cookbook.Utility;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

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
    //private String encodeImage64;
    private String imageURI;
    private Bitmap imageBitmap;
    private byte[] imageByteArray;
    private int preparationTime = 0;
    private int servingsNumber = 0;
    private static final int RESULT_LOAD_IMAGE = 1;
    private long recipeID;
    private SeekBar servingsSeekBar;
    private MarshmallowPermissions marshmallowPermissions = new MarshmallowPermissions(this);


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
                if (imageByteArray == null){
                    recipeID = dbAdapter.insertRecipeData(recipeName, preparationTime,
                            recipePreparation, servingsNumber);
                }
                else{
                    //Encoded to byte[]
                    recipeID = dbAdapter.insertRecipeData(recipeName, preparationTime,
                            recipePreparation, servingsNumber, imageByteArray);
                    //Encoded to Base64
                    /*recipeID = dbAdapter.insertRecipeData(recipeName, preparationTime,
                            recipePreparation, servingsNumber, encodeImage64);*/
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
                //ASKING PERMISSIONS ON USE
                if (!marshmallowPermissions.checkPermissionForCamera()){
                    marshmallowPermissions.requestPermissionForCamera();
                }
                else{
                    if (!marshmallowPermissions.checkPermissionForExternalStorage()){
                        marshmallowPermissions.requestPermissionForExternalStorage();
                    }
                    else{
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, RESULT_LOAD_IMAGE);
                    }
                }


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
            imgRecipePhoto.setImageURI(selectedImage);
            imgRecipePhoto.setScaleType(ImageView.ScaleType.FIT_XY);
            Drawable drawable = imgRecipePhoto.getDrawable();
            imageBitmap = ((BitmapDrawable) drawable).getBitmap();
            imageByteArray = Utility.getBytes(imageBitmap);
            //encodeImage64 = Utility.encodeToBase64(imageBitmap);
        }
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
