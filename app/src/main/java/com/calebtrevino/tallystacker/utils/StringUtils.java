package com.calebtrevino.tallystacker.utils;

/**
 * Created by fatal on 9/4/2016.
 */
public class StringUtils {
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
    public static boolean isNull(String field) {
        if (field == null)
            return true;
        else
            field = field.trim();

        return (field.equalsIgnoreCase("NULL") || field.equalsIgnoreCase("") || field.isEmpty());
    }
}
