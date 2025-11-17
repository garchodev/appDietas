package com.example.appdietas.login;

public class LoginRequest {


    private final String email;
    private final String contrasena;

    // Constructor para crear el objeto fácilmente
    public LoginRequest(String email, String contrasena) {
        this.email = email;
        this.contrasena = contrasena;
    }

}
