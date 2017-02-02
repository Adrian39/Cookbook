package com.android.lopez.cookbook.RecyclerViewAdapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.android.lopez.cookbook.Filters.IngredientFilter;
import com.android.lopez.cookbook.R;
import com.android.lopez.cookbook.SQLiteDatabase.IngredientObject;
import com.android.lopez.cookbook.recipes.NewRecipeActivity;

import java.util.ArrayList;


/**
 * Modyfied by JacoboAdrian on 10/22/2016.
 */

public class SearchIngredientAdapter extends RecyclerView.Adapter<SearchIngredientAdapter.MyViewHolder> implements Filterable{

    private ArrayList<IngredientObject> ingredientObjectList = new ArrayList<>();
    private ArrayList<IngredientObject> ingredientFilteredList = new ArrayList<>();
    private IngredientFilter filter;
    private Context mContext;
    private NewRecipeActivity parentRecipeActivity;


    public SearchIngredientAdapter(ArrayList<IngredientObject> ingredients, Context context, NewRecipeActivity recipeActivity){
        this.ingredientObjectList = ingredients;
        this.ingredientFilteredList = ingredients;
        this.mContext = context;
        parentRecipeActivity = recipeActivity;
    }

    public void SetValues(ArrayList<IngredientObject> newIngredientValues){
        this.ingredientObjectList = newIngredientValues;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ingredient, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.txtIngredientName.setText(ingredientObjectList.get(position).getMyName());
        holder.selectedIngredient = ingredientObjectList.get(position);

    }

    @Override
    public int getItemCount() {
        return ingredientObjectList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new IngredientFilter(ingredientFilteredList,this);
        }
        return filter;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView txtIngredientName;
        public IngredientObject selectedIngredient;
        public MyViewHolder(View view){
            super(view);
            txtIngredientName = (TextView) itemView.findViewById(R.id.txtRowIngredientName);
            txtIngredientName.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            parentRecipeActivity.setIngredient(selectedIngredient);
            Toast.makeText(mContext, "Added " + txtIngredientName.getText()+ " to the recipe", Toast.LENGTH_LONG).show();
        }
    }
    
}
