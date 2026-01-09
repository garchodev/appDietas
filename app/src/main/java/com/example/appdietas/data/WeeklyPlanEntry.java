package com.example.appdietas.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(
        tableName = "weekly_plan",
        primaryKeys = {"day", "tipo"},
        foreignKeys = @ForeignKey(
                entity = Comida.class,
                parentColumns = "id",
                childColumns = "comidaId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = @Index("comidaId")
)
public class WeeklyPlanEntry {
    private int day;
    private String tipo;
    private long comidaId;

    public WeeklyPlanEntry(int day, String tipo, long comidaId) {
        this.day = day;
        this.tipo = tipo;
        this.comidaId = comidaId;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public long getComidaId() {
        return comidaId;
    }

    public void setComidaId(long comidaId) {
        this.comidaId = comidaId;
    }
}
