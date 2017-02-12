package com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.calebtrevino.tallystacker.controllers.factories.DefaultFactory;
import com.calebtrevino.tallystacker.controllers.sources.espn_scrappers.EspnScoreboardParser;
import com.calebtrevino.tallystacker.controllers.sources.espn_scrappers.exceptions.ExpectedElementNotFound;
import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.MLB_Total;
import com.calebtrevino.tallystacker.models.Bid;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.Team;
import com.calebtrevino.tallystacker.models.database.DatabaseContract;
import com.calebtrevino.tallystacker.models.enums.BidCondition;
import com.calebtrevino.tallystacker.models.enums.ScoreType;
import com.calebtrevino.tallystacker.utils.Constants;
import com.calebtrevino.tallystacker.utils.ParseUtils;
import com.calebtrevino.tallystacker.utils.TeamPreference;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ritesh Shakya
 */

public abstract class LeagueBase implements League {
    private static final String TAG = MLB_Total.class.getSimpleName();
    private long REFRESH_INTERVAL = DefaultFactory.League.REFRESH_INTERVAL;
    private Context context;

    @Override
    public List<Game> pullGamesFromNetwork(Context context) throws Exception {
        this.context = context;
        if (context != null) {
            Log.e(TAG, "Started " + getAcronym() + " " + getScoreType());
        }
        List<Game> updatedGameList = new LinkedList<>();
        Document parsedDocument = Jsoup.connect(getBaseUrl()).timeout(60 * 1000).get();
        storeDocument(parsedDocument);
        updatedGameList = scrapeUpdateGamesFromParsedDocument(updatedGameList, parsedDocument);
        // Only add dates that are scheduled for that date.
        List<Game> tempList = new LinkedList<>(updatedGameList);
        for (Game game : tempList) {
            if (game.getGameAddDate() != new DateTime(Constants.DATE.VEGAS_TIME_ZONE).withTimeAtStartOfDay().getMillis() || new DateTime(game.getGameDateTime(), Constants.DATE.VEGAS_TIME_ZONE).toDateTime(DateTimeZone.getDefault()).isBeforeNow()) {
                updatedGameList.remove(game);
            }
        }
        // Initiate teams for this league if not initiated
        syncDateWithEspn(updatedGameList);
        updateLibraryInDatabase(updatedGameList, context);
        return updatedGameList;
    }

    private void syncDateWithEspn(List<Game> updatedGameList) throws Exception {
        for (Game game : updatedGameList) {
            TeamPreference.getInstance(context, this).updateTeamInfo(game);
            EspnScoreboardParser.getInstance(this).setGameUrl(game);
        }
    }

    private void storeDocument(Document parsedDocument) {
        try {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/Tallystacker/" + new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
            myDir.mkdirs();
            final File f = new File(myDir, getAcronym() + "-" + getScoreType() + ".html");
            FileUtils.writeStringToFile(f, parsedDocument.select("table.frodds-data-tbl").outerHtml(), "UTF-8");
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
        }
    }

    private List<Game> scrapeUpdateGamesFromParsedDocument(List<Game> updatedGameList, Document parsedDocument) {
        Elements updatedHtmlBlocks = parsedDocument.select(getCSSQuery());
        for (Element currentHtmlBlock : updatedHtmlBlocks) {
            Game currentGame = constructGameFromHtmlBlock(currentHtmlBlock);
            updatedGameList.add(currentGame);
        }

        return updatedGameList;
    }

    private Game constructGameFromHtmlBlock(Element currentHtmlBlock) {
        Game gameFromHtmlBlock = DefaultFactory.Game.constructDefault();
        gameFromHtmlBlock.setScoreType(getScoreType());
        gameFromHtmlBlock.setLeagueType(this);
        Elements updatedHtmlBlocks = currentHtmlBlock.select("td");
        boolean once = true;
        int position = 0;
        for (Element currentColumnBlock : updatedHtmlBlocks) {
            if (once) {
                once = false;
                createGameInfo(Jsoup.parse(currentColumnBlock.html().replaceAll("(?i)<br[^>]*>", "br2n")).text(), gameFromHtmlBlock);

            } else {
                createBidInfo(Jsoup.parse(currentColumnBlock.html().replaceAll("(?i)<br[^>]*>", "br2n")).text(), gameFromHtmlBlock, position == 2);
            }
            position++;
        }
        gameFromHtmlBlock.setVI_bid();
        gameFromHtmlBlock.createID();
        return gameFromHtmlBlock;
    }

