package com.example.appdietas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {

    private final List<DayItem> dayItems;
    private final MealAdapter.OnMealClickListener mealClickListener;

    public DayAdapter(List<DayItem> dayItems, MealAdapter.OnMealClickListener mealClickListener) {
        this.dayItems = dayItems;
        this.mealClickListener = mealClickListener;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        DayItem item = dayItems.get(position);

        // 1. Obtener el nombre del día
        if (item.getSummary() != null) {
            holder.dayTitle.setText(item.getSummary().getDayName());
        }

        // 2. Calcular totales dinámicamente a partir de la clase Comida
        int totalCalories = 0;
        int totalProtein = 0;
        int totalCarbs = 0;
        int totalFat = 0;

        if (item.getMeals() != null) {
            for (MealItem mealItem : item.getMeals()) {
                Comida c = mealItem.getComida(); // Ya no dará error
                if (c != null) {
                    totalCalories += c.getCalorias();
                    totalProtein += c.getProteinas();
                    totalCarbs += c.getCarbohidratos();
                    totalFat += c.getLipidos();
                }
            }
        }

        // Usamos Locale para evitar problemas de formato y %d porque son enteros
        String summaryText = String.format(java.util.Locale.getDefault(),
                "Calorías: %d kcal · Prot: %dg · Carbs: %dg · Grasas: %dg",
                totalCalories, totalProtein, totalCarbs, totalFat);

        holder.daySummary.setText(summaryText);

        if (holder.mealRecyclerView.getLayoutManager() == null) {
            holder.mealRecyclerView.setLayoutManager(
                new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false)
            );
        }
        holder.mealRecyclerView.setAdapter(new MealAdapter(item.getMeals(), mealClickListener));
    }

    @Override
    public int getItemCount() {
        return dayItems.size();
    }

    private String formatSummary(DaySummary summary) {
        return "Calorías: " + summary.getCalories() + " kcal · Prot: " + summary.getProtein()
            + "g · Carbs: " + summary.getCarbs() + "g · Grasas: " + summary.getFat() + "g";
    }

    static class DayViewHolder extends RecyclerView.ViewHolder {
        private final TextView dayTitle;
        private final TextView daySummary;
        private final RecyclerView mealRecyclerView;

        DayViewHolder(@NonNull View itemView) {
            super(itemView);
            dayTitle = itemView.findViewById(R.id.textDayTitle);
            daySummary = itemView.findViewById(R.id.textDaySummary);
            mealRecyclerView = itemView.findViewById(R.id.mealRecyclerView);
        }
    }
}
