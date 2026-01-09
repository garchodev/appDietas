package com.example.appdietas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.appdietas.login.LoginActivity;
import com.example.appdietas.data.AppDatabaseHelper;
import com.example.appdietas.data.AppDatabaseHelper.Meal;
import com.example.appdietas.data.AppDatabaseHelper.WeeklyPlanEntry;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LandingActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "appPrefs";
    private static final String PREF_KEY_FIRST_RUN = "isFirstRun";
    private static final String PREF_KEY_PLAN_INITIALIZED = "weeklyPlanInitialized";
    private static final String[] TIPOS_COMIDA = {"Desayuno", "Comida", "Cena"};

    private ViewPager2 viewPager;
    private LinearLayout layoutDots;
    private ImageView[] dots;
    private LandingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_landing);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isFirstRun = prefs.getBoolean(PREF_KEY_FIRST_RUN, true);

        ensureWeeklyPlanInitialized(prefs);

        if (!isFirstRun) {
            // !isFirstRun para la version FINAL IMPORTANTEEEEEE
            // Ya ha abierto la app antes → ir directamente al MainActivity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        viewPager = findViewById(R.id.landingViewPager);
        layoutDots = findViewById(R.id.layoutDots);

        List<Integer> layouts = Arrays.asList(
                R.layout.landing_slide1,
                R.layout.landing_slide2,
                R.layout.landing_slide3
        );

        adapter = new LandingAdapter(layouts);
        viewPager.setAdapter(adapter);

        addDots(layouts.size());
        setCurrentDot(0);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentDot(position);

                if (position == layouts.size() - 1) {
                    // Buscar el botón dentro del último slide
                    Button buttonStart = viewPager.findViewById(R.id.buttonContinuar);
                    if (buttonStart != null) {
                        buttonStart.setOnClickListener(v -> {
                            SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                            editor.putBoolean(PREF_KEY_FIRST_RUN, false);
                            editor.apply();
                            Intent intent = new Intent(LandingActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        });
                    }
                }
            }
        });
    }

    private void addDots(int count) {
        dots = new ImageView[count];
        layoutDots.removeAllViews();

        for (int i = 0; i < count; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageResource(R.drawable.dot_inactive);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0);
            layoutDots.addView(dots[i], params);
        }
    }

    private void setCurrentDot(int index) {
        for (int i = 0; i < dots.length; i++) {
            dots[i].setImageResource(i == index ? R.drawable.dot_active : R.drawable.dot_inactive);
        }
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
