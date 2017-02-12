package com.calebtrevino.tallystacker.utils;

import org.joda.time.DateTimeZone;

/**
 * @author Ritesh Shakya
 */
@SuppressWarnings("unused")
public class Constants {
    public static final int DATE_LAG = 0;

    private Constants() {
        throw new AssertionError();
    }


    private class PREFS {
        private PREFS() {
            throw new AssertionError();
        }

        public static final String PREFS_NAME = "tallyStacker";
    }

    public class VALUES {
        private VALUES() {
            throw new AssertionError();
        }

        public static final float SOCCER_MIN_VALUE = -25f;
    }

    public static class DATE {
        private DATE() {
            throw new AssertionError();
        }

        public static final DateTimeZone VEGAS_TIME_ZONE = DateTimeZone.forID("EST5EDT");
    }
}
