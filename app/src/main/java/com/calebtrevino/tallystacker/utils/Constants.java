package com.calebtrevino.tallystacker.utils;

import org.joda.time.DateTimeZone;

/**
 * @author Ritesh Shakya
 */
@SuppressWarnings("unused") public class Constants {
    public static final int DATE_LAG = 0;

    private Constants() {
        throw new AssertionError();
    }

    public static class DATE {
        public static final DateTimeZone VEGAS_TIME_ZONE = DateTimeZone.forID("EST5EDT");

        private DATE() {
            throw new AssertionError();
        }
    }

    private class PREFS {
        public static final String PREFS_NAME = "tallyStacker";

        private PREFS() {
            throw new AssertionError();
        }
    }

    public class VALUES {
        public static final float SOCCER_MIN_VALUE = -25f;

        private VALUES() {
            throw new AssertionError();
        }
    }
}
