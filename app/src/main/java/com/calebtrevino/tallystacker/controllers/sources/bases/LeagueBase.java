package com.calebtrevino.tallystacker.controllers.sources.bases;

import android.content.Context;
import android.util.Log;

import com.calebtrevino.tallystacker.controllers.factories.DefaultFactory;
import com.calebtrevino.tallystacker.controllers.sources.MLB_Total;
import com.calebtrevino.tallystacker.models.Bid;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.database.DatabaseContract;
import com.calebtrevino.tallystacker.models.enums.BidCondition;
import com.calebtrevino.tallystacker.models.enums.ScoreType;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ritesh Shakya
 */

public abstract class LeagueBase implements League {
    private static final String TAG = MLB_Total.class.getSimpleName();
    private long REFRESH_INTERVAL = DefaultFactory.League.REFRESH_INTERVAL;

    @Override
    public List<Game> pullGamesFromNetwork(Context context) throws Exception {
        if (context != null) {
            Log.e(TAG, "Started " + getAcronym() + " " + getScoreType());
        }
        List<Game> updatedGameList = new LinkedList<>();
        Document parsedDocument = null;
        try {
            parsedDocument = Jsoup.connect(getBaseUrl()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        updatedGameList = scrapeUpdateGamesFromParsedDocument(updatedGameList, parsedDocument);

        updateLibraryInDatabase(updatedGameList, context);
        return updatedGameList;
    }

    private List<Game> scrapeUpdateGamesFromParsedDocument(List<Game> updatedGameList, Document parsedDocument) throws Exception {
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
        for (Element currentColumnBlock : updatedHtmlBlocks) {
            if (once) {
                once = false;
                createGameInfo(currentColumnBlock.text(), gameFromHtmlBlock);

            } else {
                createBidInfo(Jsoup.parse(currentColumnBlock.html().replaceAll("(?i)<br[^>]*>", "br2n")).text().replaceAll("br2n", "\n"), gameFromHtmlBlock);
            }
        }
        gameFromHtmlBlock.createID();
        return gameFromHtmlBlock;
    }

    protected abstract void createGameInfo(String text, Game gameFromHtmlBlock);

    private void createBidInfo(String text, Game gameFromHtmlBlock) {
        if (getScoreType() == ScoreType.SPREAD) {
            createBidSpread(text, gameFromHtmlBlock);
        } else if (getScoreType() == ScoreType.TOTAL) {
            createBidTotal(text, gameFromHtmlBlock);
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

    private void createBidTotal(String text, Game gameFromHtmlBlock) {
        // 3 -25 41½u-10
        String[] bidBlocks = text.split("\n");
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
                bid.createID();
                gameFromHtmlBlock.getBidList().add(bid);
            }
        }
    }

    private void createBidSpread(String text, Game gameFromHtmlBlock) {
        // 3 -25 41½u-10
        String[] bidBlocks = text.split("\n");
        for (String individualBlock : bidBlocks) {
            Pattern pattern = Pattern.compile("([-]?\\d+" + //digit before o/u
                    "[\\p{N}]?" +  // if char like ½ exists
                    ")" +
                    " " + // condition to check
                    ".*");
            Matcher m = pattern.matcher(individualBlock.trim());
            if (m.matches()) {
                Bid bid = DefaultFactory.Bid.constructDefault();
                bid.setBidAmount(m.group(1));
                bid.setCondition(BidCondition.SPREAD);
                bid.createID();
                gameFromHtmlBlock.getBidList().add(bid);
            }
        }
    }
}
