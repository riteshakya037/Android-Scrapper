
package com.calebtrevino.tallystacker.models.sofascore;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeamScore {

    @SerializedName("current")
    @Expose
    private int current;

    @SerializedName("period1")
    @Expose
    private int period1;

    @SerializedName("normaltime")
    @Expose
    private int normaltime;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getPeriod1() {
        return period1;
    }

    public void setPeriod1(int period1) {
        this.period1 = period1;
    }

    public int getNormaltime() {
        return normaltime;
    }

    public void setNormaltime(int normaltime) {
        this.normaltime = normaltime;
    }
}
