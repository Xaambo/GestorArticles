package com.example.gestorarticles;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class stockManagerGestorArticles extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_stockmanager);

        Bundle extras = getIntent().getExtras();

        String opcio = extras.getString("opcio");

        final Spinner spinner = findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.opcions_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        int spinnerPosition = adapter.getPosition(opcio);
        spinner.setSelection(spinnerPosition);

        final EditText edtDatePicker = findViewById(R.id.edtDatePicker);
        edtDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(edtDatePicker);
            }
        });

        TextView tvOk = findViewById(R.id.tvOk);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nouMoviment(v, spinner, edtDatePicker);
            }
        });
    }

    private void nouMoviment(View v, Spinner spinner, EditText edtDatePicker) {

        if (spinner.getSelectedItemPosition() == 1){};
    }

    private void showDatePickerDialog(final EditText edtDatePicker) {
        DatePickerFragment picker = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = day + " / " + (month+1) + " / " + year;
                edtDatePicker.setText(selectedDate);
            }
        });

        picker.show(getSupportFragmentManager(), "datePicker");
    }
}