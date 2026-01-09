package com.example.appdietas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

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

        String[] diasSemana = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};

        String[] tiposComida = {"Desayuno", "Comida", "Cena"};

        for (int i = 1; i <= 7; i++) {

            for (String tipo : tiposComida) {

                String nombreID = "card" + tipo + i;

                int resID = getResources().getIdentifier(nombreID, "id", getPackageName());

                String textoParaEnviar = tipo + " " + diasSemana[i-1];

                if (resID != 0) {
                    configurarClick(resID, textoParaEnviar, tipo, i);
                }
            }
        }
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
}

