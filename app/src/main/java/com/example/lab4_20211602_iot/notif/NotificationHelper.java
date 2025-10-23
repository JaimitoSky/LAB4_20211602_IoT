package com.example.lab4_20211602_iot.notif;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.lab4_20211602_iot.R;
import com.example.lab4_20211602_iot.data.model.Importance;

public final class NotificationHelper {

    public static final String CH_HIGH = "pay_alert_high";
    public static final String CH_DEFAULT = "pay_alert_default";
    public static final String CH_LOW = "pay_alert_low";

    private NotificationHelper(){}

    public static void ensureChannels(Context ctx) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = ctx.getSystemService(NotificationManager.class);

            NotificationChannel high = new NotificationChannel(
                    CH_HIGH, "Pagos importantes", NotificationManager.IMPORTANCE_HIGH);
            high.setDescription("Vencimientos críticos");

            NotificationChannel def = new NotificationChannel(
                    CH_DEFAULT, "Pagos normales", NotificationManager.IMPORTANCE_DEFAULT);
            def.setDescription("Vencimientos estándar");

            NotificationChannel low = new NotificationChannel(
                    CH_LOW, "Pagos de baja prioridad", NotificationManager.IMPORTANCE_LOW);
            low.setDescription("Recordatorios de baja prioridad");

            nm.createNotificationChannel(high);
            nm.createNotificationChannel(def);
            nm.createNotificationChannel(low);
        }
    }

    public static String channelOf(Importance imp) {
        if (imp == Importance.ALTA) return CH_HIGH;
        if (imp == Importance.MEDIA) return CH_DEFAULT;
        return CH_LOW;
    }

    public static Notification build(Context ctx, String channelId, String title, String text) {
        return new NotificationCompat.Builder(ctx, channelId)
                .setSmallIcon(R.mipmap.ic_launcher) // puedes cambiar a un drawable propio
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
    }
}
