package com.example.gestorarticles;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GestorArticlesHelper extends SQLiteOpenHelper {

    // database version
    private static final int database_VERSION = 1;

    // database name
    private static final String database_NAME = "GestorArticlesDataBase";

    public GestorArticlesHelper(Context context) {
        super(context, database_NAME, null, database_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_GESTORARTICLES =
                "CREATE TABLE gestorarticles ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "codiarticle TEXT," +
                        "descripcio TEXT," +
                        "pvp FLOAT," +
                        "stock INTEGER)";

        db.execSQL(CREATE_GESTORARTICLES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}