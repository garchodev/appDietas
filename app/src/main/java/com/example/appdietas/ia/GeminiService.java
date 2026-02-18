package com.example.appdietas.ia;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.appdietas.Comida;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GeminiService {

    // RECUERDA: Pon una clave nueva porque la anterior la publicaste por error.
    private static final String API_KEY = "AIzaSyDO-gH8uXCs-5UY2FATJHDBlFuBQw0oC_w";
    private static final String URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + API_KEY;

    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface GeminiCallback {
        void onSuccess(Comida nuevaComida); // Solo un metodo limpio
        void onError(String error);
    }

    public void generarNuevaComida(String tipoComida, String contextoPrevio, GeminiCallback callback) {
        String prompt = "Actúa como un nutricionista. Sugiere una receta de " + tipoComida + ". " +
                "El usuario ya ha comido hoy: " + contextoPrevio + ". " +
                "Devuelve ÚNICAMENTE un JSON con estas claves exactas: " +
                "nombre (String), tipo (String, debe ser '" + tipoComida + "'), " +
                "descripcion (String con ingredientes), gramaje (Integer), calorias (Integer), carbohidratos (Integer), proteinas (Integer), lipidos (Integer)." +
                "imagenUri (String, obligatorio. Devuelve solo las palabras clave del plato en inglés separadas por guiones. Ejemplo: 'greek-yogurt-berries-granola')";

        String jsonBody = "{" +
                "\"contents\": [{\"parts\": [{\"text\": \"" + prompt + "\"}]}]," +
                "\"generationConfig\": {\"responseMimeType\": \"application/json\"}" +
                "}";

        RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder().url(URL).post(body).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                postError("Error de red: " + e.getMessage(), callback);
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful()) {
                    postError("Error API: " + response.code(), callback);
                    return;
                }
                try {
                    String responseData = response.body().string();

                    // AQUI ESTÁ LA SOLUCIÓN AL ERROR DE PARSESTRING:
                    JsonObject jsonObject = new JsonParser().parse(responseData).getAsJsonObject();

                    String textResponse = jsonObject
                            .getAsJsonArray("candidates").get(0).getAsJsonObject()
                            .getAsJsonObject("content")
                            .getAsJsonArray("parts").get(0).getAsJsonObject()
                            .get("text").getAsString();

                    Comida nuevaComida = gson.fromJson(textResponse, Comida.class);

                    if(nuevaComida == null || nuevaComida.getNombre() == null) {
                        postError("La IA devolvió un formato incorrecto.", callback);
                        return;
                    }

                    mainHandler.post(() -> callback.onSuccess(nuevaComida));

                } catch (Exception e) {
                    Log.e("GeminiService", "Error parseando JSON", e);
                    postError("Error procesando la respuesta de la IA.", callback);
                }
            }
        });
    }

    private void postError(String error, GeminiCallback callback) {
        mainHandler.post(() -> callback.onError(error));
    }
}