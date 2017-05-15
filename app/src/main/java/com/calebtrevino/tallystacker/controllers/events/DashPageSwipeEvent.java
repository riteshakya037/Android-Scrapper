package com.calebtrevino.tallystacker.controllers.events;

/**
 * Created by Ritesh on 0015, May 15, 2017.
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
