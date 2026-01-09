package com.example.appdietas;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ComidasDiaActivity extends AppCompatActivity {

    public static final String EXTRA_DIA_ID = "EXTRA_DIA_ID";
    public static final String EXTRA_TIPO_COMIDA = "EXTRA_TIPO_COMIDA";

    private int diaId;
    private LinearLayout mealsContainer;
    private ComidasDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comidas_dia);

        diaId = getIntent().getIntExtra(EXTRA_DIA_ID, 1);

        mealsContainer = findViewById(R.id.meals_container);
        dbHelper = new ComidasDbHelper(this);
        loadMeals();

        ImageView btnBack = findViewById(R.id.btnVolver);
        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMeals();
    }

    private void loadMeals() {
        mealsContainer.removeAllViews();
        List<Comida> comidasDia = dbHelper.getComidasForDia(diaId);

        LayoutInflater inflater = LayoutInflater.from(this);
        for (Comida comida : comidasDia) {
            View mealView = inflater.inflate(
                    R.layout.item_comida,
                    mealsContainer,
                    false
            );

            ImageView imagen = mealView.findViewById(R.id.meal_image);
            TextView tipo = mealView.findViewById(R.id.meal_type);
            TextView nombre = mealView.findViewById(R.id.meal_name);
            TextView descripcion = mealView.findViewById(R.id.meal_description);
            TextView calorias = mealView.findViewById(R.id.meal_calories);
            TextView carbohidratos = mealView.findViewById(R.id.meal_carbs);
            TextView proteinas = mealView.findViewById(R.id.meal_protein);
            TextView lipidos = mealView.findViewById(R.id.meal_fats);
            Button cambiarComida = mealView.findViewById(R.id.button_cambiar_comida);

            imagen.setImageResource(comida.getImagenResId());
            tipo.setText(comida.getTipo());
            nombre.setText(comida.getNombre());
            descripcion.setText(comida.getDescripcion());
            calorias.setText(getString(R.string.calorias_format, comida.getCalorias()));
            carbohidratos.setText(getString(R.string.gramos_format, comida.getCarbohidratos()));
            proteinas.setText(getString(R.string.gramos_format, comida.getProteinas()));
            lipidos.setText(getString(R.string.gramos_format, comida.getLipidos()));

            cambiarComida.setOnClickListener(view -> {
                Intent intent = new Intent(ComidasDiaActivity.this, CambiarComidaActivity.class);
                intent.putExtra(CambiarComidaActivity.EXTRA_DIA_ID, diaId);
                intent.putExtra(CambiarComidaActivity.EXTRA_TIPO, comida.getTipo());
                startActivity(intent);
            });

            mealsContainer.addView(mealView);
        }
    }
}
