package com.calebtrevino.tallystacker.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.calebtrevino.tallystacker.controllers.factories.DefaultFactory;
import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases.League;
import com.calebtrevino.tallystacker.models.base.BaseModel;
import com.calebtrevino.tallystacker.models.enums.BidResult;
import com.calebtrevino.tallystacker.models.enums.GameStatus;
import com.calebtrevino.tallystacker.models.enums.ScoreType;
import com.calebtrevino.tallystacker.utils.Constants;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;


/**
 * @author Ritesh Shakya
 */

public class Game extends BaseModel implements Parcelable {
    @SuppressWarnings("unused")
    public static final String TAG = Game.class.getSimpleName();
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
    private long _id;
    private Team firstTeam;
    private Team SecondTeam;
    private League leagueType;
    private long gameDateTime;
    private long gameAddDate;
    private ScoreType scoreType;
    private List<Bid> bidList;
    private BidResult bidResult;
    private long firstTeamScore;
    private long secondTeamScore;
    private Bid VI_bid = DefaultFactory.Bid.constructDefault();
    private long updatedTime;
    private String gameUrl;
    private GameStatus gameStatus;
    private boolean reqManual = true;
    private int group = -1;
    private int VI_row;
    private int gridCount;
    private BidResult previousGridStatus;
    private boolean bannerVisibility;

    public Game() {
    }

    protected Game(Parcel in) {
        _id = in.readLong();
        firstTeam = in.readParcelable(Team.class.getClassLoader());
        SecondTeam = in.readParcelable(Team.class.getClassLoader());
        leagueType = (League) in.readSerializable();
        gameDateTime = in.readLong();
        gameAddDate = in.readLong();
        scoreType = in.readParcelable(ScoreType.class.getClassLoader());
        bidList = in.createTypedArrayList(Bid.CREATOR);
        bidResult = in.readParcelable(BidResult.class.getClassLoader());
        firstTeamScore = in.readLong();
        secondTeamScore = in.readLong();
        VI_bid = in.readParcelable(Bid.class.getClassLoader());
        updatedTime = in.readLong();
        gameUrl = in.readString();
        gameStatus = in.readParcelable(GameStatus.class.getClassLoader());
        reqManual = in.readByte() != 0;
        group = in.readInt();
        VI_row = in.readInt();
        gridCount = in.readInt();
        previousGridStatus = in.readParcelable(BidResult.class.getClassLoader());
        bannerVisibility = in.readByte() != 0;
    }

    public static String getIDArrayToJSSON(List<Game> gameList) {
        JSONArray jsonArray = new JSONArray();
        for (Game game : gameList) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", game.getId());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray.toString();
    }

