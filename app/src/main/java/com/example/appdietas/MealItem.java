package com.example.appdietas;

public class MealItem {
    private final int dayIndex;
    private final String dayName;
    private final String label;
    private final int imageResId;

    private final Comida comida;

    public MealItem(int dayIndex, String dayName, String label, int imageResId, Comida comida) {
        this.dayIndex = dayIndex;
        this.dayName = dayName;
        this.label = label;
        this.imageResId = imageResId;
        this.comida = comida;
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

    public Comida getComida() {
        return comida;
    }
}
