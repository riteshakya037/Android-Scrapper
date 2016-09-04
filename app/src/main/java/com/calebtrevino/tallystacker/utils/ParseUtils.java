package com.calebtrevino.tallystacker.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by fatal on 9/4/2016.
 */
public class ParseUtils {
    public static long parseDate(String string_date, String string_time, String date_format, String time_format) {
        SimpleDateFormat f = new SimpleDateFormat(date_format + " yyyy " + time_format, Locale.US);
        Date d;
        try {
            d = f.parse(string_date + " " + String.valueOf(Calendar.getInstance().get(Calendar.YEAR) + " " + string_time));

        } catch (ParseException e) {
            e.printStackTrace();
            return 0L;
        }
        return d.getTime();
    }
}
