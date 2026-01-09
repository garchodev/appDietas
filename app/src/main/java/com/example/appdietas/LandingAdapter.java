package com.example.appdietas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LandingAdapter extends RecyclerView.Adapter<LandingAdapter.ViewHolder> {
    private final List<Integer> layouts;
    private final OnStartClickListener onStartClickListener;

    public interface OnStartClickListener {
        void onStartClick();
    }

    public LandingAdapter(List<Integer> layouts, OnStartClickListener onStartClickListener) {
        this.layouts = layouts;
        this.onStartClickListener = onStartClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layouts.get(viewType), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Button startButton = holder.itemView.findViewById(R.id.buttonContinuar);
        if (startButton != null) {
            startButton.setOnClickListener(v -> {
                if (onStartClickListener != null) {
                    onStartClickListener.onStartClick();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return layouts.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
