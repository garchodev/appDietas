package com.example.appdietas.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Comida.class, WeeklyPlanEntry.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "app_dietas.db";
    private static volatile AppDatabase instance;

    public abstract ComidaDao comidaDao();

    public abstract WeeklyPlanDao weeklyPlanDao();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    DATABASE_NAME
                            )
                            .build();
                }
            }
        }
        return instance;
    }
}
