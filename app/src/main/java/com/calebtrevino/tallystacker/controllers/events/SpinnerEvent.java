package com.calebtrevino.tallystacker.controllers.events;

/**
 * Created by Ritesh on 0012, May 12, 2017.
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
