package com.example.lab4_20211602_iot.notif;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.lab4_20211602_iot.data.model.ServiceReminder;
import com.example.lab4_20211602_iot.util.DateUtils;

public final class AlarmScheduler {
    private AlarmScheduler(){}

    @SuppressLint("ScheduleExactAlarm")
    public static void schedule24hBefore(Context ctx, ServiceReminder s) {
        long triggerAt = DateUtils.minus24h(s.fechaVencimientoMs);
        long now = System.currentTimeMillis();
        if (triggerAt <= now) return;

        AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pi = buildPI(ctx, s);

        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pi);
    }

    public static void cancel(Context ctx, long serviceId) {
        AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pi = buildPI(ctx, serviceId);
        am.cancel(pi);
    }

    private static PendingIntent buildPI(Context ctx, ServiceReminder s) {
        Intent i = new Intent(ctx, ReminderReceiver.class);
        i.putExtra("serviceId", s.id);
        i.putExtra("name", s.nombre);
        i.putExtra("amount", s.monto);
        i.putExtra("importance", s.importancia.name());
        return PendingIntent.getBroadcast(
                ctx, (int) s.id, i, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    private static PendingIntent buildPI(Context ctx, long id) {
        Intent i = new Intent(ctx, ReminderReceiver.class);
        i.putExtra("serviceId", id);
        return PendingIntent.getBroadcast(
                ctx, (int) id, i, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }
}
