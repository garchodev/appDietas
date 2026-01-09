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
        DaySummary summary = item.getSummary();
        holder.dayTitle.setText(summary.getDayName());
        holder.daySummary.setText(formatSummary(summary));

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
