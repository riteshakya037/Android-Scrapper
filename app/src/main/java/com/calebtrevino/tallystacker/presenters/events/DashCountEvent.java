package com.calebtrevino.tallystacker.presenters.events;

/**
 * @author Ritesh Shakya
 */
public class DashCountEvent {
    private int size;

    public DashCountEvent(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
