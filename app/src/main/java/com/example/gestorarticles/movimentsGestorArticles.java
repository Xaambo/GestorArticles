package com.example.gestorarticles;

import android.database.Cursor;
import android.os.Bundle;
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
        String data = extras.getString("data");

        Cursor cursorMoviments;

        if (data != null) {
            cursorMoviments = bd.movimentsEnData(data);
        } else {
            cursorMoviments = bd.moviments();
        }

        scMoviments = new adapterMovimentsGestorArticles(this, R.layout.layout_moviment, cursorMoviments, from, to, 1);

        ListView lv = findViewById(R.id.lvMoviments);
        lv.setAdapter(scMoviments);
    }
}
