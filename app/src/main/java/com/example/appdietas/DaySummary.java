package com.example.appdietas;

public class DaySummary {
    private final int dayIndex;
    private final String dayName;
    private final int calories;
    private final int protein;
    private final int carbs;
    private final int fat;

    public DaySummary(int dayIndex, String dayName, int calories, int protein, int carbs, int fat) {
        this.dayIndex = dayIndex;
        this.dayName = dayName;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
    }

    public int getDayIndex() {
        return dayIndex;
    }

    public String getDayName() {
        return dayName;
    }

    public int getCalories() {
        return calories;
    }

    public int getProtein() {
        return protein;
    }

    public int getCarbs() {
        return carbs;
    }

    public int getFat() {
        return fat;
    }
}
