package com.calebtrevino.tallystacker.controllers.events;

import com.calebtrevino.tallystacker.models.Game;

/**
 * @author Ritesh Shakya
 */
public class GameRemovedEvent {
    private Game gameData;

    public Game getGameData() {
        return gameData;
    }
}
