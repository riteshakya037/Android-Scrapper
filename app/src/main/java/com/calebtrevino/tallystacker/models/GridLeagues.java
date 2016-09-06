package com.calebtrevino.tallystacker.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.calebtrevino.tallystacker.controllers.sources.League;

/**
 * Created by fatal on 9/6/2016.
 */
public class GridLeagues implements Parcelable {
    private League league;
    private int startNo;
    private int endNumber;

    public GridLeagues() {
    }

    protected GridLeagues(Parcel in) {
        league = in.readParcelable(League.class.getClassLoader());
        startNo = in.readInt();
        endNumber = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(league, flags);
        dest.writeInt(startNo);
        dest.writeInt(endNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GridLeagues> CREATOR = new Creator<GridLeagues>() {
        @Override
        public GridLeagues createFromParcel(Parcel in) {
            return new GridLeagues(in);
        }

        @Override
        public GridLeagues[] newArray(int size) {
            return new GridLeagues[size];
        }
    };

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public int getStartNo() {
        return startNo;
    }

    public void setStartNo(int startNo) {
        this.startNo = startNo;
    }

    public int getEndNumber() {
        return endNumber;
    }

    public void setEndNumber(int endNumber) {
        this.endNumber = endNumber;
    }
}
