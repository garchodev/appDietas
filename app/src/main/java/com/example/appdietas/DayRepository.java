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
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_DAY_SUMMARY = "day_summary";
    private static final String COLUMN_DAY_INDEX = "day_index";
    private static final String COLUMN_DAY_NAME = "day_name";
    private static final String COLUMN_CALORIES = "calories";
    private static final String COLUMN_PROTEIN = "protein";
    private static final String COLUMN_CARBS = "carbs";
    private static final String COLUMN_FAT = "fat";

    public DayRepository(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_DAY_SUMMARY + " ("
            + COLUMN_DAY_INDEX + " INTEGER PRIMARY KEY, "
            + COLUMN_DAY_NAME + " TEXT NOT NULL, "
            + COLUMN_CALORIES + " INTEGER NOT NULL, "
            + COLUMN_PROTEIN + " INTEGER NOT NULL, "
            + COLUMN_CARBS + " INTEGER NOT NULL, "
            + COLUMN_FAT + " INTEGER NOT NULL"
            + ")";
        db.execSQL(createTable);
        seedDefaults(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAY_SUMMARY);
        onCreate(db);
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
}
