package com.calebtrevino.tallystacker.controllers.events;

/**
 * @author Ritesh Shakya
 */
public class DashCountEvent {
    private int size;
    private int dateLag;

    public DashCountEvent(int size, int dateLag) {
        this.size = size;
        this.dateLag = dateLag;
    }

    public int getDateLag() {
        return dateLag;
    }

    public int getSize() {
        return size;
    }
}
