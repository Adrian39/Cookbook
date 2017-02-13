package com.android.lopez.cookbook.Filters;

import android.widget.Filter;

import com.android.lopez.cookbook.RecyclerViewAdapters.SearchIngredientAdapter;
import com.android.lopez.cookbook.SQLiteDatabase.IngredientObject;

import java.util.ArrayList;

/**
 * Created by JacoboAdrian on 11/5/2016.
 */

public class IngredientFilter extends Filter {

    SearchIngredientAdapter mAdapter;
    ArrayList<IngredientObject> mIngredientList;

    public IngredientFilter(ArrayList<IngredientObject> ingredientList, SearchIngredientAdapter adapter) {
        this.mAdapter = adapter;
        this.mIngredientList = ingredientList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        //CHECK CONSTRAINTS
        if (constraint != null && constraint.length() > 0) {
            constraint = constraint.toString().toLowerCase();
            ArrayList<IngredientObject> filteredIngredients = new ArrayList<>();

            for (int i = 0; i < mIngredientList.size(); i++) {
                if (mIngredientList.get(i).getMyName().toLowerCase().contains(constraint)) {
                    filteredIngredients.add(mIngredientList.get(i));
                }
            }
            results.count = filteredIngredients.size();
            results.values = filteredIngredients;
        } else {
            results.count = mIngredientList.size();
            results.values = mIngredientList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        mAdapter.SetValues((ArrayList<IngredientObject>) results.values);

        //REFRESH
        mAdapter.notifyDataSetChanged();
    }
}
