package com.example.appdietas.data;

import androidx.room.Embedded;
import androidx.room.Relation;

public class WeeklyPlanEntryWithComida {
    @Embedded
    private WeeklyPlanEntry entry;

    @Relation(parentColumn = "comidaId", entityColumn = "id")
    private Comida comida;

    public WeeklyPlanEntry getEntry() {
        return entry;
    }

    public void setEntry(WeeklyPlanEntry entry) {
        this.entry = entry;
    }

    public Comida getComida() {
        return comida;
    }

    public void setComida(Comida comida) {
        this.comida = comida;
    }
}
