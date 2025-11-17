package com.example.appdietas.login;

import com.example.appdietas.register.RegisterRequest;
import com.example.appdietas.register.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/login")
    Call<LoginResponse> login(@Body LoginRequest request);

     @POST("/register")
     Call<RegisterResponse> register(@Body RegisterRequest request);

}
