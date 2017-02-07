package com.calebtrevino.tallystacker.utils;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author Ritesh Shakya
 */
public class DateUtils {
    public static String getTodaysDate(String format) {
        return new SimpleDateFormat(format, Locale.getDefault()).format(new DateTime().minusDays(1).toDate());
    }
}
