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
import android.widget.TextView;

public class adapterMovimentsGestorArticles extends android.widget.SimpleCursorAdapter {

    private static final String entrada = "#4600FF33";
    private static final String sortida = "#45FF0000";

    private movimentsGestorArticles movimentsArticles;

    public adapterMovimentsGestorArticles(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        movimentsArticles = (movimentsGestorArticles) context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final View view = super.getView(position, convertView, parent);

        TextView tvMoviment = view.findViewById(R.id.tvTipusMoviment);
        TextView tvUnitats = view.findViewById(R.id.tvNumUnitats);

        if (tvMoviment.getText().toString().equals("E")) {
            view.setBackgroundColor(Color.parseColor(entrada));
        } else {
            view.setBackgroundColor(Color.parseColor(sortida));
        }

        return view;
    }
}