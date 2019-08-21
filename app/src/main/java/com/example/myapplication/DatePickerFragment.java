package com.example.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.app.DatePickerDialog;;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), R.style.DatePickerDialogTheme, this, year, month, day);
    }

    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        onFragmentDateListener.onDateSateChoisen(year,month, day);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public interface onFragmentDateListener{
        void onDateSateChoisen(int year, int month, int day);
    }

    private onFragmentDateListener onFragmentDateListener;

    //Override this function as below to set fragmentInterfacer
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onFragmentDateListener)
        onFragmentDateListener = (onFragmentDateListener) context;
        else throw new RuntimeException(context.toString() + " must implement onFragmentDateListener");
    }


}
