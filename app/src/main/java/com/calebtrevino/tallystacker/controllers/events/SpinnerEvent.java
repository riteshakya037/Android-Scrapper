package com.calebtrevino.tallystacker.controllers.events;

/**
 * @author Ritesh Shakya
 */

public class SpinnerEvent {
    private int position;

    public SpinnerEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
