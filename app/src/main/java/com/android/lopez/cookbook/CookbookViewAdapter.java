package com.android.lopez.cookbook;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Adrian Lopez on 5/1/2016.
 */
public class CookbookViewAdapter extends RecyclerView.Adapter<CookbookViewAdapter.MyViewHolder>{

    private Context mContext;
    private List<RecipeObject> mRecipeList;
    private static LayoutInflater mInflater;

    public CookbookViewAdapter(Context context, List<RecipeObject> recipeList){

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mRecipeList = recipeList;

    }

    public void udateRecipeList(List<RecipeObject> updatedRecipeList){
        mRecipeList = updatedRecipeList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public MyViewHolder(View itemView){
            super(itemView);

        }

        @Override
        public void onClick(View v) {

        }
    }
}
