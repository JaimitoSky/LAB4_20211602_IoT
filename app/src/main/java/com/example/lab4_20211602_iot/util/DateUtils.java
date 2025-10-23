package com.example.lab4_20211602_iot.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public final class DateUtils {
    private DateUtils(){}

    public static String formatDate(long millis) {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(millis);
    }

    public static long daysBetweenCeil(long fromMs, long toMs) {
        long diff = Math.max(0, toMs - fromMs);
        long days = TimeUnit.MILLISECONDS.toDays(diff);
        return (TimeUnit.DAYS.toMillis(days) == diff) ? days : (days + 1);
    }

    public static long minus24h(long millis) {
        return millis - TimeUnit.HOURS.toMillis(24);
    }

    public static long addMonths(long millis, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        cal.add(Calendar.MONTH, months);
        return cal.getTimeInMillis();
    }

    public static long addYears(long millis, int years) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        cal.add(Calendar.YEAR, years);
        return cal.getTimeInMillis();
    }
}
