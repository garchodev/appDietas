package com.example.appdietas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appdietas.data.AppDatabaseHelper;
import com.example.appdietas.data.AppDatabaseHelper.Meal;
import com.example.appdietas.data.AppDatabaseHelper.WeeklyPlanEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LandingActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "appPrefs";
    private static final String PREF_KEY_FIRST_RUN = "isFirstRun";
    private static final String PREF_KEY_PLAN_INITIALIZED = "weeklyPlanInitialized";
    private static final String[] TIPOS_COMIDA = {"Desayuno", "Comida", "Cena"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_landing);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        ensureWeeklyPlanInitialized(prefs);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PREF_KEY_FIRST_RUN, false);
        editor.apply();

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void ensureWeeklyPlanInitialized(SharedPreferences prefs) {
        AppDatabaseHelper dbHelper = new AppDatabaseHelper(this);
        if (prefs.getBoolean(PREF_KEY_PLAN_INITIALIZED, false)) {
            List<WeeklyPlanEntry> entries = dbHelper.loadWeeklyPlan();
            if (!entries.isEmpty()) {
                mirrorPlanToPrefs(entries, prefs);
                return;
            }
        }

        List<WeeklyPlanEntry> plan = generateRandomPlan(dbHelper);
        dbHelper.saveWeeklyPlan(plan);
        mirrorPlanToPrefs(plan, prefs);
        prefs.edit().putBoolean(PREF_KEY_PLAN_INITIALIZED, true).apply();
    }

    private List<WeeklyPlanEntry> generateRandomPlan(AppDatabaseHelper dbHelper) {
        List<WeeklyPlanEntry> plan = new ArrayList<>();
        Random random = new Random();

        for (int day = 1; day <= 7; day++) {
            for (String tipo : TIPOS_COMIDA) {
                List<Meal> meals = dbHelper.getMealsByType(tipo);
                if (meals.isEmpty()) {
                    continue;
                }
                Meal selected = meals.get(random.nextInt(meals.size()));
                plan.add(new WeeklyPlanEntry(day, tipo, selected.id, selected.name));
            }
        }

        return plan;
    }

    private void mirrorPlanToPrefs(List<WeeklyPlanEntry> entries, SharedPreferences prefs) {
        SharedPreferences.Editor editor = prefs.edit();
        for (WeeklyPlanEntry entry : entries) {
            String key = buildPlanKey(entry.day, entry.tipo);
            editor.putString(key, entry.mealName);
        }
        editor.apply();
    }

    private String buildPlanKey(int day, String tipo) {
        return "week_plan_" + day + "_" + tipo;
    }
}
