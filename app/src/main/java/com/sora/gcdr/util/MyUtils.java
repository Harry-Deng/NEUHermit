package com.sora.gcdr.util;

import android.icu.text.SimpleDateFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Locale;

public class MyUtils {

    public static long getCurrentDayMinTime(int year,int month,int day) {
        return LocalDate.of(year,month,day).atStartOfDay().with(LocalTime.MIN).toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    public static long getCurrentDayMaxTime(int year,int month,int day) {
        return LocalDate.of(year,month,day).atStartOfDay().with(LocalTime.MAX).toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public static String getTimeByLong(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);
        return sdf.format(date);
    }
}
