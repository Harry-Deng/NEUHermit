package com.sora.gcdr.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Locale;

public class MyUtils {

    public static long getCurrentDayMinTime(int year, int month, int day) {
        return LocalDate.of(year, month, day).atStartOfDay().with(LocalTime.MIN).toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    public static long getCurrentDayMaxTime(int year, int month, int day) {
        return LocalDate.of(year, month, day).atStartOfDay().with(LocalTime.MAX).toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public static String getTimeByLong(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);
        return sdf.format(date);
    }

    public static String getDateByLong(long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd", Locale.CHINA);
        return sdf.format(new Date(date));
    }

    public static long timeToLong(String datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm", Locale.CHINA);
        try {
            Date date = sdf.parse(datetime);
            if (date != null)
                return date.getTime();
            return 0L;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }
}
