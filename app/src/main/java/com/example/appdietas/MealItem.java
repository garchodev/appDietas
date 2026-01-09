package com.example.appdietas;

public class MealItem {
    private final String dayName;
    private final String label;
    private final int imageResId;

    public MealItem(String dayName, String label, int imageResId) {
        this.dayName = dayName;
        this.label = label;
        this.imageResId = imageResId;
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
