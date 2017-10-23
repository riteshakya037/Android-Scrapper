package com.calebtrevino.tallystacker.utils;

import java.util.Calendar;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @author Ritesh Shakya
 */
public class ParseUtils {
    public static long parseDate(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd hh:mm aa");
        DateTime d = formatter.parseDateTime(dateTime);
        DateTime dt = new DateTime(Calendar.getInstance().get(Calendar.YEAR), d.getMonthOfYear(),
                d.getDayOfMonth(), d.getHourOfDay(), d.getMinuteOfHour(),
                Constants.DATE.VEGAS_TIME_ZONE);
        return dt.getMillis();
    }
}
