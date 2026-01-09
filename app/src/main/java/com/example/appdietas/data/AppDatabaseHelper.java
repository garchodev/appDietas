package com.example.appdietas.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class AppDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "appdietas.db";
    private static final int DATABASE_VERSION = 1;

    public AppDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE comidas ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nombre TEXT NOT NULL,"
                + "tipo TEXT NOT NULL"
                + ")");
        db.execSQL("CREATE TABLE weekly_plan ("
                + "day INTEGER NOT NULL,"
                + "tipo TEXT NOT NULL,"
                + "comida_id INTEGER NOT NULL,"
                + "PRIMARY KEY (day, tipo),"
                + "FOREIGN KEY (comida_id) REFERENCES comidas(id) ON DELETE CASCADE"
                + ")");

        seedComidas(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS weekly_plan");
        db.execSQL("DROP TABLE IF EXISTS comidas");
        onCreate(db);
    }

    public List<Meal> getMealsByType(String tipo) {
        List<Meal> meals = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.query(
                "comidas",
                new String[]{"id", "nombre", "tipo"},
                "tipo = ?",
                new String[]{tipo},
                null,
                null,
                "nombre ASC"
        )) {
            while (cursor.moveToNext()) {
                Meal meal = new Meal(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                        cursor.getString(cursor.getColumnIndexOrThrow("tipo"))
                );
                meals.add(meal);
            }
        }
        return meals;
    }

    public List<WeeklyPlanEntry> loadWeeklyPlan() {
        List<WeeklyPlanEntry> plan = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT weekly_plan.day, weekly_plan.tipo, weekly_plan.comida_id, comidas.nombre "
                + "FROM weekly_plan "
                + "JOIN comidas ON comidas.id = weekly_plan.comida_id "
                + "ORDER BY weekly_plan.day, weekly_plan.tipo";
        try (Cursor cursor = db.rawQuery(query, null)) {
            while (cursor.moveToNext()) {
                plan.add(new WeeklyPlanEntry(
                        cursor.getInt(cursor.getColumnIndexOrThrow("day")),
                        cursor.getString(cursor.getColumnIndexOrThrow("tipo")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("comida_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                ));
            }
        }
        return plan;
    }

    public void saveWeeklyPlan(List<WeeklyPlanEntry> planEntries) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            for (WeeklyPlanEntry entry : planEntries) {
                ContentValues values = new ContentValues();
                values.put("day", entry.day);
                values.put("tipo", entry.tipo);
                values.put("comida_id", entry.mealId);
                db.insertWithOnConflict("weekly_plan", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private void seedComidas(SQLiteDatabase db) {
        insertMeal(db, "Tostadas integrales", "Desayuno");
        insertMeal(db, "Yogur con fruta", "Desayuno");
        insertMeal(db, "Avena con plátano", "Desayuno");
        insertMeal(db, "Ensalada de pollo", "Comida");
        insertMeal(db, "Pasta con verduras", "Comida");
        insertMeal(db, "Arroz con pescado", "Comida");
        insertMeal(db, "Sopa de verduras", "Cena");
        insertMeal(db, "Tortilla francesa", "Cena");
        insertMeal(db, "Crema de calabaza", "Cena");
    }

    private void insertMeal(SQLiteDatabase db, String nombre, String tipo) {
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("tipo", tipo);
        db.insert("comidas", null, values);
    }

    public static class Meal {
        public final int id;
        public final String name;
        public final String type;

        public Meal(int id, String name, String type) {
            this.id = id;
            this.name = name;
            this.type = type;
        }
    }

    public static class WeeklyPlanEntry {
        public final int day;
        public final String tipo;
        public final int mealId;
        public final String mealName;

        public WeeklyPlanEntry(int day, String tipo, int mealId, String mealName) {
            this.day = day;
            this.tipo = tipo;
            this.mealId = mealId;
            this.mealName = mealName;
        }
    }
}
