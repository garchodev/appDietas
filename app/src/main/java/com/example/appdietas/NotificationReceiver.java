package com.example.appdietas;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "dieta_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        String tipoComida = intent.getStringExtra("TIPO_COMIDA");
        if (tipoComida == null) tipoComida = "Comida de prueba";

        // Imprimimos en la consola para saber que la alarma ha despertado
        Log.d("ALARM_TEST", "¡La alarma de " + tipoComida + " ha saltado!");

        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int diaId = (dayOfWeek == Calendar.SUNDAY) ? 7 : dayOfWeek - 1;

        ComidasDbHelper dbHelper = new ComidasDbHelper(context);
        Comida comidaDeHoy = dbHelper.getMealForDayTipo(diaId, tipoComida);

        // Textos por defecto por si la base de datos devuelve null
        String titulo = "🍽️ ¡Hora de tu " + tipoComida + "!";
        String textoCuerpo = "No tienes nada planificado. ¡Abre la app!";

        if (comidaDeHoy != null) {
            textoCuerpo = "Hoy toca: " + comidaDeHoy.getNombre() + " (" + comidaDeHoy.getCalorias() + " kcal)";
        }

        mostrarNotificacion(context, tipoComida, titulo, textoCuerpo);
    }

    private void mostrarNotificacion(Context context, String tipo, String titulo, String texto) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "Avisos de Comidas", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, tipo.hashCode(), i, PendingIntent.FLAG_IMMUTABLE);

        // Usamos un icono por defecto del sistema android para evitar cuelgues por iconos no compatibles
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(titulo)
                .setContentText(texto)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        notificationManager.notify(tipo.hashCode(), builder.build());
    }
}