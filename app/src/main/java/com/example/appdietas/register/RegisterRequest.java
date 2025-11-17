package com.example.appdietas.register;

public class RegisterRequest {

    private final String username;
    private final String email;
    private final String contrasena;

    public RegisterRequest(String username, String email, String contrasena) {
        this.username = username;
        this.email = email;
        this.contrasena = contrasena;
    }
}
