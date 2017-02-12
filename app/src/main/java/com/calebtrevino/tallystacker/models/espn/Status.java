
package com.calebtrevino.tallystacker.models.espn;


public class Status {

    public Long clock;
    public Type type;
    public Long period;
    public String displayClock;

    @Override
    public String toString() {
        return "Status{" +
                "type=" + type +
                '}';
    }
}
