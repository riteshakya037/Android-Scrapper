package com.calebtrevino.tallystacker.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.calebtrevino.tallystacker.controllers.sources.League;
import com.calebtrevino.tallystacker.controllers.sources.bases.LeagueBase;
import com.calebtrevino.tallystacker.models.enums.ScoreType;

import java.util.List;

/**
 * Created by fatal on 9/3/2016.
 */

public class Game implements Parcelable {
    public static final String TAG = Game.class.getSimpleName();

    private Long _id;
    private Team firstTeam;
    private Team SecondTeam;
    private League leagueType;
    private long gameDateTime;
    private ScoreType scoreType;
    private List<Bid> bidList;

    protected Game(Parcel in) {
        firstTeam = in.readParcelable(Team.class.getClassLoader());
        SecondTeam = in.readParcelable(Team.class.getClassLoader());
        leagueType = in.readParcelable(League.class.getClassLoader());
        gameDateTime = in.readLong();
        scoreType = in.readParcelable(ScoreType.class.getClassLoader());
        bidList = in.createTypedArrayList(Bid.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(firstTeam, flags);
        dest.writeParcelable(SecondTeam, flags);
        dest.writeParcelable(leagueType, flags);
        dest.writeLong(gameDateTime);
        dest.writeParcelable(scoreType, flags);
        dest.writeTypedList(bidList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    public Long get_id() {
        return _id;
    }

    public void set_id() {
        this._id = (long) hashCode();
    }

    public List<Bid> getBidList() {
        return bidList;
    }

    public void setBidList(List<Bid> bidList) {
        this.bidList = bidList;
    }

    public ScoreType getScoreType() {
        return scoreType;
    }

    public void setScoreType(ScoreType scoreType) {
        this.scoreType = scoreType;
    }


    public long getGameDateTime() {
        return gameDateTime;
    }

    public void setGameDateTime(long gameDate) {
        this.gameDateTime = gameDate;
    }

    public League getLeagueType() {
        return leagueType;
    }

    public void setLeagueType(League leagueType) {
        this.leagueType = leagueType;
    }

    public Team getSecondTeam() {
        return SecondTeam;
    }

    public void setSecondTeam(Team secondTeam) {
        SecondTeam = secondTeam;
    }

    public Team getFirstTeam() {
        return firstTeam;
    }

    public void setFirstTeam(Team firstTeam) {
        this.firstTeam = firstTeam;
    }


    public Game() {
    }


    @Override
    public String toString() {
        return "Game{" +
                "_id=" + _id +
                ", firstTeam=" + firstTeam +
                ", SecondTeam=" + SecondTeam +
                ", leagueType=" + leagueType +
                ", gameDateTime=" + gameDateTime +
                ", scoreType=" + scoreType +
                ", bidList=" + bidList +
                '}';
    }
}
