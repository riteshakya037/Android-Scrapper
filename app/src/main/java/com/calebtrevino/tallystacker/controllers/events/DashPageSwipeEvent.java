package com.calebtrevino.tallystacker.controllers.events;

/**
 * @author Ritesh Shakya
 */

public class DashPageSwipeEvent {
    private int position;

    public DashPageSwipeEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
