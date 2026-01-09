package com.example.appdietas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_TEMP = "temp_meal_state";
    private static final String KEY_TEMP_CALORIES = "temp_calories_day_";
    private static final String KEY_TEMP_PROTEIN = "temp_protein_day_";
    private static final String KEY_TEMP_CARBS = "temp_carbs_day_";
    private static final String KEY_TEMP_FAT = "temp_fat_day_";

    private DayRepository dayRepository;
    private RecyclerView dayRecyclerView;
    private SharedPreferences sharedPreferences;

    private final SharedPreferences.OnSharedPreferenceChangeListener preferenceListener =
        (sharedPrefs, key) -> {
            if (key != null && key.startsWith("temp_")) {
                loadDays();
            }
        };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dayRepository = new DayRepository(this);
        sharedPreferences = getSharedPreferences(PREFS_TEMP, MODE_PRIVATE);

        dayRecyclerView = findViewById(R.id.dayRecyclerView);
        dayRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dayRecyclerView.setHasFixedSize(true);

        loadDays();
    }

    @Override
    protected void onStart() {
        super.onStart();
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(preferenceListener);
    }

                if (resID != 0) {
                    configurarClick(resID, textoParaEnviar, tipo, i);
                }
            }
    private void loadDays() {
        List<DaySummary> summaries = dayRepository.getAllSummaries();
        List<DayItem> dayItems = new ArrayList<>();
        for (DaySummary summary : summaries) {
            DaySummary resolvedSummary = applyTemporaryOverrides(summary);
            dayItems.add(new DayItem(resolvedSummary, buildMealsForDay(resolvedSummary.getDayName())));
        }

        dayRecyclerView.setAdapter(new DayAdapter(dayItems, this::openMealDetail));
    }

    private DaySummary applyTemporaryOverrides(DaySummary summary) {
        int dayIndex = summary.getDayIndex();
        int calories = getTempValue(KEY_TEMP_CALORIES, dayIndex, summary.getCalories());
        int protein = getTempValue(KEY_TEMP_PROTEIN, dayIndex, summary.getProtein());
        int carbs = getTempValue(KEY_TEMP_CARBS, dayIndex, summary.getCarbs());
        int fat = getTempValue(KEY_TEMP_FAT, dayIndex, summary.getFat());
        return new DaySummary(dayIndex, summary.getDayName(), calories, protein, carbs, fat);
    }

    private void configurarClick(int cardId, String nombreComida, String tipoComida, int diaId) {
        CardView card = findViewById(cardId);
        if (card != null) {
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Creamos el Intent para ir a la actividad de detalle
                    Intent intent = new Intent(MainActivity.this, ComidasDiaActivity.class);

                    // Pasamos datos extra (opcional, para saber qué comida se pulsó)
                    intent.putExtra("NOMBRE_COMIDA", nombreComida);
                    intent.putExtra(ComidasDiaActivity.EXTRA_DIA_ID, diaId);
                    intent.putExtra(ComidasDiaActivity.EXTRA_TIPO_COMIDA, tipoComida);
    private int getTempValue(String keyPrefix, int dayIndex, int fallback) {
        String key = keyPrefix + dayIndex;
        return sharedPreferences.contains(key) ? sharedPreferences.getInt(key, fallback) : fallback;
    }

    private List<MealItem> buildMealsForDay(String dayName) {
        List<MealItem> meals = new ArrayList<>();
        meals.add(new MealItem(dayName, "Desayuno", R.drawable.desayuno1));
        meals.add(new MealItem(dayName, "Comida", R.drawable.desayuno1));
        meals.add(new MealItem(dayName, "Cena", R.drawable.desayuno1));
        return meals;
    }

    private void openMealDetail(MealItem mealItem) {
        Intent intent = new Intent(MainActivity.this, ComidasDiaActivity.class);
        intent.putExtra("NOMBRE_COMIDA", mealItem.getLabel() + " " + mealItem.getDayName());
        startActivity(intent);
    }
}

