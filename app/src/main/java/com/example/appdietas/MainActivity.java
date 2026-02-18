package com.example.appdietas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_TEMP = "temp_meal_state";
    private static final String KEY_TEMP_CALORIES = "temp_calories_day_";
    private static final String KEY_TEMP_PROTEIN = "temp_protein_day_";
    private static final String KEY_TEMP_CARBS = "temp_carbs_day_";
    private static final String KEY_TEMP_FAT = "temp_fat_day_";

    private DayRepository dayRepository;
    private ComidasDbHelper comidasDbHelper;
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
        comidasDbHelper = new ComidasDbHelper(this);
        sharedPreferences = getSharedPreferences(PREFS_TEMP, MODE_PRIVATE);

        dayRecyclerView = findViewById(R.id.dayRecyclerView);
        dayRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dayRecyclerView.setHasFixedSize(true);

        solicitarPermisosNotificacion();
        configurarAlarmasDiarias();
        // programarAlarmaPrueba();
        loadDays();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cada vez que volvamos a esta pantalla (por ejemplo, después de cambiar una comida),
        // volvemos a leer la base de datos y refrescamos la suma de macronutrientes.
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

    private void loadDays() {
        List<DaySummary> summaries = dayRepository.getAllSummaries();
        List<DayItem> dayItems = new ArrayList<>();
        for (DaySummary summary : summaries) {
            DaySummary resolvedSummary = applyTemporaryOverrides(summary);
            dayItems.add(new DayItem(resolvedSummary, buildMealsForDay(resolvedSummary)));
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
                    startActivity(intent);
                }
            });
        }
    }

    private int getTempValue(String keyPrefix, int dayIndex, int fallback) {
        String key = keyPrefix + dayIndex;
        return sharedPreferences.contains(key) ? sharedPreferences.getInt(key, fallback) : fallback;
    }

    private List<MealItem> buildMealsForDay(DaySummary summary) {
        List<MealItem> meals = new ArrayList<>();

        // ¡ESTE ES EL CAMBIO CLAVE!
        // Usamos comidasDbHelper en lugar de dayRepository
        List<Comida> comidasReales = comidasDbHelper.getComidasForDia(summary.getDayIndex());

        for (Comida c : comidasReales) {
            meals.add(new MealItem(
                    summary.getDayIndex(),
                    summary.getDayName(),
                    c.getTipo(),
                    c.getImagenResId(),
                    c                    // El objeto Comida ahora trae las calorías de comidas.db
            ));
        }
        return meals;
    }

    private void openMealDetail(MealItem mealItem) {
        Intent intent = new Intent(MainActivity.this, ComidasDiaActivity.class);
        intent.putExtra("NOMBRE_COMIDA", mealItem.getLabel() + " " + mealItem.getDayName());
        intent.putExtra(ComidasDiaActivity.EXTRA_DIA_ID, mealItem.getDayIndex());
        intent.putExtra(ComidasDiaActivity.EXTRA_TIPO_COMIDA, mealItem.getLabel());
        startActivity(intent);
    }

    private void solicitarPermisosNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
    }

    private void configurarAlarmasDiarias() {
        // Horarios obligatorios de tu enunciado
        programarAlarma("Desayuno", 6, 0);
        programarAlarma("Comida", 12, 0);
        programarAlarma("Cena", 18, 0);
    }

    private void programarAlarma(String tipoComida, int hora, int minuto) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("TIPO_COMIDA", tipoComida);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, tipoComida.hashCode(), intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hora);
        calendar.set(Calendar.MINUTE, minuto);
        calendar.set(Calendar.SECOND, 0);

        // Si la hora ya ha pasado hoy, programarla para mañana a la misma hora
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        if (alarmManager != null) {
            // Repetir cada 24 horas exactas
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
            );
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private void programarAlarmaPrueba() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("TIPO_COMIDA", "Prueba Inmediata");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, 999, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        // Programar para dentro de 10 segundos (10000 milisegundos)
        long triggerTime = System.currentTimeMillis() + 10000;

        if (alarmManager != null) {
            // setExact obliga al móvil a no retrasar la alarma
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        }
    }
}
