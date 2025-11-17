package com.example.appdietas.login;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://api.nicolavirgilio.cloud";

    private static Retrofit retrofit = null;


    public static synchronized ApiService getInstance() {
        if (retrofit == null) {
            // Si la instancia no existe, la creamos
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // 1. Seteamos la URL base
                    // 2. Seteamos el convertidor (GSON)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit.create(ApiService.class);
    }



}
