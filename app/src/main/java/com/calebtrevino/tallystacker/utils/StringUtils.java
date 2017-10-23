package com.calebtrevino.tallystacker.utils;

/**
 * @author Ritesh Shakya
 */
@SuppressWarnings({ "SameParameterValue", "unused" }) public class StringUtils {
    /**
     * @param field String to check
     * @return {@code true} if string is not null;{@code false} otherwise
     */
    public static boolean isNotNull(String field) {
        return !isNull(field);
    }

    /**
     * @param field String to check
     * @return {@code true} if string is null; {@code false} otherwise
     */
    @SuppressWarnings("WeakerAccess") public static boolean isNull(String field) {
        String temp = field;
        if (temp == null) {
            return true;
        } else {
            temp = field.trim();
        }

        return (temp.equalsIgnoreCase("NULL") || temp.equalsIgnoreCase("") || temp.isEmpty());
    }

    public static String right(String text, int length) {
        if (StringUtils.isNull(text) || text.length() <= length) {
            return (text);
        } else {

            return text.substring(text.length() - length, text.length());
        }
    }

    public static String repeat(String str, int count) {
        String ret = "";
        for (int i = 0; i < count; i++)
            ret += str;
        return ret;
    }

    public static int getHour(String time) {
        String[] pieces = time.split(":");
        return Integer.parseInt(pieces[0]);
    }

    public static int getMinute(String time) {
        String[] pieces = time.split(":");
        return Integer.parseInt(pieces[1]);
    }

    public static String capitalize(String text) {
        String c = (text != null) ? text.trim() : "";
        String[] words = c.split(" ");
        String result = "";
        for (String w : words) {
            result += (w.length() > 1 ? w.substring(0, 1).toUpperCase() + w.substring(1, w.length())
                    .toLowerCase() : w) + " ";
        }
        return result.trim();
    }
}
