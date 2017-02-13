package com.android.lopez.cookbook.RecyclerViewAdapters;

import android.content.ClipData;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.lopez.cookbook.R;
import com.android.lopez.cookbook.SQLiteDatabase.IngredientObject;

import java.util.ArrayList;

/**
 * Created by JacoboAdrian on 2/5/2017.
 */

public class IngredientEditorAdapter
        extends RecyclerView.Adapter<IngredientEditorAdapter.MyViewHolder> {

    private ArrayList<IngredientObject> mIngredientList = new ArrayList<IngredientObject>();
    private Context mContext;

    public IngredientEditorAdapter(ArrayList<IngredientObject> ingredientList, Context context) {
        this.mIngredientList = ingredientList;
        this.mContext = context;
    }

    @Override
    public IngredientEditorAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_edit_ingredients,
                parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientEditorAdapter.MyViewHolder holder, int position) {
        holder.txtIngredientName.setText(mIngredientList.get(position).getMyName());
    }

    @Override
    public int getItemCount() {
        return mIngredientList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtIngredientName;
        public ImageView imgDeleteIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtIngredientName = (TextView) itemView.findViewById(R.id.txtIngredientName);
            imgDeleteIcon = (ImageView) itemView.findViewById(R.id.icoDelete);
            imgDeleteIcon.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            deleteItem(getPosition());
        }
    }

    public void getNewIngredientList(ArrayList<IngredientObject> newIngredientList) {
        mIngredientList = newIngredientList;
    }

    public void deleteItem(int position) {
        mIngredientList.remove(position);
        notifyItemRemoved(position);
    }
}
