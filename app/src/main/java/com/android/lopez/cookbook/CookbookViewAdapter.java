package com.android.lopez.cookbook;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.lopez.cookbook.SQLiteDatabase.RecipeObject;

import java.util.List;

/**
 * Created by Adrian Lopez on 5/1/2016.
 */
public class CookbookViewAdapter extends RecyclerView.Adapter<CookbookViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<RecipeObject> mRecipeList;
    private static LayoutInflater mInflater;

    public CookbookViewAdapter(Context context, List<RecipeObject> recipeList) {

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mRecipeList = recipeList;

    }

    public void udateRecipeList(List<RecipeObject> updatedRecipeList) {
        mRecipeList = updatedRecipeList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_row_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.txtRecipeName.setText(mRecipeList.get(position).getMyName());
        holder.txtServingSize.setText("" + mRecipeList.get(position).getMyServings() + " Servings");
        holder.txtTime.setText("" + mRecipeList.get(position).getMyTime() + " Minutes");
    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtRecipeName, txtServingSize, txtTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtRecipeName = (TextView) itemView.findViewById(R.id.txtRecipeName);
            txtServingSize = (TextView) itemView.findViewById(R.id.txtServings);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
        }

        @Override
        public void onClick(View v) {

        }
    }
}