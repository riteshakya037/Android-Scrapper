package com.calebtrevino.tallystacker.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.calebtrevino.tallystacker.controllers.factories.DefaultFactory;
import com.calebtrevino.tallystacker.controllers.sources.League;
import com.calebtrevino.tallystacker.models.base.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by fatal on 9/6/2016.
 */
public class GridLeagues extends BaseModel implements Parcelable {
    private long _id;
    private League league;
    private int startNo;
    private int endNumber;
    private boolean forceAdd;

    public GridLeagues() {
    }

    private GridLeagues(Parcel in) {
        _id = in.readLong();
        league = in.readParcelable(League.class.getClassLoader());
        startNo = in.readInt();
        endNumber = in.readInt();
        forceAdd = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_id);
        dest.writeParcelable(league, flags);
        dest.writeInt(startNo);
        dest.writeInt(endNumber);
        dest.writeByte((byte) (forceAdd ? 1 : 0));
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

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

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

    public boolean isForceAdd() {
        return forceAdd;
    }

    public void setForceAdd(boolean forceAdd) {
        this.forceAdd = forceAdd;
    }

    @Override
    public void createID() {
        _id = hashCode();
    }

    @Override
    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", get_id());
            jsonObject.put("league", getLeague().getPackageName());
            jsonObject.put("start_no", getStartNo());
            jsonObject.put("end_no", getEndNumber());
            jsonObject.put("force_add", isForceAdd());
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }


    public static String createJsonArray(List<GridLeagues> gridLeagues) {
        JSONArray jsonArray = new JSONArray();
        for (GridLeagues leagues : gridLeagues) {
            try {
                JSONObject jsonObject = new JSONObject(leagues.toJSON());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray.toString();
    }


    public static GridLeagues createFromJSON(String jsonString) {
        GridLeagues gridLeagues = DefaultFactory.GridLeagues.constructDefault();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            gridLeagues.set_id(jsonObject.getLong("id"));
            gridLeagues.setLeague((League) Class.forName(jsonObject.getString("league")).newInstance());
            gridLeagues.setStartNo(jsonObject.getInt("start_no"));
            gridLeagues.setEndNumber(jsonObject.getInt("end_no"));
            gridLeagues.setForceAdd(jsonObject.getBoolean("force_add"));
        } catch (JSONException | IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            e.printStackTrace();
        }
        return gridLeagues;
    }

    public static List<GridLeagues> createArrayFromJson(String jsonString) {
        List<GridLeagues> gridLeagues = new LinkedList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                gridLeagues.add(createFromJSON(jsonArray.get(i).toString()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return gridLeagues;
    }
}
