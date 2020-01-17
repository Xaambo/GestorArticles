package com.example.gestorarticles;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class detallArticle extends AppCompatActivity {

    private long idArticle;
    private GestorArticlesDataSource bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_detallarticle);

        bd = new GestorArticlesDataSource(this);

        setTitle("Article");

        // Boton ok
        Button btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                aceptar();
            }
        });

        // Boton eliminar
        Button  btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                eliminar();
            }
        });

        // Boton cancelar
        Button  btnCancel = (Button) findViewById(R.id.btnCancelar);
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancelar();
            }
        });

        // Busquem el id que estem modificant
        // si el el id es -1 vol dir que s'està creant
        idArticle = this.getIntent().getExtras().getLong("id");

        if (idArticle != -1) {
            // Si estem modificant carreguem les dades en pantalla
            carregarDadesArticle();
        }
        else {
            // Si estem creant amaguem el checkbox de finalitzada i el botó d'eliminar
            btnDelete.setVisibility(View.GONE);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void carregarDadesArticle() {

        // Cridem a la funció que busca un únic article
        Cursor datos = bd.article(idArticle);
        datos.moveToFirst();

        // Posem les dades en el detall ready per a ser modificades
        EditText edt;
        float preu;
        String desc;

        edt = findViewById(R.id.edtCodiArticle);
        edt.setFocusable(false);
        edt.setText(datos.getString(datos.getColumnIndex(GestorArticlesDataSource.GESTORARTICLES_CODIARTICLE)));

        edt = findViewById(R.id.edtDescripcio);

        desc = datos.getString(datos.getColumnIndex(GestorArticlesDataSource.GESTORARTICLES_DESCRIPCION));
        setTitle("Article: " + desc);

        edt.setText(desc);

        edt = findViewById(R.id.edtStock);
        edt.setFocusable(false);
        edt.setText(datos.getString(datos.getColumnIndex(GestorArticlesDataSource.GESTORARTICLES_STOCK)));

        preu = datos.getFloat(datos.getColumnIndex(GestorArticlesDataSource.GESTORARTICLES_PVP));
        
        edt = findViewById(R.id.edtPreu);
        edt.setText(String.valueOf(preu));
    }

    private void aceptar() {
        // Validem les dades
        EditText edt;
        int count;

        // El codi d'article ha d'estar informat i ha de ser únic
        edt = findViewById(R.id.edtCodiArticle);
        String codiArticle = edt.getText().toString();

        Cursor datos = bd.countArticle(codiArticle);
        boolean hiHaDades = datos.moveToFirst();

        if (hiHaDades) {
            count = Integer.parseInt(datos.getString(datos.getColumnIndex(GestorArticlesDataSource.GESTORARTICLES_CODIARTICLE)));

            if (count > 0 && idArticle == -1) {
                Snackbar.make(findViewById(android.R.id.content), "Ja existeix un article amb aquest codi.", Snackbar.LENGTH_LONG).show();
                return;
            }
        }

        if (codiArticle.length() <= 0) {
            Snackbar.make(findViewById(android.R.id.content), "El codi d'article ha d'estar informat.", Snackbar.LENGTH_LONG).show();
            return;
        }

        // La descripció ha d'estar informada
        edt = findViewById(R.id.edtDescripcio);
        String descripcio = edt.getText().toString();

        if (descripcio.length() <= 0) {
            Snackbar.make(findViewById(android.R.id.content), "La descripció ha d'estar informada", Snackbar.LENGTH_LONG).show();
            return;
        }

        edt = findViewById(R.id.edtStock);
        int stock = Integer.valueOf(edt.getText().toString());

        edt = findViewById(R.id.edtPreu);
        float preu = Float.valueOf(edt.getText().toString());

        // Mirem si estem creant o estem guardant
        if (idArticle == -1) {
            idArticle = bd.insert(codiArticle, descripcio, preu, stock);
        }
        else {
            bd.update(idArticle,descripcio,preu,stock);
        }

        Intent i = new Intent();
        i.putExtra("id", idArticle);
        setResult(RESULT_OK, i);

        finish();
    }

    private void cancelar() {
        Intent i = new Intent();
        i.putExtra("id", idArticle);
        setResult(RESULT_CANCELED, i);

        finish();
    }

    private void eliminar() {

        // Demanem confirmació ja que és algo important
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Vols eliminar l'article?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                bd.delete(idArticle);

                Intent i = new Intent();
                i.putExtra("id", -1);  // Tornem el -1 indicant que s'ha eliminat
                setResult(RESULT_OK, i);

                finish();
            }
        });

        builder.setNegativeButton("No", null);

        builder.show();

    }
}
