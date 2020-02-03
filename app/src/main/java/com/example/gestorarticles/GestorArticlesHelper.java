package com.example.gestorarticles;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GestorArticlesHelper extends SQLiteOpenHelper {

    // database version
    private static final int database_VERSION = 2;

    // database name
    private static final String database_NAME = "GestorArticlesDataBase";

    public GestorArticlesHelper(Context context) {
        super(context, database_NAME, null, database_VERSION);
    }

    String CREATE_GESTORARTICLES =
            "CREATE TABLE gestorarticles ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "codiarticle TEXT," +
                    "descripcio TEXT," +
                    "pvp REAL," +
                    "stock INTEGER)";

    String CREATE_MOVIMENTS =
            "CREATE TABLE moviments ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "codiarticle TEXT," +
                    "dia TEXT," +
                    "quantitat INTEGER," +
                    "tipus TEXT," +
                    "FOREIGN KEY(codiarticle) REFERENCES gestorarticles(codiarticle))";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_GESTORARTICLES);
        db.execSQL(CREATE_MOVIMENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL(CREATE_MOVIMENTS);
        }
    }
}