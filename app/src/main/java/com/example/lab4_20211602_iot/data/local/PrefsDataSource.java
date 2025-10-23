package com.example.lab4_20211602_iot.data.local;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefsDataSource {
    private final SharedPreferences prefs;

    public PrefsDataSource(Context ctx) {
        this.prefs = ctx.getSharedPreferences(PrefsKeys.PREFS_FILE, Context.MODE_PRIVATE);
    }

    public String getString(String key) {
        return prefs.getString(key, "");
    }

    public void putString(String key, String value) {
        prefs.edit().putString(key, value == null ? "" : value).apply();
    }

    public long getLong(String key, long def) {
        return prefs.getLong(key, def);
    }

    public void putLong(String key, long value) {
        prefs.edit().putLong(key, value).apply();
    }
}
