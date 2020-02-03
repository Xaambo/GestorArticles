package com.example.gestorarticles;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class stockManagerGestorArticles extends AppCompatActivity {

    private GestorArticlesDataSource bd;
    private Cursor datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_stockmanager);

        bd = new GestorArticlesDataSource(this);

        Bundle extras = getIntent().getExtras();

        String opcio = extras.getString("opcio");
        final int id = extras.getInt("_id");

        datos = bd.article(id);

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

        final EditText edtModStock = findViewById(R.id.edtModStock);

        TextView tvOk = findViewById(R.id.tvOk);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nouMoviment(spinner, edtModStock, edtDatePicker);
            }
        });

        TextView tvCancel = findViewById(R.id.tvCancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent();
                i.putExtra("id", id);
                setResult(RESULT_CANCELED, i);

                finish();
            }
        });
    }

    private void nouMoviment(Spinner spinner, EditText edtModStock, EditText edtDatePicker) {

        datos.moveToFirst();
        String codiArticle = datos.getString(datos.getColumnIndexOrThrow(GestorArticlesDataSource.GESTORARTICLES_CODIARTICLE));
        int modStock = Integer.parseInt(edtModStock.getText().toString());
        int newStock;
        String tipus;
        String data = edtDatePicker.getText().toString();

        if (spinner.getSelectedItemPosition() == 0){

            newStock = datos.getInt(datos.getColumnIndexOrThrow(GestorArticlesDataSource.GESTORARTICLES_STOCK)) + modStock;
            tipus = "E";

        } else {
            newStock = datos.getInt(datos.getColumnIndexOrThrow(GestorArticlesDataSource.GESTORARTICLES_STOCK)) - modStock;
            tipus = "S";
        }

        Intent i = new Intent();

        if (accioBDD(codiArticle, data, modStock, newStock, tipus) > 0) {

            setResult(RESULT_OK, i);
            finish();

        } else {
            setResult(RESULT_CANCELED, i);
            finish();
        }

    }

    private int accioBDD(String codiArticle, String data, int modStock, int newStock, String tipus) {

        bd.insertMoviment(codiArticle, data, modStock, tipus);
        return bd.updateStock(datos.getInt(datos.getColumnIndexOrThrow(GestorArticlesDataSource.GESTORARTICLES_ID)), newStock);
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
}