package com.calebtrevino.tallystacker.controllers.sources.espn_scrappers.exceptions;

/**
 * Occurs when we don't find a single game
 * @author Ritesh Shakya
 */
public class ExpectedElementNotFound extends Exception {
    public ExpectedElementNotFound(String message) {
        super(message);
    }
}