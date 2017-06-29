
package com.calebtrevino.tallystacker.models.espn;


public class Status {

    public Type type;
    public String displayClock;

    @Override
    public String toString() {
        return "Status{" +
                "type=" + type +
                '}';
    }
}
