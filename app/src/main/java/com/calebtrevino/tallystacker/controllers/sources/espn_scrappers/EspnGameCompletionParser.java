package com.calebtrevino.tallystacker.controllers.sources.espn_scrappers;

import com.calebtrevino.tallystacker.models.Game;

/**
 * @author Ritesh Shakya
 */

public class EspnGameCompletionParser {

    private Game game;

    private EspnGameCompletionParser(Game game) {
        this.game = game;
    }

    public static EspnGameCompletionParser getInstance(Game game) {
        return new EspnGameCompletionParser(game);
    }
}
