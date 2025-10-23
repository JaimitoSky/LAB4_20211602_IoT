package com.example.lab4_20211602_iot.util;

import com.example.lab4_20211602_iot.data.local.PrefsDataSource;
import com.example.lab4_20211602_iot.data.local.PrefsKeys;

public class IdGenerator {
    private final PrefsDataSource prefs;

    public IdGenerator(PrefsDataSource prefs) { this.prefs = prefs; }

    public long nextServiceId() {
        long next = prefs.getLong(PrefsKeys.LAST_ID, 0L) + 1L;
        prefs.putLong(PrefsKeys.LAST_ID, next);
        return next;
    }

    public long nextHistoryId() {
        long next = prefs.getLong(PrefsKeys.LAST_HISTORY_ID, 0L) + 1L;
        prefs.putLong(PrefsKeys.LAST_HISTORY_ID, next);
        return next;
    }
}
