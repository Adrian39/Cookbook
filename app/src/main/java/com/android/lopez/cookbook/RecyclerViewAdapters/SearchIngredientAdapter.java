package com.android.lopez.cookbook.RecyclerViewAdapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.lopez.cookbook.R;
import com.android.lopez.cookbook.SQLiteDatabase.IngredientObject;

import java.util.Collections;
import java.util.List;

/**
 * Modyfied by JacoboAdrian on 10/22/2016.
 */

public class SearchIngredientAdapter extends RecyclerView.Adapter<SearchIngredientAdapter.MyViewHolder> {
    List<IngredientObject> ingredientObjectList = Collections.emptyList();

    public SearchIngredientAdapter(List<IngredientObject> ingredients){
        this.ingredientObjectList = ingredients;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ingredient, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.txtIngredientName.setText(ingredientObjectList.get(position).getMyName());

    }

    @Override
    public int getItemCount() {
        return ingredientObjectList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtIngredientName;
        public MyViewHolder(View view){
            super(view);
            txtIngredientName = (TextView) itemView.findViewById(R.id.txtRowIngredientName);
        }
    }
    
}
