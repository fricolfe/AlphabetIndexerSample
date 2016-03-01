package com.suappmovil.alphabetindexersample.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Fermin on 01/08/2014.
 */
public class MainDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Main.db";
    private static final int DATABASE_VERSION = 1;

    public MainDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static class Coches {
        public static final String TABLE = "Coches";

        public static class Field {
            public static final String ID = "_id";
            public static final String MARCA = "Marca";
            public static final String MODELO = "Modelo";
            public static final String VERSION = "Version";
            public static final String ANYO = "Anyo";
        }

        private static final String CREATE = "CREATE TABLE " + TABLE + "(" +
                Field.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Field.MARCA + " TEXT," +
                Field.MODELO + " TEXT," +
                Field.VERSION + " TEXT," +
                Field.ANYO + " INTEGER" +
                ");";

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Coches.CREATE);
        CharSequence s = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0; i < s.length(); i++) {
            for (int m = 1; m < 3; m++) {
                for (int v = 1; v < 3; v++) {
                    ContentValues values = new ContentValues();
                    values.put(Coches.Field.MARCA, String.format("%cMarca", s.charAt(i)));
                    values.put(Coches.Field.MODELO, String.format("Modelo %d", m));
                    values.put(Coches.Field.VERSION, String.format("Version %d", v));
                    values.put(Coches.Field.ANYO, String.format("%d", 1950 + v + (m * 5)));
                    db.insert(Coches.TABLE, null, values);
//                            db.execSQL(String.format("INSERT INTO Coches (Marca, Modelo, Version,Anyo) VALUES ('%1$cMarca','Modelo %2$d', 'Version %3$d',%4$d)", s.charAt(i), m, v, 1950 + v  + (m * 5)));
                }
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
