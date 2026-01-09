package com.example.appdietas.data;

import java.util.List;

public class ComidaRepository {
    private final ComidaDao comidaDao;
    private final WeeklyPlanDao weeklyPlanDao;

    public ComidaRepository(AppDatabase database) {
        this.comidaDao = database.comidaDao();
        this.weeklyPlanDao = database.weeklyPlanDao();
    }

    public List<WeeklyPlanEntryWithComida> loadWeek() {
        return weeklyPlanDao.loadWeeklyPlanWithComidas();
    }

    public void replaceMealForDayTipo(int day, String tipo, long comidaId) {
        WeeklyPlanEntry entry = new WeeklyPlanEntry(day, tipo, comidaId);
        weeklyPlanDao.upsert(entry);
    }

    public long insertCustomMeal(Comida comida) {
        return comidaDao.insert(comida);
    }
}
