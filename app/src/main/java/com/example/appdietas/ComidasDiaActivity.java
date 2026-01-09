package com.example.appdietas;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.content.Intent;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

public class ComidasDiaActivity extends AppCompatActivity {

    private LinearLayout headerIngredientes;
    private LinearLayout contentIngredientes;

    private LinearLayout headerInstrucciones;
    private LinearLayout contentInstrucciones;

    private ImageView arrowIngredientes;
    private ImageView arrowInstrucciones;

    private boolean ingredientesExpanded = false;
    private boolean instruccionesExpanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comidas_dia);

        // Referencias de ingredientes
        headerIngredientes = findViewById(R.id.header_ingredientes);
        contentIngredientes = findViewById(R.id.content_ingredientes);
        arrowIngredientes = findViewById(R.id.arrow_ingredientes);

        // Referencias de instrucciones
        headerInstrucciones = findViewById(R.id.header_instrucciones);
        contentInstrucciones = findViewById(R.id.content_instrucciones);
        arrowInstrucciones = findViewById(R.id.arrow_instrucciones); // AGREGA este ImageView al layout XML

        String nombreComida = getIntent().getStringExtra("NOMBRE_COMIDA");
        String[] partes = nombreComida == null ? new String[0] : nombreComida.split(" ", 2);
        String tipo = partes.length > 0 ? partes[0] : null;
        String dia = partes.length > 1 ? partes[1] : null;

        Button botonCambiar = findViewById(R.id.button);

        botonCambiar.setOnClickListener(view -> {
            Intent intent = new Intent(ComidasDiaActivity.this, CambiarComidaActivity.class);
            intent.putExtra(CambiarComidaActivity.EXTRA_DIA, dia);
            intent.putExtra(CambiarComidaActivity.EXTRA_TIPO, tipo);
            startActivity(intent);
        });

        ImageView btnBack = findViewById(R.id.btnVolver);
        btnBack.setOnClickListener(v -> finish());


        // Ocultar contenidos al inicio
        contentIngredientes.setVisibility(View.GONE);
        contentInstrucciones.setVisibility(View.GONE);

        // Listener para ingredientes
        headerIngredientes.setOnClickListener(v -> toggleIngredientes());

        // Listener para instrucciones
        headerInstrucciones.setOnClickListener(v -> toggleInstrucciones());
    }

    private void toggleIngredientes() {
        if (ingredientesExpanded) {
            contentIngredientes.setVisibility(View.GONE);
            arrowIngredientes.setRotation(0);
        } else {
            contentIngredientes.setVisibility(View.VISIBLE);
            arrowIngredientes.setRotation(180);
        }
        ingredientesExpanded = !ingredientesExpanded;
    }

    private void toggleInstrucciones() {
        if (instruccionesExpanded) {
            contentInstrucciones.setVisibility(View.GONE);
            arrowInstrucciones.setRotation(0);
        } else {
            contentInstrucciones.setVisibility(View.VISIBLE);
            arrowInstrucciones.setRotation(180);
        }
        instruccionesExpanded = !instruccionesExpanded;
    }
}
