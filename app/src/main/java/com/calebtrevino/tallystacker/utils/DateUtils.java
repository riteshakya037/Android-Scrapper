package com.calebtrevino.tallystacker.utils;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author Ritesh Shakya
 */
public class DateUtils {
    public static String getDatePlus(String format, int relativeDate) {
        return new SimpleDateFormat(format, Locale.getDefault()).format(new DateTime().plusDays(relativeDate).toDate());
    }
}
