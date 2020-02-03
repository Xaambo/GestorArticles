package com.example.gestorarticles;

import android.content.ContentValues;
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

    public static final String table_MOVIMENTS = "moviments";
    public static final String MOVIMENTS_ID = "_id";
    public static final String MOVIMENTS_CODIARTICLE = "codiarticle";
    public static final String MOVIMENTS_DIA = "dia";
    public static final String MOVIMENTS_QUANTITAT = "quantitat";
    public static final String MOVIMENTS_TIPUS = "tipus";

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

    public Cursor moviments() {
        /* Consulta dels moviments */
        String query = "SELECT moviments._id, descripcio, quantitat, dia, tipus FROM moviments INNER JOIN gestorarticles ON moviments.codiarticle = gestorarticles.codiarticle";
        return dbR.rawQuery(query, null);
    }

    public Cursor movimentsEnData(String data) {
        /* Consulta dels moviments */
        String query = "SELECT moviments._id, descripcio, quantitat, dia, tipus FROM moviments INNER JOIN gestorarticles ON moviments.codiarticle = gestorarticles.codiarticle WHERE dia     = ?";
        String[] args = new String[] {data};
        return dbR.rawQuery(query, args);
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

    public Cursor article(long id) {
        /* Consulta d'un article */
        String query = "SELECT * FROM gestorarticles WHERE _id = ?";
        String[] args = new String[] {String.valueOf(id)};
        return dbR.rawQuery(query, args);
    }

    public Cursor countArticle(String id) {
        /* Consulta numero articles */
        String query = "SELECT COUNT(codiarticle) AS codiarticle FROM gestorarticles WHERE codiarticle = ?";
        String[] args = new String[] {id};
        return dbR.rawQuery(query, args);
    }

    // MODIFICACIO DB
    public long insert(String codiArticle, String descricpio, float pvp, int stock) {
        // Creem una nova tasca i retornem el id crear per si el necessiten
        ContentValues values = new ContentValues();
        values.put(GESTORARTICLES_CODIARTICLE, codiArticle);
        values.put(GESTORARTICLES_DESCRIPCION, descricpio);
        values.put(GESTORARTICLES_PVP, pvp);
        values.put(GESTORARTICLES_STOCK, stock);

        return dbW.insert(table_GESTORARTICLES,null, values);
    }

    public long insertMoviment(String codiArticle, String dia, int stock, String tipus) {
        // Creem una nova tasca i retornem el id crear per si el necessiten
        ContentValues values = new ContentValues();
        values.put(MOVIMENTS_CODIARTICLE, codiArticle);
        values.put(MOVIMENTS_DIA, dia);
        values.put(MOVIMENTS_QUANTITAT, stock);
        values.put(MOVIMENTS_TIPUS, tipus);

        return dbW.insert(table_MOVIMENTS,null, values);
    }

    public void update(long id, String descripcio, float pvp, int stock) {
        // Modifiquem els valors de las tasca amb clau primària "id"
        ContentValues values = new ContentValues();
        values.put(GESTORARTICLES_DESCRIPCION, descripcio);
        values.put(GESTORARTICLES_PVP,pvp);
        values.put(GESTORARTICLES_STOCK,stock);

        String[] args = new String[] {String.valueOf(id)};
        dbW.update(table_GESTORARTICLES,values, GESTORARTICLES_ID + " = ?", args);
    }

    public int updateStock(long id, int stock) {
        // Modifiquem els valors de las tasca amb clau primària "id"
        ContentValues values = new ContentValues();
        values.put(GESTORARTICLES_STOCK,stock);

        String[] args = new String[] {String.valueOf(id)};
        return dbW.update(table_GESTORARTICLES,values, GESTORARTICLES_ID + " = ?", args);
    }

    public void delete(long id) {
        // Eliminem l'artile amb clau primària "id"
        String[] args = new String[] {String.valueOf(id)};
        dbW.delete(table_GESTORARTICLES,GESTORARTICLES_ID + " = ?", args);
    }

    public void dropDataBase() {
        // Fem un drop de la data base
        String exec = "DROP TABLE IF EXISTS gestorarticles";
        dbW.execSQL(exec);
    }

}
