package com.example.lab4_20211602_iot.notif;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.lab4_20211602_iot.data.model.Importance;

public class ReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context ctx, Intent intent) {
        long id = intent.getLongExtra("serviceId", 0L);
        String name = intent.getStringExtra("name");
        double amount = intent.getDoubleExtra("amount", 0.0);
        String impStr = intent.getStringExtra("importance");

        NotificationHelper.ensureChannels(ctx);

        Importance imp;
        try {
            imp = Importance.valueOf(impStr != null ? impStr : Importance.MEDIA.name());
        } catch (IllegalArgumentException ex) {
            imp = Importance.MEDIA;
        }
        String channelId = NotificationHelper.channelOf(imp);

        String title = "Vence mañana: " + (name != null ? name : "Servicio");
        String text = "Monto: S/. " + String.format(java.util.Locale.US, "%.2f", amount);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            int ok = ContextCompat.checkSelfPermission(ctx, Manifest.permission.POST_NOTIFICATIONS);
            if (ok != PackageManager.PERMISSION_GRANTED) {
                return; // Sin permiso, no se puede notificar desde un receiver
            }
        }

        NotificationManagerCompat nmc = NotificationManagerCompat.from(ctx);
        if (!nmc.areNotificationsEnabled()) {
            return;
        }

        nmc.notify(
                (int) id,
                NotificationHelper.build(ctx, channelId, title, text)
        );
    }
}
