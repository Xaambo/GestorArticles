package com.example.gestorarticles;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private static int ARTICLE_ADD = 1;
    private static int ARTICLE_UPDATE = 2;
    private static int ARTICLE_EDIT = 3;

    private GestorArticlesDataSource bd;
    private adapterGestorArticles scArticles;
    private filtreArticles filterActual;

    private AlertDialog alert;

    private static String[] from = new String[]{GestorArticlesDataSource.GESTORARTICLES_CODIARTICLE, GestorArticlesDataSource.GESTORARTICLES_DESCRIPCION, GestorArticlesDataSource.GESTORARTICLES_STOCK};
    private static int[] to = new int[]{R.id.tvCodiArticle, R.id.tvDescripcio, R.id.tvNumUnitats};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Articles");
        bd = new GestorArticlesDataSource(this);

        filterActual = filtreArticles.FILTER_ALL;

        Cursor cursorArticles = bd.articles();

        scArticles = new adapterGestorArticles(this, R.layout.layout_article, cursorArticles, from, to, 1);

        ListView lv = findViewById(R.id.lvArticles);
        lv.setAdapter(scArticles);

        lv.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                // modifiquem el id
                actualitzarArticle(id);
                }
            }
        );
    }

    private void carregaArticles() {

        Cursor cursorArticles = null;

        // Demanem les tasques depenen del filtre que s'estigui aplicant
        switch (filterActual) {
            case FILTER_ALL:
                cursorArticles = bd.articles();
                break;
            case FILTER_STOCK:
                cursorArticles = bd.articlesStock();
                break;
            case FILTER_NOSTOCK:
                cursorArticles = bd.articlesNoStock();
                break;
        }

        // Un cop fet el filtre li diem al cursor que hem canviat les seves dades i que s'actualitzi
        scArticles.changeCursor(cursorArticles);
        scArticles.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.layout_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnAdd:
                crearArticle();
                return true;
            case R.id.btnHistory:
                veureMoviments();
                return true;
            case R.id.btnWeather:
                veureTemps();
                return true;
            case R.id.noFiltre:
                filtreOff();
                return true;
            case R.id.filtreStock:
                filtreStock();
                return true;
            case R.id.filtreNoStock:
                filtreNoStock();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void veureTemps() {

        final Intent i = new Intent(this, detailWeather.class );

        final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setView(findViewById(android.R.id.content))
                .setTitle("Temps")
                .setMessage("De quina ciutat vols veure el temps?")
                .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        final EditText data = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        data.setGravity(Gravity.CENTER);

        data.setLayoutParams(lp);
        dialog.setView(data);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        String ciutat = data.getText().toString();

                        if (!ciutat.isEmpty()) {

                            dialog.dismiss();

                            i.putExtra("ciutat", ciutat);
                            startActivity(i);
                        } else {
                            Dialogs.showInformacion(MainActivity.this, "No has posat cap ciutat");
                        }
                    }
                });
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ARTICLE_ADD) {
            if (resultCode == RESULT_OK) {
                // Carreguem totes les tasques a lo bestia
                carregaArticles();
            }
        }

        if (requestCode == ARTICLE_UPDATE) {
            if (resultCode == RESULT_OK) {
                carregaArticles();
            }
        }

        if (requestCode == ARTICLE_EDIT) {
            if (resultCode == RESULT_OK) {
                carregaArticles();
            }
        }
    }

    private void crearArticle() {
        // S'envia un -1 perque la creacio i la modificació fan servir la mateixa activity i s'ha de diferenciar
        Bundle bundle = new Bundle();
        bundle.putLong("id",-1);

        Intent i = new Intent(this, detallArticle.class );
        i.putExtras(bundle);
        startActivityForResult(i,ARTICLE_ADD);
    }

    private void veureMoviments() {

        final Intent i = new Intent(this, movimentsGestorArticles.class );

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Data");
        builder.setMessage("Dia dels moviments?");

        final EditText data = new EditText(MainActivity.this);
        data.setFocusable(false);
        data.setClickable(true);
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
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

        alert = builder.show();
    }

    private void actualitzarArticle(long id) {
        // Cridem a l'activity del detall de la tasca enviant com a id -1
        Bundle bundle = new Bundle();
        bundle.putLong("id",id);

        Intent i = new Intent(this, detallArticle.class );
        i.putExtras(bundle);
        startActivityForResult(i,ARTICLE_UPDATE);
    }

    public void eliminarArticle(final int _id) {
        // Pedimos confirmación
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("¿Desitja eliminar la tasca?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                bd.delete(_id);
                carregaArticles();
            }
        });

        builder.setNegativeButton("No", null);

        builder.show();
    }

    private void filtreOff() {
        // Demanem totes les tasques finalitzades
        Cursor cursorArticles = bd.articles();
        filterActual = filtreArticles.FILTER_ALL;

        // Notifiquem al adapter que les dades han canviat i que refresqui
        scArticles.changeCursor(cursorArticles);
        scArticles.notifyDataSetChanged();

        // Ens situem en el primer registre
        ListView lv = (ListView) findViewById(R.id.lvArticles);
        lv.setSelection(0);

        Snackbar.make(findViewById(android.R.id.content), "Ensenyant tots els articles...", Snackbar.LENGTH_LONG).show();
    }

    private void filtreStock() {
        // Demanem totes les tasques finalitzades
        Cursor cursorArticles = bd.articlesStock();
        filterActual = filtreArticles.FILTER_STOCK;

        // Notifiquem al adapter que les dades han canviat i que refresqui
        scArticles.changeCursor(cursorArticles);
        scArticles.notifyDataSetChanged();

        // Ens situem en el primer registre
        ListView lv = (ListView) findViewById(R.id.lvArticles);
        lv.setSelection(0);

        Snackbar.make(findViewById(android.R.id.content), "Ensenyant articles disponibles...", Snackbar.LENGTH_LONG).show();
    }

    private void filtreNoStock() {
        // Demanem totes les tasques finalitzades
        Cursor cursorArticles = bd.articlesNoStock();
        filterActual = filtreArticles.FILTER_NOSTOCK;

        // Notifiquem al adapter que les dades han canviat i que refresqui
        scArticles.changeCursor(cursorArticles);
        scArticles.notifyDataSetChanged();

        // Ens situem en el primer registre
        ListView lv = (ListView) findViewById(R.id.lvArticles);
        lv.setSelection(0);

        Snackbar.make(findViewById(android.R.id.content), "Ensenyant articles sense stock actualment...", Snackbar.LENGTH_LONG).show();
    }

    private void showDatePickerDialog(final EditText edtDatePicker, final Intent i) {
        final DatePickerFragment picker = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                month = month + 1;

                String formatedDay = String.valueOf(day);
                String formatedMonth = String.valueOf(month);

                if (day < 10) {
                    formatedDay = "0" + day;
                }

                if (month < 10) {
                    formatedMonth = "0" + month;
                }

                //final String selectedDate = formatedDay + "/" + formatedMonth + "/" + year;
                final String selectedDate = year + "-" + formatedMonth + "-" + formatedDay;
                edtDatePicker.setText(selectedDate);
                i.putExtra("data", selectedDate);
                startActivity(i);
            }
        });

        picker.show(getSupportFragmentManager(), "datePicker");
    }

    /*private class myCustomAlertDialog extends AlertDialog {

        protected myCustomAlertDialog(Context context) {
            super(context);

            setTitle("Data");
            setMessage("Dia dels moviments?");

            final EditText data = new EditText(MainActivity.this);
            data.setFocusable(false);
            data.setClickable(true);
            data.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    showDatePickerDialog(data, i);
                }
            });

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);

            data.setGravity(Gravity.CENTER);

            data.setLayoutParams(lp);
            setView(data);

            setNeutralButton("Veure tot", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    i.putExtra("data", (Parcelable[]) null);
                    startActivity(i);
                }
            });

            setPositiveButton("Día", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    showDatePickerDialog(data, i);
                }
            });

            setNegativeButton("Cancel", null);
        }

    }*/
}
