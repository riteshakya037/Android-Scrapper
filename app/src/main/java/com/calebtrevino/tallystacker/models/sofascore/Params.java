
package com.calebtrevino.tallystacker.models.sofascore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Params {

    @SerializedName("sport")
    @Expose
    private String sport;
    @SerializedName("category")
    @Expose
    private Object category;
    @SerializedName("date")
    @Expose
    private String date;

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public Object getCategory() {
        return category;
    }

    public void setCategory(Object category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
