package com.example.gestorarticles;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GestorArticlesDataSource {

    public static final String table_GESTORARTICLES = "gestorarticles";
    public static final String GESTORARTICLES_ID = "_id";
    public static final String GESTORARTICLES_CODIARTICLE = "codiarticle";
    public static final String GESTORARTICLES_DESCRIPCION = "descripcio";
    public static final String GESTORARTICLES_PVP = "pvp";
    public static final String GESTORARTICLES_STOCK = "stock";

    private GestorArticlesHelper dbHelper;
    private SQLiteDatabase dbW, dbR;

    // CONSTRUCTOR
    public GestorArticlesDataSource(Context ctx) {
        // DB Connection
        dbHelper = new GestorArticlesHelper(ctx);

        // creem db descriptura i de lectura per mantenir les coses separades
        open();
    }

    private void open() {
        dbW = dbHelper.getWritableDatabase();
        dbR = dbHelper.getReadableDatabase();
    }

    // DESTRUCTOR
    protected void finalize () {
        // es tanquen les db
        dbW.close();
        dbR.close();
    }

    // CONSULTES
    public Cursor articles() {
        /* Consulta de tots els articles */
        String query = "SELECT * FROM gestorarticles";
        return dbR.rawQuery(query, null);
    }

    public Cursor articlesNoStock() {
        /* Consulta de tots els articles sense Stock */
        String query = "SELECT * FROM gestorarticles WHERE stock <= ?";
        String[] args = new String[] {"0"};
        return dbR.rawQuery(query, args);
    }

    public Cursor articlesStock() {
        /* Consulta de tots els articles amb Stock */
        String query = "SELECT * FROM gestorarticles WHERE stock > ?";
        String[] args = new String[] {"0"};
        return dbR.rawQuery(query, args);
    }

    // MODIFICACIO DB


}
