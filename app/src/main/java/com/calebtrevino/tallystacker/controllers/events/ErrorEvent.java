package com.calebtrevino.tallystacker.controllers.events;

/**
 * @author Ritesh Shakya
 */
public class ErrorEvent {
    private boolean visible;

    public ErrorEvent(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }
}
