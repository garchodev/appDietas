package com.example.appdietas;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class CambiarComidaActivity extends AppCompatActivity {

    public static final String EXTRA_DIA = "EXTRA_DIA";
    public static final String EXTRA_TIPO = "EXTRA_TIPO";

    private static final String PREFS_NAME = "mealPrefs";
    private static final String TEMP_KEY_PREFIX = "temp_meal_";

    private DatabaseHelper databaseHelper;
    private String dia;
    private String tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cambiar_comida);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseHelper = new DatabaseHelper(this);

        dia = getIntent().getStringExtra(EXTRA_DIA);
        tipo = getIntent().getStringExtra(EXTRA_TIPO);

        TextView textViewContexto = findViewById(R.id.textViewContexto);
        String contexto = buildContexto(dia, tipo);
        textViewContexto.setText(contexto);

        ImageView btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(view -> finish());

        TextInputEditText editTextNombreComida = findViewById(R.id.editTextNombreComida);
        TextInputEditText editTextDescripcion = findViewById(R.id.editTextDescripcion);
        TextInputEditText editTextGramajes = findViewById(R.id.editTextGramajes);
        TextInputEditText editTextCalorias = findViewById(R.id.editTextCalorias);
        TextInputEditText editTextGrasas = findViewById(R.id.editTextGrasas);
        TextInputEditText editTextProteinas = findViewById(R.id.editTextProteinas);
        TextInputEditText editTextCarbohidratos = findViewById(R.id.editTextCarbohidratos);

        Button btnSolicitarNuevaComida = findViewById(R.id.btnSolicitarNuevaComida);
        Button btnRegistrarComida = findViewById(R.id.btnRegistrarComida);
        Button btnGuardarCambios = findViewById(R.id.btnGuardarCambios);
        Button btnCancelar = findViewById(R.id.btnCancelar);

        btnSolicitarNuevaComida.setOnClickListener(view -> solicitarNuevaComida());

        btnRegistrarComida.setOnClickListener(view -> {
            String nombre = getTextValue(editTextNombreComida);
            String descripcion = getTextValue(editTextDescripcion);
            double gramajes = getNumericValue(editTextGramajes);
            double calorias = getNumericValue(editTextCalorias);
            double grasas = getNumericValue(editTextGrasas);
            double proteinas = getNumericValue(editTextProteinas);
            double carbohidratos = getNumericValue(editTextCarbohidratos);

            if (nombre.isEmpty()) {
                Toast.makeText(this, "Ingresa el nombre de la comida", Toast.LENGTH_SHORT).show();
                return;
            }

            if (tipo == null || tipo.isEmpty()) {
                Toast.makeText(this, "No se pudo identificar el tipo de comida", Toast.LENGTH_SHORT).show();
                return;
            }

            long comidaId = databaseHelper.insertMeal(
                    nombre,
                    descripcion,
                    gramajes,
                    grasas,
                    proteinas,
                    carbohidratos,
                    calorias,
                    tipo
            );

            if (comidaId == -1) {
                Toast.makeText(this, "No se pudo registrar la comida", Toast.LENGTH_SHORT).show();
                return;
            }

            guardarSeleccionTemporal((int) comidaId);
            Toast.makeText(this, "Comida registrada y seleccionada", Toast.LENGTH_SHORT).show();
        });

        btnGuardarCambios.setOnClickListener(view -> guardarCambios());
        btnCancelar.setOnClickListener(view -> cancelarCambios());
    }

    private void solicitarNuevaComida() {
        if (tipo == null || tipo.isEmpty()) {
            Toast.makeText(this, "No se pudo identificar el tipo de comida", Toast.LENGTH_SHORT).show();
            return;
        }

        int comidaId = databaseHelper.getRandomMealIdByTipo(tipo);
        if (comidaId == -1) {
            Toast.makeText(this, "No hay comidas disponibles para este tipo", Toast.LENGTH_SHORT).show();
            return;
        }

        guardarSeleccionTemporal(comidaId);
        Toast.makeText(this, "Se ha seleccionado una nueva comida", Toast.LENGTH_SHORT).show();
    }

    private void guardarSeleccionTemporal(int comidaId) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt(getTempKey(), comidaId);
        editor.apply();
    }

    private void guardarCambios() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (!prefs.contains(getTempKey())) {
            Toast.makeText(this, "No hay cambios para guardar", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        int comidaId = prefs.getInt(getTempKey(), -1);
        if (comidaId == -1 || dia == null || dia.isEmpty() || tipo == null || tipo.isEmpty()) {
            Toast.makeText(this, "No se pudo guardar la selección", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseHelper.upsertComidaDia(dia, tipo, comidaId);
        prefs.edit().remove(getTempKey()).apply();
        Toast.makeText(this, "Cambios guardados", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void cancelarCambios() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit().remove(getTempKey()).apply();
        Toast.makeText(this, "Cambios cancelados", Toast.LENGTH_SHORT).show();
        finish();
    }

    private String buildContexto(String dia, String tipo) {
        if (dia == null && tipo == null) {
            return "Comida del día";
        }
        if (dia == null) {
            return tipo;
        }
        if (tipo == null) {
            return dia;
        }
        return tipo + " · " + dia;
    }

    private String getTempKey() {
        return TEMP_KEY_PREFIX + (dia == null ? "" : dia) + "_" + (tipo == null ? "" : tipo);
    }

    private String getTextValue(TextInputEditText editText) {
        if (editText.getText() == null) {
            return "";
        }
        return editText.getText().toString().trim();
    }

    private double getNumericValue(TextInputEditText editText) {
        String value = getTextValue(editText);
        if (value.isEmpty()) {
            return 0;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
