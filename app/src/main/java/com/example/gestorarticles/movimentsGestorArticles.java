package com.example.gestorarticles;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class movimentsGestorArticles extends AppCompatActivity {

    private GestorArticlesDataSource bd;
    private adapterMovimentsGestorArticles scMoviments;

    private static String[] from = new String[]{GestorArticlesDataSource.GESTORARTICLES_DESCRIPCION, GestorArticlesDataSource.MOVIMENTS_DIA, GestorArticlesDataSource.MOVIMENTS_QUANTITAT, GestorArticlesDataSource.MOVIMENTS_TIPUS};
    private static int[] to = new int[]{R.id.tvDescripcioArticle, R.id.tvData, R.id.tvNumUnitats, R.id.tvTipusMoviment};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_moviments);

        setTitle("Moviments");
        bd = new GestorArticlesDataSource(this);

        Bundle extras = getIntent().getExtras();

        Cursor cursorMoviments;

        cursorMoviments = carregaCursor(extras);

        scMoviments = new adapterMovimentsGestorArticles(this, R.layout.layout_moviment, cursorMoviments, from, to, 1);

        ListView lv = findViewById(R.id.lvMoviments);
        lv.setAdapter(scMoviments);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.layout_menu_moviments, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnSelectDia:
                Bundle extras = new Bundle();
                actualitzarMoviments(extras);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void actualitzarMoviments(final Bundle extras) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Data");
        builder.setMessage("Dia dels moviments?");

        final EditText data = new EditText(movimentsGestorArticles.this);
        data.setFocusable(false);
        data.setClickable(true);
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(data, extras);
            }
        });

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);

        data.setGravity(Gravity.CENTER);

        data.setLayoutParams(lp);
        builder.setView(data);

        builder.setNeutralButton("Veure tot", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                carregaMoviments(extras);
            }
        });

        builder.setPositiveButton("Día", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                showDatePickerDialog(data, extras);
            }
        });

        builder.setNegativeButton("Cancel", null);

        builder.show();
    }

    private void showDatePickerDialog(final EditText edtDatePicker, final Bundle extras) {
        final DatePickerFragment picker = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = day + "/" + (month+1) + "/" + year;
                edtDatePicker.setText(selectedDate);
                extras.putString("data", selectedDate);
                carregaMoviments(extras);
            }
        });

        picker.show(getSupportFragmentManager(), "datePicker");
    }

    private void carregaMoviments(Bundle extras) {

        scMoviments.changeCursor(carregaCursor(extras));
        scMoviments.notifyDataSetChanged();
    }

    private Cursor carregaCursor(Bundle extras) {
        Cursor cursorMoviments;

        if (extras.size() <= 1) {

            String data = extras.getString("data");

            if (data != null) {
                cursorMoviments = bd.movimentsEnData(data);
            } else {
                cursorMoviments = bd.moviments();
            }

        } else {

            String dataFrom = extras.getString("dataFrom");
            String dataTo = extras.getString("dataTo");
            String codiArticle = extras.getString("codiArticle");

            if (dataFrom.equals("") && dataTo.equals("")) {
                cursorMoviments = bd.moviments();
            } else if (dataTo.equals("")) {
                cursorMoviments = bd.movimentsDesDeData(dataFrom, codiArticle);
            } else if (dataFrom.equals("")) {
                cursorMoviments = bd.movimentsFinsAData(dataTo, codiArticle);
            } else {
                cursorMoviments = bd.movimentsEntreDates(dataFrom, dataTo, codiArticle);
            }
        }

        return cursorMoviments;
    }
}