    protected void createGameInfo(String bodyText, Game gameFromHtmlBlock) {
        // Header: 09/08 8:30 PM 451 Carolina 452 Denver
        Pattern pattern = Pattern.compile("([0-9]{2}/[0-9]{2}" + // Date of match
                "\\s+" + "[0-9]{1,2}:[0-9]{2}" + "\\s+" + "[A|P]M)" + // Time of match
                "br2n " + "([0-9]{3})" + // First team code
                ".?(\\w.*)br2n " + // First team city
                "([0-9]{3})" + // Second team code
                ".?(\\w.*)"); // Second team city
        Matcher m = pattern.matcher(bodyText);
        if (m.matches()) {
            // Initialize gameFromHtmlBlock
            gameFromHtmlBlock.setGameDateTime(ParseUtils.parseDate(m.group(1)));
            gameFromHtmlBlock.setGameAddDate();

            Team firstTeam = DefaultFactory.Team.constructDefault();
            firstTeam.setLeagueType(this);
            firstTeam.set_teamId(Long.valueOf(m.group(2)));
            firstTeam.setCity(m.group(3));
            firstTeam.createID();
            gameFromHtmlBlock.setFirstTeam(firstTeam);

            Team secondTeam = DefaultFactory.Team.constructDefault();
            secondTeam.setLeagueType(this);
            secondTeam.set_teamId(Long.valueOf(m.group(4)));
            secondTeam.setCity(m.group(5));
            secondTeam.createID();
            gameFromHtmlBlock.setSecondTeam(secondTeam);
        }
    }

    protected void createBidInfo(String text, Game gameFromHtmlBlock, boolean isVI_column) {
        if (getScoreType() == ScoreType.SPREAD) {
            createBidSpread(text, gameFromHtmlBlock, isVI_column);
        } else if (getScoreType() == ScoreType.TOTAL) {
            createBidTotal(text, gameFromHtmlBlock, isVI_column);
        }
    }


    private void updateLibraryInDatabase(List<Game> updatedGameList, Context context) {
        if (context != null) {
            DatabaseContract.DbHelper dbHelper = new DatabaseContract.DbHelper(context);
            dbHelper.onInsertGame(updatedGameList);
            dbHelper.close();
        }
    }

    @Override
    public String toString() {
        return "League {" +
                "Name = \"" + getName() +
                "\" REFRESH_INTERVAL = \"" + REFRESH_INTERVAL +
                "\"}";
    }

    @Override
    public long getRefreshInterval() {
        return REFRESH_INTERVAL;
    }

    @Override
    public void setRefreshInterval(long refreshInterval) {
        this.REFRESH_INTERVAL = refreshInterval;
    }

    private void createBidTotal(String text, Game gameFromHtmlBlock, boolean isVI_column) {
        // 3 -25 41½u-10
        String[] bidBlocks = text.split("br2n");
        for (String individualBlock : bidBlocks) {
            Pattern pattern = Pattern.compile("(\\d+" + //digit before o/u
                    "[\\p{N}]?" +  // if char like ½ exists
                    ")(" +
                    "[uUoO]" + // condition to check
                    ").*");
            Matcher m = pattern.matcher(individualBlock.trim());
            if (m.matches()) {
                Bid bid = DefaultFactory.Bid.constructDefault();
                bid.setBidAmount(m.group(1));
                bid.setCondition(BidCondition.match(m.group(2)));
                bid.setVI_column(isVI_column);
                gameFromHtmlBlock.getBidList().add(bid);
            }
        }
    }

    private void createBidSpread(String text, Game gameFromHtmlBlock, boolean isVI_column) {
        // 3 -25 41½u-10
        String[] bidBlocks = text.split("br2n");
        int position = 0;
        for (String individualBlock : bidBlocks) {
            Pattern pattern = Pattern.compile("([-]?(\\d+|" + //digit before o/u
                    "[\\p{N}]|" +  // if char like ½ exists
                    "\\d+[\\p{N}])" +  // if char like ½ exists
                    ")" +
                    " " + // condition to check
                    ".*");
            Matcher m = pattern.matcher(individualBlock.trim());
            if (m.matches()) {
                Bid bid = DefaultFactory.Bid.constructDefault();
                if (position == 1) {
                    bid.setBidAmount(m.group(1), true);
                } else {
                    bid.setBidAmount(m.group(1));
                }
                bid.setCondition(BidCondition.SPREAD);
                bid.setVI_column(isVI_column);
                gameFromHtmlBlock.getBidList().add(bid);
            }
            position++;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        League league = (League) o;

        return getPackageName().equals(league.getPackageName());
    }
}
