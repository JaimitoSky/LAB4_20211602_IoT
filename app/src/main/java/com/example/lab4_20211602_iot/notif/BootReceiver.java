package com.example.lab4_20211602_iot.notif;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.lab4_20211602_iot.data.local.Repository;
import com.example.lab4_20211602_iot.data.model.ServiceReminder;

import java.util.List;

public class BootReceiver extends BroadcastReceiver {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override public void onReceive(Context ctx, Intent intent) {
        Repository repo = new Repository(ctx);
        List<ServiceReminder> all = repo.getAllServices();
        for (ServiceReminder s : all) {
            if (s.activo) {
                AlarmScheduler.schedule24hBefore(ctx, s);
            }
        }
    }
}
