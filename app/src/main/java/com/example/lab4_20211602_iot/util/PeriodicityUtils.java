package com.example.lab4_20211602_iot.util;

import com.example.lab4_20211602_iot.data.model.Periodicity;

public final class PeriodicityUtils {
    private PeriodicityUtils(){}

    public static long nextDueMillis(Periodicity p, long currentDueMs) {
        switch (p) {
            case MENSUAL:   return DateUtils.addMonths(currentDueMs, 1);
            case BIMESTRAL: return DateUtils.addMonths(currentDueMs, 2);
            case TRIMESTRAL:return DateUtils.addMonths(currentDueMs, 3);
            case ANUAL:     return DateUtils.addYears(currentDueMs, 1);
            case UNA_VEZ:
            default:        return currentDueMs;
        }
    }
}
