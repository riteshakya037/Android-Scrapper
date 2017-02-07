package com.calebtrevino.tallystacker.controllers.events;

import com.calebtrevino.tallystacker.models.Game;

/**
 * @author Ritesh Shakya
 */
public class GameAddedEvent {
    private Game gameData;

    public GameAddedEvent(Game gameData) {
        this.gameData = gameData;
    }

    public Game getGameData() {
        return gameData;
    }
}
