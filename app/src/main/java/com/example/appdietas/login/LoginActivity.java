package com.example.appdietas.login;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.annotation.NonNull;

import com.example.appdietas.MainActivity;
import com.example.appdietas.register.RegisterActivity;
import com.example.appdietas.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class LoginActivity extends AppCompatActivity {

    private EditText emailET;
    private EditText passwordEt;
    private Button loginButton;
    private Button btnIrARegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        emailET = findViewById(R.id.etEmail);
        passwordEt = findViewById(R.id.etPassword);
        loginButton = findViewById(R.id.btInicioSesion);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString().trim();
                String password = passwordEt.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                LoginRequest loginRequest = new LoginRequest(email, password);


                ApiService apiService = RetrofitClient.getInstance();
                Call<LoginResponse> call = apiService.login(loginRequest);

                call.enqueue(new Callback<LoginResponse>() {

                    @Override
                    public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {


                        if (response.isSuccessful() && response.body() != null) {

                            // Si las credenciales son correctas se pasa a la siguiente pantalla
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // Cerramos LoginActivity

                        } else {
                            // Error de credenciales
                            Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                        }
                    }

                    // Fallo por si no tiene conexecion a internet
                    @Override
                    public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                        // Error de red (sin internet, servidor caído, etc.)
                        Toast.makeText(LoginActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


            btnIrARegistro = findViewById(R.id.btRegister);

            btnIrARegistro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            });

    }



}