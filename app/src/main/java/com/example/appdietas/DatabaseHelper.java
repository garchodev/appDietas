package com.example.appdietas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "appdietas.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_COMIDAS = "comidas";
    public static final String COLUMN_COMIDA_ID = "id";
    public static final String COLUMN_COMIDA_NOMBRE = "nombre";
    public static final String COLUMN_COMIDA_DESCRIPCION = "descripcion";
    public static final String COLUMN_COMIDA_GRAMAJES = "gramajes";
    public static final String COLUMN_COMIDA_GRASAS = "grasas";
    public static final String COLUMN_COMIDA_PROTEINAS = "proteinas";
    public static final String COLUMN_COMIDA_CARBOHIDRATOS = "carbohidratos";
    public static final String COLUMN_COMIDA_CALORIAS = "calorias";
    public static final String COLUMN_COMIDA_TIPO = "tipo";

    public static final String TABLE_COMIDAS_DIA = "comidas_dia";
    public static final String COLUMN_COMIDAS_DIA_DIA = "dia";
    public static final String COLUMN_COMIDAS_DIA_TIPO = "tipo";
    public static final String COLUMN_COMIDAS_DIA_COMIDA_ID = "comida_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createComidas = "CREATE TABLE " + TABLE_COMIDAS + " ("
                + COLUMN_COMIDA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_COMIDA_NOMBRE + " TEXT NOT NULL, "
                + COLUMN_COMIDA_DESCRIPCION + " TEXT, "
                + COLUMN_COMIDA_GRAMAJES + " REAL, "
                + COLUMN_COMIDA_GRASAS + " REAL, "
                + COLUMN_COMIDA_PROTEINAS + " REAL, "
                + COLUMN_COMIDA_CARBOHIDRATOS + " REAL, "
                + COLUMN_COMIDA_CALORIAS + " REAL, "
                + COLUMN_COMIDA_TIPO + " TEXT"
                + ");";

        String createComidasDia = "CREATE TABLE " + TABLE_COMIDAS_DIA + " ("
                + COLUMN_COMIDAS_DIA_DIA + " TEXT NOT NULL, "
                + COLUMN_COMIDAS_DIA_TIPO + " TEXT NOT NULL, "
                + COLUMN_COMIDAS_DIA_COMIDA_ID + " INTEGER, "
                + "PRIMARY KEY (" + COLUMN_COMIDAS_DIA_DIA + ", " + COLUMN_COMIDAS_DIA_TIPO + ")"
                + ");";

        db.execSQL(createComidas);
        db.execSQL(createComidasDia);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMIDAS_DIA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMIDAS);
        onCreate(db);
    }

    public int getRandomMealIdByTipo(String tipo) {
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.rawQuery(
                "SELECT " + COLUMN_COMIDA_ID + " FROM " + TABLE_COMIDAS
                        + " WHERE " + COLUMN_COMIDA_TIPO + " = ?"
                        + " ORDER BY RANDOM() LIMIT 1",
                new String[]{tipo}
        )) {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
        }
        return -1;
    }

    public long insertMeal(String nombre, String descripcion, double gramajes, double grasas,
                           double proteinas, double carbohidratos, double calorias, String tipo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMIDA_NOMBRE, nombre);
        values.put(COLUMN_COMIDA_DESCRIPCION, descripcion);
        values.put(COLUMN_COMIDA_GRAMAJES, gramajes);
        values.put(COLUMN_COMIDA_GRASAS, grasas);
        values.put(COLUMN_COMIDA_PROTEINAS, proteinas);
        values.put(COLUMN_COMIDA_CARBOHIDRATOS, carbohidratos);
        values.put(COLUMN_COMIDA_CALORIAS, calorias);
        values.put(COLUMN_COMIDA_TIPO, tipo);
        return db.insert(TABLE_COMIDAS, null, values);
    }

    public void upsertComidaDia(String dia, String tipo, int comidaId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMIDAS_DIA_DIA, dia);
        values.put(COLUMN_COMIDAS_DIA_TIPO, tipo);
        values.put(COLUMN_COMIDAS_DIA_COMIDA_ID, comidaId);
        db.insertWithOnConflict(TABLE_COMIDAS_DIA, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }
}
