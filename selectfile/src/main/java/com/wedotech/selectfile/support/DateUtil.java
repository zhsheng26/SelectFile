package com.wedotech.selectfile.support;

import android.text.format.DateFormat;

import java.util.Calendar;

public class DateUtil {

    public static String formatDatePhoto(long timeInMillis) {
        String format = (isCurrentYear(timeInMillis) ? "MM月dd日" : "yyyy年MM月dd日");
        return formatDate(format, timeInMillis);
    }

    public static boolean isCurrentYear(long timeMillis) {
        String year = formatDate("yyyy", timeMillis);
        String currentY = formatDate("yyyy", System.currentTimeMillis());
        return year.equals(currentY);
    }

    public static String formatDate(String format, long timeInMillis) {
        Calendar dealTime = Calendar.getInstance();
        dealTime.setTimeInMillis(timeInMillis);
        return DateFormat.format(format, dealTime).toString();
    }

}
