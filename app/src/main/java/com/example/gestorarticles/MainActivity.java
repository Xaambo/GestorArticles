package com.example.gestorarticles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private static int ARTICLE_ADD = 1;
    private static int ARTICLE_UPDATE = 2;

    private GestorArticlesDataSource bd;
    private adapterGestorArticles scTasks = new adapterGestorArticles(this, R.layout.row_todolisticon, cursorTasks, from, to, 1);
    private filtreArticles filterActual;

    private static String[] from = new String[]{GestorArticlesDataSource.GESTORARTICLES_CODIARTICLE, GestorArticlesDataSource.GESTORARTICLES_DESCRIPCION, GestorArticlesDataSource.GESTORARTICLES_PVP, GestorArticlesDataSource.GESTORARTICLES_STOCK};
    private static int[] to = new int[]{R.id.lblTitulo, R.id.lblDescription, R.id.lblPriority, R.id.lblState};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("ToolBar & Adapter Icon");
        bd = new GestorArticlesDataSource(this);

        filterActual = filtreArticles.FILTER_ALL;

        Cursor cursorTasks = bd.articles();

        scTasks = new adapterGestorArticles(this, R.layout.row_todolisticon, cursorTasks, from, to, 1);

        ListView lv = findViewById(R.id.lvArticles);
        lv.setAdapter(scTasks);

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

        // Demanem totes les tasques
        Cursor cursorTasks = null;

        // Demanem les tasques depenen del filtre que s'estigui aplicant
        switch (filterActual) {
            case FILTER_ALL:
                cursorTasks = bd.articles();
                break;
            case FILTER_STOCK:
                cursorTasks = bd.articlesStock();
                break;
            case FILTER_NOSTOCK:
                cursorTasks = bd.articlesNoStock();
                break;
        }

        // Un cop fet el filtre li diem al cursor que hem canviat les seves dades i que s'actualitzi
        scTasks.changeCursor(cursorTasks);
        scTasks.notifyDataSetChanged();
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
    }

    private void crearArticle() {
        // S'envia un -1 perque la creacio i la modificació fan servir la mateixa activity i s'ha de diferenciar
        Bundle bundle = new Bundle();
        bundle.putLong("id",-1);

        Intent i = new Intent(this, detallArticle.class );
        i.putExtras(bundle);
        startActivityForResult(i,ARTICLE_ADD);
    }

    private void actualitzarArticle(long id) {
        // Cridem a l'activity del detall de la tasca enviant com a id -1
        Bundle bundle = new Bundle();
        bundle.putLong("id",id);

        Intent i = new Intent(this, detallArticle.class );
        i.putExtras(bundle);
        startActivityForResult(i,ARTICLE_UPDATE);
    }

    private void filtreOff() {
        // Demanem totes les tasques finalitzades
        Cursor cursorTasks = bd.articles();
        filterActual = filtreArticles.FILTER_ALL;

        // Notifiquem al adapter que les dades han canviat i que refresqui
        scTasks.changeCursor(cursorTasks);
        scTasks.notifyDataSetChanged();

        // Ens situem en el primer registre
        ListView lv = (ListView) findViewById(R.id.lvArticles);
        lv.setSelection(0);

        Snackbar.make(findViewById(android.R.id.content), "Filtre apagat...", Snackbar.LENGTH_LONG).show();
    }

    private void filtreStock() {
        // Demanem totes les tasques finalitzades
        Cursor cursorTasks = bd.articlesStock();
        filterActual = filtreArticles.FILTER_STOCK;

        // Notifiquem al adapter que les dades han canviat i que refresqui
        scTasks.changeCursor(cursorTasks);
        scTasks.notifyDataSetChanged();

        // Ens situem en el primer registre
        ListView lv = (ListView) findViewById(R.id.lvArticles);
        lv.setSelection(0);

        Snackbar.make(findViewById(android.R.id.content), "Ensenyant articles disponibles...", Snackbar.LENGTH_LONG).show();
    }

    private void filtreNoStock() {
        // Demanem totes les tasques finalitzades
        Cursor cursorTasks = bd.articlesNoStock();
        filterActual = filtreArticles.FILTER_NOSTOCK;

        // Notifiquem al adapter que les dades han canviat i que refresqui
        scTasks.changeCursor(cursorTasks);
        scTasks.notifyDataSetChanged();

        // Ens situem en el primer registre
        ListView lv = (ListView) findViewById(R.id.lvArticles);
        lv.setSelection(0);

        Snackbar.make(findViewById(android.R.id.content), "Ensenyant articles sense stock actualment...", Snackbar.LENGTH_LONG).show();
    }
}
