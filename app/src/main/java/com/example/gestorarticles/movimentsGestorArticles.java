package com.example.gestorarticles;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class movimentsGestorArticles extends AppCompatActivity {

    private GestorArticlesDataSource bd;
    private adapterGestorArticles scArticles;

    private static String[] from = new String[]{GestorArticlesDataSource.GESTORARTICLES_DESCRIPCION, GestorArticlesDataSource.MOVIMENTS_DIA, GestorArticlesDataSource.MOVIMENTS_QUANTITAT};
    private static int[] to = new int[]{R.id.tvCodiArticle, R.id.tvDescripcio, R.id.tvNumUnitats};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_moviments);

        setTitle("Moviments");
        bd = new GestorArticlesDataSource(this);

        Cursor cursorArticles = bd.articles();

        scArticles = new adapterGestorArticles(this, R.layout.layout_article, cursorArticles, from, to, 1);

        ListView lv = findViewById(R.id.lvArticles);
        lv.setAdapter(scArticles);
    }
}
