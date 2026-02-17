package com.example.appdietas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DayRepository extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "dietas.db";
    private static final int DATABASE_VERSION = 2; // Incrementamos la versión para forzar actualización

    // Tabla Resumen (la que ya tenías)
    private static final String TABLE_DAY_SUMMARY = "day_summary";
    private static final String COLUMN_DAY_INDEX = "day_index";
    private static final String COLUMN_DAY_NAME = "day_name";
    private static final String COLUMN_CALORIES = "calories";
    private static final String COLUMN_PROTEIN = "protein";
    private static final String COLUMN_CARBS = "carbs";
    private static final String COLUMN_FAT = "fat";

    // NUEVA Tabla Comidas (para que buildMealsForDay funcione)
    private static final String TABLE_COMIDAS = "comidas";
    private static final String COLUMN_COMIDA_ID = "comida_id";
    private static final String COLUMN_COMIDA_DIA_ID = "dia_id";
    private static final String COLUMN_COMIDA_TIPO = "tipo";
    private static final String COLUMN_COMIDA_NOMBRE = "nombre";
    private static final String COLUMN_COMIDA_CAL = "calorias";
    private static final String COLUMN_COMIDA_PROT = "proteinas";
    private static final String COLUMN_COMIDA_CARB = "carbohidratos";
    private static final String COLUMN_COMIDA_LIP = "lipidos";
    private static final String COLUMN_COMIDA_IMG = "imagen_res_id";

    public DayRepository(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla de resumen
        String createSummaryTable = "CREATE TABLE " + TABLE_DAY_SUMMARY + " ("
                + COLUMN_DAY_INDEX + " INTEGER PRIMARY KEY, "
                + COLUMN_DAY_NAME + " TEXT NOT NULL, "
                + COLUMN_CALORIES + " INTEGER NOT NULL, "
                + COLUMN_PROTEIN + " INTEGER NOT NULL, "
                + COLUMN_CARBS + " INTEGER NOT NULL, "
                + COLUMN_FAT + " INTEGER NOT NULL"
                + ")";
        db.execSQL(createSummaryTable);

        // NUEVA: Crear tabla de comidas reales
        String createComidasTable = "CREATE TABLE " + TABLE_COMIDAS + " ("
                + COLUMN_COMIDA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_COMIDA_DIA_ID + " INTEGER, "
                + COLUMN_COMIDA_TIPO + " TEXT, "
                + COLUMN_COMIDA_NOMBRE + " TEXT, "
                + COLUMN_COMIDA_CAL + " INTEGER, "
                + COLUMN_COMIDA_PROT + " INTEGER, "
                + COLUMN_COMIDA_CARB + " INTEGER, "
                + COLUMN_COMIDA_LIP + " INTEGER, "
                + COLUMN_COMIDA_IMG + " INTEGER"
                + ")";
        db.execSQL(createComidasTable);

        seedDefaults(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAY_SUMMARY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMIDAS);
        onCreate(db);
    }

    // EL MÉTODO QUE LE FALTABA A TU MAINACTIVITY
    public List<Comida> getComidasForDay(int dayIndex) {
        List<Comida> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_COMIDAS, null, COLUMN_COMIDA_DIA_ID + "=?",
                new String[]{String.valueOf(dayIndex)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(new Comida(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMIDA_DIA_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMIDA_TIPO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMIDA_NOMBRE)),
                        "", // descripción
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMIDA_CAL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMIDA_CARB)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMIDA_PROT)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMIDA_LIP)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMIDA_IMG))
                ));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public List<DaySummary> getAllSummaries() {
        List<DaySummary> summaries = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.query(TABLE_DAY_SUMMARY, null, null, null, null, null, COLUMN_DAY_INDEX)) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    summaries.add(mapCursorToSummary(cursor));
                } while (cursor.moveToNext());
            }
        }
        return summaries;
    }

    private DaySummary mapCursorToSummary(Cursor cursor) {
        int dayIndex = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DAY_INDEX));
        String dayName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DAY_NAME));
        int calories = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CALORIES));
        int protein = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PROTEIN));
        int carbs = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CARBS));
        int fat = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FAT));
        return new DaySummary(dayIndex, dayName, calories, protein, carbs, fat);
    }

    private void seedDefaults(SQLiteDatabase db) {
        String[] days = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
        for (int i = 0; i < days.length; i++) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_DAY_INDEX, i + 1);
            values.put(COLUMN_DAY_NAME, days[i]);
            values.put(COLUMN_CALORIES, 0);
            values.put(COLUMN_PROTEIN, 0);
            values.put(COLUMN_CARBS, 0);
            values.put(COLUMN_FAT, 0);
            db.insert(TABLE_DAY_SUMMARY, null, values);
        }
    }

    private void insertComida(SQLiteDatabase db, int diaId, String tipo, String nombre, int cal, int carb, int prot, int lip, int img) {
        ContentValues v = new ContentValues();
        v.put(COLUMN_COMIDA_DIA_ID, diaId);
        v.put(COLUMN_COMIDA_TIPO, tipo);
        v.put(COLUMN_COMIDA_NOMBRE, nombre);
        v.put(COLUMN_COMIDA_CAL, cal);
        v.put(COLUMN_COMIDA_CARB, carb);
        v.put(COLUMN_COMIDA_PROT, prot);
        v.put(COLUMN_COMIDA_LIP, lip);
        v.put(COLUMN_COMIDA_IMG, img);
        db.insert(TABLE_COMIDAS, null, v);
    }
}
