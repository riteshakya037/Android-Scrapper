package com.calebtrevino.tallystacker;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.Soccer_Total;
import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases.League;
import com.calebtrevino.tallystacker.models.Game;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTimeZone;
import org.joda.time.tz.UTCProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@SuppressWarnings("ALL")
@RunWith(AndroidJUnit4.class)
public class InstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        JodaTimeAndroid.init(InstrumentationRegistry.getTargetContext());

        League league = new Soccer_Total();
        DateTimeZone.setProvider(new UTCProvider());

        for (Game game : league.pullGamesFromNetwork(null, true)) {
            System.out.println(game.getFirstTeam().getCity() + ", " + game.getSecondTeam().getCity());
        }
    }
}
