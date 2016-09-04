package com.calebtrevino.tallystacker;

import com.calebtrevino.tallystacker.utils.ParseUtils;

import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void team_info() throws Exception {
        String bodyText = "09/08 8:30 PM 451 Carolina 452 Denver";
        assertEquals(true, bodyText.matches("[0-9]{2}/[0-9]{2}" + "\\s+" + "[0-9]{1,2}:[0-9]{2}" + "\\s+" + "[A|P]M" + "\\s+" + "[0-9]{3}" + "\\s+" + ".*"));
    }

    @Test
    public void total_check() throws Exception {
        String bodyText = "-2 -10 41½U-10";
        assertEquals(true, bodyText.matches(".*[\\d]+.*?[o|O|u|U][+|-]?[\\d]+.*"));
    }

    @Test
    public void dateParser() throws Exception {
        String date = "09/08";
        String time = "8:30 PM";
        System.out.println("date = " + new Date(ParseUtils.parseDate(date, time, "MM/dd", "hh:mm aa")));
    }
}