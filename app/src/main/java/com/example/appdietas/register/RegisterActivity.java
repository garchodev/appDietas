package com.example.appdietas.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appdietas.R;
import com.example.appdietas.login.ApiService;
import com.example.appdietas.login.LoginActivity;
import com.example.appdietas.login.RetrofitClient;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    // 1. Declarar las vistas
    private EditText etUsername;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //  Vincular las vistas con el XML
        etUsername = findViewById(R.id.editTextUsername);
        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
        etConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        btnRegister = findViewById(R.id.buttonRegister);

        //  Configurar el listener del botón
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los datos de los campos
                String username = etUsername.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();

                // Validaciones
                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Si las validaciones son correctas, llamar a la API
                realizarRegistro(username, email, password);
            }
        });
    }

    private void realizarRegistro(String username, String email, String password) {
        // Crear el objeto de la petición
        RegisterRequest registerRequest = new RegisterRequest(username, email, password);


        ApiService apiService = RetrofitClient.getInstance();
        Call<RegisterResponse> call = apiService.register(registerRequest);

        // Ejecutar la llamada asíncrona
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(@NonNull Call<RegisterResponse> call, @NonNull Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Registro exitoso
                    Toast.makeText(RegisterActivity.this, "Registro exitoso. Ahora puedes iniciar sesión.", Toast.LENGTH_LONG).show();

                    // Devolver al usuario a la pantalla de Login
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish(); // Cerrar RegisterActivity

                } else {
                    // Error en el registro
                    Toast.makeText(RegisterActivity.this, "Error en el registro. Inténtalo de nuevo.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RegisterResponse> call, @NonNull Throwable t) {
                // Error de red
                Toast.makeText(RegisterActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}