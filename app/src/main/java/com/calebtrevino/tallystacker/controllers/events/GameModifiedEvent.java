package com.calebtrevino.tallystacker.controllers.events;

import com.calebtrevino.tallystacker.models.Game;

/**
 * @author Ritesh Shakya
 */
public class GameModifiedEvent {
    private Game gameData;

    public GameModifiedEvent(Game gameData) {
        this.gameData = gameData;
    }

    public Game getGameData() {
        return gameData;
    }
}
