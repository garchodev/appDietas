package com.example.appdietas;

import java.util.List;

public class DayItem {
    private final DaySummary summary;
    private final List<MealItem> meals;

    public DayItem(DaySummary summary, List<MealItem> meals) {
        this.summary = summary;
        this.meals = meals;
    }

    public DaySummary getSummary() {
        return summary;
    }

    public List<MealItem> getMeals() {
        return meals;
    }
}
