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

import com.example.appdietas.ia.GeminiService;
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
        TextInputEditText editTextGramajes = findViewById(R.id.editTextGramajes);


        Button btnSolicitarNuevaComida = findViewById(R.id.btnSolicitarNuevaComida);
        Button btnRegistrarComida = findViewById(R.id.btnRegistrarComida);
        Button btnGuardarCambios = findViewById(R.id.btnGuardarCambios);
        Button btnCancelar = findViewById(R.id.btnCancelar);

        btnSolicitarNuevaComida.setOnClickListener(view -> solicitarNuevaComida());

        btnRegistrarComida.setOnClickListener(view -> {
            String nombre = getTextValue(editTextNombreComida);
            String descripcion = getTextValue(editTextDescripcion);
            double gramaje = getNumericValue(editTextGramajes);
            double calorias = getNumericValue(editTextCalorias);
            double grasas = getNumericValue(editTextGrasas);
            double proteinas = getNumericValue(editTextProteinas);
            double carbohidratos = getNumericValue(editTextCarbohidratos);
            String urlExistente = (selectedMeal != null) ? selectedMeal.getImagenUri() : "";

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
                    (int) Math.round(gramaje),
                    (int) Math.round(calorias),
                    (int) Math.round(carbohidratos),
                    (int) Math.round(proteinas),
                    (int) Math.round(grasas),
                    R.drawable.imagencomida,
                    urlExistente
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

        Toast.makeText(this, "Consultando a la IA...", Toast.LENGTH_SHORT).show();

        // 1. Instanciamos nuestro servicio
        GeminiService geminiService = new GeminiService();

        // 2. Construimos el contexto
        String contexto = "Día " + diaId;

        // 3. Llamada a la IA
        geminiService.generarNuevaComida(tipo, contexto, new GeminiService.GeminiCallback() {

            @Override
            public void onSuccess(Comida nuevaComidaIA) {
                // Buscamos todas las vistas de la interfaz
                TextInputEditText editTextNombreComida = findViewById(R.id.editTextNombreComida);
                TextInputEditText editTextDescripcion = findViewById(R.id.editTextDescripcion);
                TextInputEditText editTextCalorias = findViewById(R.id.editTextCalorias);
                TextInputEditText editTextGrasas = findViewById(R.id.editTextGrasas);
                TextInputEditText editTextProteinas = findViewById(R.id.editTextProteinas);
                TextInputEditText editTextCarbohidratos = findViewById(R.id.editTextCarbohidratos);

                // Añadimos la vista del gramaje (Asegúrate de que este ID coincida con tu XML)
                TextInputEditText editTextGramajes = findViewById(R.id.editTextGramajes);

                // Rellenamos los campos con la respuesta de la IA
                editTextNombreComida.setText(nuevaComidaIA.getNombre());
                editTextDescripcion.setText(nuevaComidaIA.getDescripcion());
                editTextCalorias.setText(String.valueOf(nuevaComidaIA.getCalorias()));
                editTextGrasas.setText(String.valueOf(nuevaComidaIA.getLipidos()));
                editTextProteinas.setText(String.valueOf(nuevaComidaIA.getProteinas()));
                editTextCarbohidratos.setText(String.valueOf(nuevaComidaIA.getCarbohidratos()));
                editTextGramajes.setText(String.valueOf(nuevaComidaIA.getGramaje()));


                // Guardamos la referencia para cuando el usuario pulse "Registrar"
                selectedMeal = nuevaComidaIA;
                // Si quieres comprobarlo, añade este log:
                android.util.Log.d("IA_DEBUG", "URL recibida: " + nuevaComidaIA.getImagenUri());

                Toast.makeText(CambiarComidaActivity.this, "¡Comida generada por IA!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(CambiarComidaActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
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
