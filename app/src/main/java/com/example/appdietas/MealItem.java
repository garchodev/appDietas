package com.example.appdietas;

public class MealItem {
    private final int dayIndex;
    private final String dayName;
    private final String label;
    private final int imageResId;

    public MealItem(int dayIndex, String dayName, String label, int imageResId) {
        this.dayIndex = dayIndex;
        this.dayName = dayName;
        this.label = label;
        this.imageResId = imageResId;
    }

    public int getDayIndex() {
        return dayIndex;
    }

    public String getDayName() {
        return dayName;
    }

    public String getLabel() {
        return label;
    }

    public int getImageResId() {
        return imageResId;
    }
}
