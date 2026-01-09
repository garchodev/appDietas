package com.example.appdietas.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface WeeklyPlanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void upsert(WeeklyPlanEntry entry);

    @Query("DELETE FROM weekly_plan WHERE day = :day AND tipo = :tipo")
    void deleteByDayTipo(int day, String tipo);

    @Transaction
    @Query("SELECT * FROM weekly_plan ORDER BY day ASC, tipo ASC")
    List<WeeklyPlanEntryWithComida> loadWeeklyPlanWithComidas();
}
