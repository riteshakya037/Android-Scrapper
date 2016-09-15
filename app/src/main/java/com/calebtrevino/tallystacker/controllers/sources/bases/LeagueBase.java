package com.calebtrevino.tallystacker.controllers.sources.bases;

import android.app.Activity;
import android.util.Log;

import com.calebtrevino.tallystacker.controllers.factories.DefaultFactory;
import com.calebtrevino.tallystacker.controllers.sources.League;
import com.calebtrevino.tallystacker.controllers.sources.MLB_Total;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.database.DatabaseContract;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by fatal on 9/4/2016.
 */

public abstract class LeagueBase implements League {
    private static final String TAG = MLB_Total.class.getSimpleName();

    @Override
    public List<Game> pullGamesFromNetwork(Activity activity) throws Exception {
        Log.e(TAG, "Started " + getName());
        List<Game> updatedGameList = new LinkedList<>();
        Document parsedDocument = null;
        try {
            parsedDocument = Jsoup.connect(getBaseUrl()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        updatedGameList = scrapeUpdateGamesFromParsedDocument(updatedGameList, parsedDocument);

        updateLibraryInDatabase(updatedGameList, activity);
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
                createBidInfo(currentColumnBlock.text(), gameFromHtmlBlock);
            }
        }
        gameFromHtmlBlock.createID();
        return gameFromHtmlBlock;
    }

    protected abstract void createGameInfo(String text, Game gameFromHtmlBlock);

    protected abstract void createBidInfo(String text, Game gameFromHtmlBlock);


    private void updateLibraryInDatabase(List<Game> updatedGameList, Activity activity) {
        DatabaseContract.DbHelper dbHelper = new DatabaseContract.DbHelper(activity);
        dbHelper.onInsertGame(updatedGameList);
    }

    @Override
    public String toString() {
        return "League {" +
                " name = \"" + getName() +
                "\"}";
    }
}
