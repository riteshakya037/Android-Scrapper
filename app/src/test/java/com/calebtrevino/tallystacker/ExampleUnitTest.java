package com.calebtrevino.tallystacker;

import com.calebtrevino.tallystacker.controllers.factories.DefaultFactory;
import com.calebtrevino.tallystacker.controllers.sources.NCAA_FB_Spread;
import com.calebtrevino.tallystacker.controllers.sources.Soccer_Spread;
import com.calebtrevino.tallystacker.controllers.sources.bases.League;
import com.calebtrevino.tallystacker.models.GridLeagues;
import com.calebtrevino.tallystacker.models.Team;
import com.calebtrevino.tallystacker.models.enums.BidCondition;
import com.calebtrevino.tallystacker.utils.ParseUtils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.tz.UTCProvider;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static junit.framework.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void team_info() throws Exception {
        String bodyText = "09/04 1:10 PM 901 St. Louis 902 Cincinnati";
        Pattern pattern = Pattern.compile("([0-9]{2}/[0-9]{2})" + "\\s+" + "([0-9]{1,2}:[0-9]{2}" + "\\s+" + "[A|P]M)" + "\\s+" + "([0-9]{3})" + ".?(\\w.*)" + "([0-9]{3})" + ".?(\\w.*)");
        Matcher m = pattern.matcher(bodyText);
        if (m.matches()) {
            System.out.println("m.group(1) = " + m.group(1));
            System.out.println("m.group(1) = " + m.group(2));
            System.out.println("m.group(1) = " + m.group(3));
            System.out.println("m.group(1) = " + m.group(4));
            System.out.println("m.group(1) = " + m.group(5));
            System.out.println("m.group(1) = " + m.group(6));
        }
    }

    @Test
    public void bidInfo() throws Exception {
        String text = "162½u-05 " +
                "-1 -05";
        Pattern pattern = Pattern.compile(".*(\\d+[\\p{N}]?)([uUoO]).*");
        Matcher m = pattern.matcher(text);
        if (m.matches()) {
            System.out.println("m.group(1) = " + m.group(1));
            System.out.println("m.group(1) = " + BidCondition.match(m.group(2)));
        }

    }

    @Test
    public void total_check() throws Exception {
        String bodyText = "162½u-05 " +
                "-1 -05";
        assertEquals(true, bodyText.matches(".*[\\d]+.*?[o|O|u|U][+|-]?[\\d]+.*"));
    }

    @Test
    public void dateParser() throws Exception {
        String date = "09/08";
        String time = "8:30 PM";
        System.out.println("date = " + new Date(ParseUtils.parseDate(date, time, "MM/dd", "hh:mm aa")));
    }

    @Test
    public void packageTest() throws Exception {
        League league = new NCAA_FB_Spread();
        DateTimeZone.setProvider(new UTCProvider());
        System.out.println("Class = " + league.pullGamesFromNetwork(null));
    }


    @Test
    public void JsonTest() throws Exception {
        List<GridLeagues> bidList = new LinkedList<>();
        bidList.add(DefaultFactory.GridLeagues.constructDefault());
        bidList.add(DefaultFactory.GridLeagues.constructDefault());
        bidList.add(DefaultFactory.GridLeagues.constructDefault());
        bidList.add(DefaultFactory.GridLeagues.constructDefault());
        bidList = bidList.subList(0, 2);
        System.out.println(bidList);

    }

    @Test
    public void SpaceTrim() throws Exception {
        String s = " Philadelphia Tat";
        System.out.println("s = " + s.trim().replaceAll(" ", "") + "s");

    }

    @Test
    public void dateTest() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        System.out.println(new Date(cal.getTimeInMillis()));
        DateTime dateTime = new DateTime();

        System.out.println("dateTime.withTimeAtStartOfDay().getMillis() = " + new Date(dateTime.minusDays(1).withTimeAtStartOfDay().getMillis()));
    }

    @Test
    public void checkTime() throws Exception {
        Date date = new Date(1476728100000L);
        System.out.println(date);

    }
}