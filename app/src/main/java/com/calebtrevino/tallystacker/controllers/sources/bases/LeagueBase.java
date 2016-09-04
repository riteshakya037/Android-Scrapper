package com.calebtrevino.tallystacker.controllers.sources.bases;

import android.util.Log;

import com.calebtrevino.tallystacker.controllers.factories.DefaultFactory;
import com.calebtrevino.tallystacker.controllers.sources.League;
import com.calebtrevino.tallystacker.controllers.sources.ProBaseball;
import com.calebtrevino.tallystacker.models.Game;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fatal on 9/4/2016.
 */

public abstract class LeagueBase implements League {
    private static final String TAG = ProBaseball.class.getSimpleName();

    @Override
    public List<Game> pullGamesFromNetwork() {
        Log.e(TAG, "ENTERED");
        List<Game> updatedGameList = new ArrayList<>();
//        do {
        Document parsedDocument = null;
        try {
            parsedDocument = Jsoup.connect(getBaseUrl()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
//            if (findNextUrlFromParsedDocument(getBaseUrl(), parsedDocument != null ? parsedDocument.text() : null).equals(DefaultFactory.UpdatePageMarker.DEFAULT_NEXT_PAGE_URL)) {
//                break;
//            }
        updatedGameList = scrapeUpdateGamesFromParsedDocument(updatedGameList, parsedDocument);
//        } while (true);
        updateLibraryInDatabase(updatedGameList);
        return updatedGameList;
    }

    private List<Game> scrapeUpdateGamesFromParsedDocument(List<Game> updatedGameList, Document parsedDocument) {
        Elements updatedHtmlBlocks = parsedDocument.select(getCSSQuery());
        for (Element currentHtmlBlock : updatedHtmlBlocks) {
            Log.e(TAG, "Outer Most " + currentHtmlBlock.text());
            Game currentGame = constructGameFromHtmlBlock(currentHtmlBlock);
            updatedGameList.add(currentGame);
        }

        return updatedGameList;
    }


    private Game constructGameFromHtmlBlock(Element currentHtmlBlock) {
        final String html = currentHtmlBlock.outerHtml();
        Game gameFromHtmlBlock = DefaultFactory.Game.constructDefault();
        gameFromHtmlBlock.setScoreType(getScoreType());
        gameFromHtmlBlock.setLeagueType(this);
        Document parsedDocument = Jsoup.parse(html, "", Parser.xmlParser());
        Elements updatedHtmlBlocks = parsedDocument.select("td");
        boolean once = true;
        for (Element currentColumnBlock : updatedHtmlBlocks) {
            Log.e(TAG, "Inner Most " + currentColumnBlock.text());

            if (once) {
                once = false;
                createGame(currentColumnBlock.text(), gameFromHtmlBlock);

            } else {
                createBid(currentColumnBlock.text(), gameFromHtmlBlock);
            }
        }
        gameFromHtmlBlock.set_id();
        return gameFromHtmlBlock;
    }

    protected abstract void createGame(String text, Game gameFromHtmlBlock);

    protected abstract void createBid(String text, Game gameFromHtmlBlock);

    protected abstract String getErrorMessage();

    private void updateLibraryInDatabase(List<Game> updatedGameList) {
        //// TODO: 9/4/2016
    }

}
