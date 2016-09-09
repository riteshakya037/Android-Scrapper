package com.calebtrevino.tallystacker;

import com.calebtrevino.tallystacker.controllers.factories.DefaultFactory;
import com.calebtrevino.tallystacker.controllers.sources.League;
import com.calebtrevino.tallystacker.controllers.sources.ProBaseball;
import com.calebtrevino.tallystacker.models.GridLeagues;
import com.calebtrevino.tallystacker.models.Team;
import com.calebtrevino.tallystacker.models.enums.BidCondition;
import com.calebtrevino.tallystacker.utils.ParseUtils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
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
        Pattern pattern = Pattern.compile("([0-9]{2}/[0-9]{2})" + "\\s+" + "([0-9]{1,2}:[0-9]{2}" + "\\s+" + "[A|P]M)" + "\\s+" + "([0-9]{3})" + "(.*)" + "([0-9]{3})" + "(.*)");
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
        String text = "8½o-25 -158 +148";
        Pattern pattern = Pattern.compile(".*(\\d+[\\p{N}]?)([u|U|o|O]).*");
        Matcher m = pattern.matcher(text);
        if (m.matches()) {
            System.out.println("m.group(1) = " + m.group(1));
            System.out.println("m.group(1) = " + BidCondition.match(m.group(2)));
        }

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

    @Test
    public void packageTest() throws Exception {
        League league = new ProBaseball();
        System.out.println("Package =  " + league.getPackageName());
        System.out.println("Class = " + Class.forName(league.getPackageName()).newInstance().toString());
    }


    @Test
    public void JsonTest() throws Exception {
        List<GridLeagues> bidList = new ArrayList<>();
        bidList.add(DefaultFactory.GridLeagues.constructDefault());
        bidList.add(DefaultFactory.GridLeagues.constructDefault());
        bidList.add(DefaultFactory.GridLeagues.constructDefault());
        bidList.add(DefaultFactory.GridLeagues.constructDefault());

        System.out.println(Team.getFromJson(DefaultFactory.Team.constructDefault().toJSON()));

    }

    @Test
    public void SpaceTrim() throws Exception {
        String s = " Philadelphia Tat";
        System.out.println("s = " + s.trim().replaceAll(" ", "") + "s");

    }
}