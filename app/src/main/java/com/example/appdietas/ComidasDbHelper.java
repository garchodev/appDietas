package com.example.appdietas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class ComidasDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "comidas.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_COMIDAS = "comidas";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_DIA_ID = "dia_id";
    private static final String COLUMN_TIPO = "tipo";
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_DESCRIPCION = "descripcion";
    private static final String COLUMN_CALORIAS = "calorias";
    private static final String COLUMN_CARBS = "carbohidratos";
    private static final String COLUMN_PROTEINAS = "proteinas";
    private static final String COLUMN_LIPIDOS = "lipidos";
    private static final String COLUMN_IMAGEN = "imagen_res";

    public ComidasDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE_COMIDAS + " ("
                        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COLUMN_DIA_ID + " INTEGER NOT NULL, "
                        + COLUMN_TIPO + " TEXT NOT NULL, "
                        + COLUMN_NOMBRE + " TEXT NOT NULL, "
                        + COLUMN_DESCRIPCION + " TEXT NOT NULL, "
                        + COLUMN_CALORIAS + " INTEGER NOT NULL, "
                        + COLUMN_CARBS + " INTEGER NOT NULL, "
                        + COLUMN_PROTEINAS + " INTEGER NOT NULL, "
                        + COLUMN_LIPIDOS + " INTEGER NOT NULL, "
                        + COLUMN_IMAGEN + " INTEGER NOT NULL"
                        + ");"
        );

        seedData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMIDAS);
        onCreate(db);
    }

    public List<Comida> getComidasForDia(int diaId) {
        List<Comida> comidas = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selection = COLUMN_DIA_ID + " = ?";
        String[] selectionArgs = {String.valueOf(diaId)};

        try (Cursor cursor = db.query(
                TABLE_COMIDAS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                COLUMN_ID + " ASC"
        )) {
            while (cursor.moveToNext()) {
                comidas.add(new Comida(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DIA_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIPO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPCION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CALORIAS)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CARBS)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PROTEINAS)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LIPIDOS)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IMAGEN))
                ));
            }
        }
        return comidas;
    }

    public Comida getMealForDayTipo(int diaId, String tipo) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = COLUMN_DIA_ID + " = ? AND " + COLUMN_TIPO + " = ?";
        String[] selectionArgs = {String.valueOf(diaId), tipo};

        try (Cursor cursor = db.query(
                TABLE_COMIDAS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                COLUMN_ID + " ASC",
                "1"
        )) {
            if (cursor.moveToFirst()) {
                return new Comida(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DIA_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIPO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPCION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CALORIAS)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CARBS)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PROTEINAS)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LIPIDOS)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IMAGEN))
                );
            }
        }
        return null;
    }

    public Comida getRandomMealByTipo(String tipo) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = COLUMN_TIPO + " = ?";
        String[] selectionArgs = {tipo};

        try (Cursor cursor = db.query(
                TABLE_COMIDAS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                "RANDOM()",
                "1"
        )) {
            if (cursor.moveToFirst()) {
                return new Comida(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DIA_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIPO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPCION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CALORIAS)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CARBS)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PROTEINAS)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LIPIDOS)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IMAGEN))
                );
            }
        }
        return null;
    }

    public boolean updateMealForDayTipo(int diaId, String tipo, Comida meal) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, meal.getNombre());
        values.put(COLUMN_DESCRIPCION, meal.getDescripcion());
        values.put(COLUMN_CALORIAS, meal.getCalorias());
        values.put(COLUMN_CARBS, meal.getCarbohidratos());
        values.put(COLUMN_PROTEINAS, meal.getProteinas());
        values.put(COLUMN_LIPIDOS, meal.getLipidos());
        values.put(COLUMN_IMAGEN, meal.getImagenResId());

        int updated = db.update(
                TABLE_COMIDAS,
                values,
                COLUMN_DIA_ID + " = ? AND " + COLUMN_TIPO + " = ?",
                new String[]{String.valueOf(diaId), tipo}
        );

        if (updated > 0) {
            return true;
        }

        values.put(COLUMN_DIA_ID, diaId);
        values.put(COLUMN_TIPO, tipo);
        return db.insert(TABLE_COMIDAS, null, values) != -1;
    }

    private void seedData(SQLiteDatabase db) {
        String[] tipos = {"Desayuno", "Comida", "Cena"};
        int[] imagenes = {R.drawable.desayuno1, R.drawable.imagencomida, R.drawable.imagencomida};

        db.beginTransaction();
        try {
            for (int dia = 1; dia <= 7; dia++) {
                for (int i = 0; i < tipos.length; i++) {
                    ContentValues values = new ContentValues();
                    values.put(COLUMN_DIA_ID, dia);
                    values.put(COLUMN_TIPO, tipos[i]);
                    values.put(COLUMN_NOMBRE, tipos[i] + " saludable Día " + dia);
                    values.put(COLUMN_DESCRIPCION, "Receta balanceada con ingredientes frescos.");
                    values.put(COLUMN_CALORIAS, 320 + (dia * 5) + (i * 30));
                    values.put(COLUMN_CARBS, 40 + (i * 5));
                    values.put(COLUMN_PROTEINAS, 20 + (i * 4));
                    values.put(COLUMN_LIPIDOS, 12 + (i * 3));
                    values.put(COLUMN_IMAGEN, imagenes[i]);
                    db.insert(TABLE_COMIDAS, null, values);
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
