package com.example.gestorarticles;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class logic extends AppCompatActivity {

    protected void DatePicker(final Intent i, Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Data");
        builder.setMessage("Dia dels moviments?");

        final EditText data = new EditText(context);
        data.setFocusable(false);
        data.setClickable(true);
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(data, i);
            }
        });

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);

        data.setGravity(Gravity.CENTER);

        data.setLayoutParams(lp);
        builder.setView(data);

        builder.setNeutralButton("Veure tot", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                i.putExtra("data", (Parcelable[]) null);
                startActivity(i);
            }
        });

        builder.setPositiveButton("Día", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                showDatePickerDialog(data, i);
            }
        });

        builder.setNegativeButton("Cancel", null);

        builder.show();
    }

    protected void showDatePickerDialog(final EditText edtDatePicker) {
        final DatePickerFragment picker = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = day + "/" + (month+1) + "/" + year;
                edtDatePicker.setText(selectedDate);
            }
        });

        picker.show(getSupportFragmentManager(), "datePicker");
    }

    protected void showDatePickerDialog(final EditText edtDatePicker, final Intent i) {
        DatePickerFragment picker = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = day + "/" + (month+1) + "/" + year;
                edtDatePicker.setText(selectedDate);
                i.putExtra("data", selectedDate);
                startActivity(i);
            }
        });

        picker.show(getSupportFragmentManager(), "datePicker");
    }
}
