package com.android.lopez.cookbook.Dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.lopez.cookbook.recipes.NewRecipeActivity;

/**
 * Created by JacoboAdrian on 2/12/2017.
 */

public class TimeDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private int DEFAULT_HOUR = 0,
            DEFAULT_MINUTE = 0;
    private NewRecipeActivity currentRecipe;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TimePickerDialog(getActivity(), this, DEFAULT_HOUR, DEFAULT_MINUTE, true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        int timeInMinutes = 0;
        Toast.makeText(getContext(),
                "Hours: " + hourOfDay + " Minutes: " + minute + " Time will be set in minutes to recipe"
                , Toast.LENGTH_LONG).show();
        timeInMinutes = (hourOfDay * 60) + minute;
        currentRecipe.setPreparationTime(timeInMinutes);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        currentRecipe = (NewRecipeActivity) getActivity();
    }

}
