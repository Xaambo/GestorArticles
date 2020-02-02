package com.example.gestorarticles;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.material.snackbar.Snackbar;

public class adapterGestorArticles extends android.widget.SimpleCursorAdapter {
    
    private static final String noStock = "#BFD78290";
    private static final String wStock = "#FFFFFF";

    private  MainActivity gestorArticles;

    public adapterGestorArticles(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        gestorArticles = (MainActivity) context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final View view = super.getView(position, convertView, parent);

        // Agafem l'objecte de la view que es una LINEA DEL CURSOR
        Cursor article = (Cursor) getItem(position);
        int stock = article.getInt(article.getColumnIndexOrThrow(GestorArticlesDataSource.GESTORARTICLES_STOCK));

        // Pintem el fons de l'article depenent de si té o no stock
        if (stock <= 0) {
            view.setBackgroundColor(Color.parseColor(noStock));
        }
        else {
            view.setBackgroundColor(Color.parseColor(wStock));
        }

        // Capturem botons
        ImageView btnBorrar = view.findViewById(R.id.ivBorrar);

        btnBorrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Busquem la linia a eliminar
                View row = (View) v.getParent().getParent();
                // Busquem el listView per poder treure el numero de la fila
                ListView lv = (ListView) row.getParent();
                // Busco quina posicio ocupa la Row dins de la ListView
                int position = lv.getPositionForView(row);
                // Carrego la linia del cursor de la posició.
                Cursor linia = (Cursor) getItem(position);

                gestorArticles.eliminarArticle(linia.getInt(linia.getColumnIndexOrThrow(GestorArticlesDataSource.GESTORARTICLES_ID)));
            }
        });

        ImageView ivIn = view.findViewById(R.id.ivIn);

        ivIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                manageArticles(v, "Afegir");

            }
        });

        ImageView ivOut = view.findViewById(R.id.ivOut);

        ivOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                manageArticles(v, "Treure");

            }
        });
        return view;
    }

    public void manageArticles(View v, String opcio) {

        // Busquem la linia a eliminar
        View row = (View) v.getParent().getParent();
        // Busquem el listView per poder treure el numero de la fila
        ListView lv = (ListView) row.getParent();
        // Busco quina posicio ocupa la Row dins de la ListView
        int position = lv.getPositionForView(row);
        // Carrego la linia del cursor de la posició.
        Cursor linia = (Cursor) getItem(position);

        Intent i = new Intent(v.getContext(), stockManagerGestorArticles.class);
        i.putExtra("opcio", opcio);
        i.putExtra("linia", (Parcelable) linia);
        v.getContext().startActivity(i);

    }
}