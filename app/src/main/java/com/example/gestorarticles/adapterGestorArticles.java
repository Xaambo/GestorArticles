package com.example.gestorarticles;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

public class adapterGestorArticles extends android.widget.SimpleCursorAdapter {
    
    private static final String noStock = "#BFD78290";
    private static final String wStock = "#FFFFFF";

    public adapterGestorArticles(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = super.getView(position, convertView, parent);

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

        return view;
    }
}