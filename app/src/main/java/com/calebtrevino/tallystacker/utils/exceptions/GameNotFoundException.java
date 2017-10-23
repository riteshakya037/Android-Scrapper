package com.calebtrevino.tallystacker.utils.exceptions;

/**
 * @author Ritesh Shakya
 */
public class GameNotFoundException extends Throwable {
    private String ignore;

    public GameNotFoundException(String ignore) {
        this.ignore = ignore;
    }

    @Override public String getMessage() {
        return ignore;
    }
}