    public static List<String> getIdArrayFromJSON(String idListJson) {
        List<String> idList = new LinkedList<>();
        try {
            JSONArray jsonArray = new JSONArray(idListJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                idList.add(jsonObject.getString("id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return idList;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_id);
        dest.writeParcelable(firstTeam, flags);
        dest.writeParcelable(SecondTeam, flags);
        dest.writeSerializable(leagueType);
        dest.writeLong(gameDateTime);
        dest.writeLong(gameAddDate);
        dest.writeParcelable(scoreType, flags);
        dest.writeTypedList(bidList);
        dest.writeParcelable(bidResult, flags);
        dest.writeLong(firstTeamScore);
        dest.writeLong(secondTeamScore);
        dest.writeParcelable(VI_bid, flags);
        dest.writeLong(updatedTime);
        dest.writeString(gameUrl);
        dest.writeParcelable(gameStatus, flags);
        dest.writeByte((byte) (reqManual ? 1 : 0));
        dest.writeInt(group);
        dest.writeInt(VI_row);
        dest.writeInt(gridCount);
        dest.writeParcelable(previousGridStatus, flags);
        dest.writeByte((byte) (bannerVisibility ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public long getId() {
        return _id;
    }

    public void setId(long _id) {
        this._id = _id;
    }

    @Override
    public void createID() {
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

    public long getGameAddDate() {
        return gameAddDate;
    }

    public void setGameAddDate(long gameAddDate) {
        this.gameAddDate = gameAddDate;
    }

    public void setGameAddDate() {
        DateTime dateTime = new DateTime(getGameDateTime(), Constants.DATE.VEGAS_TIME_ZONE);
        this.gameAddDate = dateTime.withTimeAtStartOfDay().getMillis();
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

    public BidResult getBidResult() {
        return bidResult;
    }

    public void setBidResult(BidResult bidResult) {
        this.bidResult = bidResult;
    }

    public long getFirstTeamScore() {
        return firstTeamScore;
    }

    public void setFirstTeamScore(long firstTeamScore) {
        this.firstTeamScore = firstTeamScore;
    }

    public long getSecondTeamScore() {
        return secondTeamScore;
    }

    public void setSecondTeamScore(long secondTeamScore) {
        this.secondTeamScore = secondTeamScore;
    }

    public String getGameUrl() {
        return gameUrl;
    }

    public void setGameUrl(String gameUrl) {
        this.gameUrl = gameUrl;
    }

    public boolean isReqManual() {
        return reqManual;
    }

    public void setReqManual(boolean reqManual) {
        this.reqManual = reqManual;
    }

    public Bid getVIBid() {
        return VI_bid;
    }

    public void setVIBid() {
        for (Bid bid : getBidList()) {
            if (bid.isVIColumn()) {
                this.VI_bid = bid;
                break;
            }
        }
    }

    public int getVIRow() {
        return VI_row;
    }

    public void setVIRow(int VI_row) {
        this.VI_row = VI_row;
    }

    @Override
    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", getId());
            jsonObject.put("first_team", getFirstTeam().getId());
            jsonObject.put("second_team", getSecondTeam().getId());
            jsonObject.put("league_type", getLeagueType().getPackageName());
            jsonObject.put("game_date_type", getGameDateTime());
            jsonObject.put("score_type", getScoreType().getValue());
            jsonObject.put("bid_list", Bid.createJsonArray(getBidList()));
            jsonObject.put("bid_result", getBidResult().getValue());
            jsonObject.put("first_team_score", getFirstTeamScore());
            jsonObject.put("second_team_score", getSecondTeamScore());
            jsonObject.put("game_added", getGameAddDate());
            jsonObject.put("game_url", getGameUrl());
            jsonObject.put("is_complete", getGameStatus());
            jsonObject.put("req_manual", isReqManual());
            jsonObject.put("vi_row", getVIRow());
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        return _id == game._id;
    }

    private long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(long updatedTime) {
        this.updatedTime = updatedTime;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus complete) {
        this.gameStatus = complete;
    }

    @Override
    public String toString() {
        return "Game{" +
                "firstTeam=" + firstTeam.getCity() +
                ", SecondTeam=" + SecondTeam.getCity() +
                ", bidResult=" + bidResult +
                ", firstTeamScore=" + firstTeamScore +
                ", secondTeamScore=" + secondTeamScore +
                ", gameStatus=" + gameStatus +
                '}';
    }

    @Override
    public int hashCode() {
        int result = firstTeam.hashCode();
        result = 31 * result + SecondTeam.hashCode();
        result = 31 * result + leagueType.hashCode();
        result = 31 * result + (int) (gameDateTime ^ (gameDateTime >>> 32));
        result = 31 * result + (int) (gameAddDate ^ (gameAddDate >>> 32));
        result = 31 * result + scoreType.hashCode();
        result = 31 * result + (int) (firstTeamScore ^ (firstTeamScore >>> 32));
        result = 31 * result + (int) (secondTeamScore ^ (secondTeamScore >>> 32));
        result = 31 * result + VI_bid.hashCode();
        return result;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public void resetGroup() {
        this.group = -1;
    }

    public int getGridCount() {
        return gridCount;
    }

    public void setGridCount(int gridCount) {
        this.gridCount = gridCount;
    }

    public BidResult getPreviousGridStatus() {
        return previousGridStatus;
    }

    public void setPreviousGridStatus(BidResult previousGridStatus) {
        this.previousGridStatus = previousGridStatus;
    }

    public boolean getBannerVisibility() {
        return bannerVisibility;
    }

    public void setBannerVisibility(boolean bannerVisibility) {
        this.bannerVisibility = bannerVisibility;
    }

    public static class GameTimeComparator implements Comparator<Game> {

        @Override
        public int compare(Game o1, Game o2) {
            int gameTimeDiff = new DateTime(o1.getGameDateTime()).compareTo(new DateTime(o2.getGameDateTime()));
            if (gameTimeDiff == 0) {
                return new DateTime(o1.getUpdatedTime()).compareTo(new DateTime(o2.getUpdatedTime()));
            }
            return gameTimeDiff;
        }
    }

    public static class GameComparator implements Comparator<Game> {

        @Override
        public int compare(Game o1, Game o2) {
            if (o1.getLeagueType().equals(o2.getLeagueType())) {
                int gameTimeDiff = new DateTime(o1.getGameDateTime()).compareTo(new DateTime(o2.getGameDateTime()));
                if (gameTimeDiff == 0) {
                    return new DateTime(o1.getUpdatedTime()).compareTo(new DateTime(o2.getUpdatedTime()));
                }
                return gameTimeDiff;
            } else
                return o1.getLeagueType().getPackageName().compareTo(o2.getLeagueType().getPackageName());
        }
    }

    public static class VIComparator implements Comparator<Game> {
        @Override
        public int compare(Game o1, Game o2) {
            if (o1.getLeagueType().equals(o2.getLeagueType())) {
                return o1.getVIRow() - o2.getVIRow();
            } else
                return o1.getLeagueType().getPackageName().compareTo(o2.getLeagueType().getPackageName());
        }
    }
}
