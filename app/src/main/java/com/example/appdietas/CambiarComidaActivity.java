package com.example.appdietas;

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

    public static final String EXTRA_DIA_ID = "EXTRA_DIA_ID";
    public static final String EXTRA_TIPO = "EXTRA_TIPO";

    private ComidasDbHelper comidasDbHelper;
    private int diaId;
    private String tipo;
    private Comida selectedMeal;

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

        comidasDbHelper = new ComidasDbHelper(this);

        diaId = getIntent().getIntExtra(EXTRA_DIA_ID, -1);
        tipo = getIntent().getStringExtra(EXTRA_TIPO);

        TextView textViewContexto = findViewById(R.id.textViewContexto);
        String contexto = buildContexto(diaId, tipo);
        textViewContexto.setText(contexto);

        ImageView btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(view -> finish());

        TextInputEditText editTextNombreComida = findViewById(R.id.editTextNombreComida);
        TextInputEditText editTextDescripcion = findViewById(R.id.editTextDescripcion);
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
            double calorias = getNumericValue(editTextCalorias);
            double grasas = getNumericValue(editTextGrasas);
            double proteinas = getNumericValue(editTextProteinas);
            double carbohidratos = getNumericValue(editTextCarbohidratos);

            if (nombre.isEmpty()) {
                Toast.makeText(this, "Ingresa el nombre de la comida", Toast.LENGTH_SHORT).show();
                return;
            }

            if (tipo == null || tipo.isEmpty() || diaId == -1) {
                Toast.makeText(this, "No se pudo identificar la comida", Toast.LENGTH_SHORT).show();
                return;
            }

            selectedMeal = new Comida(
                    diaId,
                    tipo,
                    nombre,
                    descripcion,
                    (int) Math.round(calorias),
                    (int) Math.round(carbohidratos),
                    (int) Math.round(proteinas),
                    (int) Math.round(grasas),
                    R.drawable.imagencomida
            );
            Toast.makeText(this, "Comida registrada y lista para guardar", Toast.LENGTH_SHORT).show();
        });

        btnGuardarCambios.setOnClickListener(view -> guardarCambios());
        btnCancelar.setOnClickListener(view -> cancelarCambios());
    }

    private void solicitarNuevaComida() {
        if (tipo == null || tipo.isEmpty() || diaId == -1) {
            Toast.makeText(this, "No se pudo identificar la comida", Toast.LENGTH_SHORT).show();
            return;
        }

        Comida randomMeal = comidasDbHelper.getRandomMealByTipo(tipo);
        if (randomMeal == null) {
            Toast.makeText(this, "No hay comidas disponibles para este tipo", Toast.LENGTH_SHORT).show();
            return;
        }

        selectedMeal = new Comida(
                diaId,
                tipo,
                randomMeal.getNombre(),
                randomMeal.getDescripcion(),
                randomMeal.getCalorias(),
                randomMeal.getCarbohidratos(),
                randomMeal.getProteinas(),
                randomMeal.getLipidos(),
                randomMeal.getImagenResId()
        );
        Toast.makeText(this, "Se ha seleccionado una nueva comida", Toast.LENGTH_SHORT).show();
    }

    private void guardarCambios() {
        if (selectedMeal == null) {
            Toast.makeText(this, "No hay cambios para guardar", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (tipo == null || tipo.isEmpty() || diaId == -1) {
            Toast.makeText(this, "No se pudo guardar la selección", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean updated = comidasDbHelper.updateMealForDayTipo(diaId, tipo, selectedMeal);
        if (updated) {
            Toast.makeText(this, "Cambios guardados", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No se pudo guardar la selección", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private void cancelarCambios() {
        selectedMeal = null;
        Toast.makeText(this, "Cambios cancelados", Toast.LENGTH_SHORT).show();
        finish();
    }

    private String buildContexto(int diaId, String tipo) {
        if (diaId == -1 && tipo == null) {
            return "Comida del día";
        }
        if (diaId == -1) {
            return tipo;
        }
        String diaTexto = "Día " + diaId;
        if (tipo == null) {
            return diaTexto;
        }
        return tipo + " · " + diaTexto;
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
