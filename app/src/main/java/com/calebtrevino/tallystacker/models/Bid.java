package com.calebtrevino.tallystacker.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.calebtrevino.tallystacker.controllers.factories.DefaultFactory;
import com.calebtrevino.tallystacker.models.base.BaseModel;
import com.calebtrevino.tallystacker.models.enums.BidCondition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by fatal on 9/3/2016.
 */
public class Bid extends BaseModel implements Parcelable {
    private long _id;
    private float bidAmount;
    private BidCondition condition;

    public Bid() {
    }


    private Bid(Parcel in) {
        _id = in.readLong();
        bidAmount = in.readFloat();
        condition = in.readParcelable(BidCondition.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_id);
        dest.writeFloat(bidAmount);
        dest.writeParcelable(condition, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Bid> CREATOR = new Creator<Bid>() {
        @Override
        public Bid createFromParcel(Parcel in) {
            return new Bid(in);
        }

        @Override
        public Bid[] newArray(int size) {
            return new Bid[size];
        }
    };

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    @Override
    public void createID() {
        this._id = hashCode();
    }

    @Override
    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", get_id());
            jsonObject.put("bid_amount", getBidAmount());
            jsonObject.put("bid_condition", getCondition().getValue());
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }


    public float getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(String bidAmount) {
        bidAmount = bidAmount
                .replaceAll("\\u00BD", ".5")
                .replaceAll("\\u00BC", ".25");
        this.bidAmount = Float.parseFloat(bidAmount);
    }

    public void setBidAmount(float bidAmount) {
        this.bidAmount = bidAmount;
    }

    public BidCondition getCondition() {
        return condition;
    }

    public void setCondition(BidCondition condition) {
        this.condition = condition;
    }


    public static Bid createFromJSON(String jsonString) {
        Bid bid = DefaultFactory.Bid.constructDefault();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            bid.set_id(jsonObject.getLong("id"));
            bid.setBidAmount(jsonObject.getLong("bid_amount"));
            bid.setCondition(BidCondition.match(jsonObject.getString("bid_condition")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bid;
    }

    public static String createJsonArray(List<Bid> bidList) {
        JSONArray jsonArray = new JSONArray();
        for (Bid bid : bidList) {
            try {
                JSONObject jsonObject = new JSONObject(bid.toJSON());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray.toString();
    }

    public static List<Bid> createArrayFromJson(String jsonString) {
        List<Bid> bids = new LinkedList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                bids.add(createFromJSON(jsonArray.get(i).toString()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return bids;
    }
}
