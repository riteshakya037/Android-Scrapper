package com.calebtrevino.tallystacker.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;
import org.joda.time.DateTime;

/**
 * @author Ritesh Shakya
 */
public class DateUtils {
    public static String getDatePlus(String format, int relativeDate) {
        return new SimpleDateFormat(format, Locale.getDefault()).format(
                new DateTime().plusDays(relativeDate).toDate());
    }

    public static String getDateMinus(String format, int relativeDate) {
        return new SimpleDateFormat(format, Locale.getDefault()).format(
                new DateTime().minusDays(relativeDate).toDate());
    }

    public static String getDate(long gameAddDate, String format) {
        return new SimpleDateFormat(format, Locale.getDefault()).format(
                new DateTime(gameAddDate, Constants.DATE.VEGAS_TIME_ZONE).toDate());
    }
}
