package com.calebtrevino.tallystacker.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.calebtrevino.tallystacker.models.enums.BidCondition;
import com.calebtrevino.tallystacker.models.enums.BidResult;
import com.calebtrevino.tallystacker.models.enums.ScoreType;

/**
 * Created by fatal on 9/3/2016.
 */
public class Bid implements Parcelable {
    private long _id;
    private ScoreType scoreType;
    private float bidAmount;
    private BidCondition condition;
    private BidResult result;

    public Bid() {
    }


    private Bid(Parcel in) {
        _id = in.readLong();
        scoreType = in.readParcelable(ScoreType.class.getClassLoader());
        bidAmount = in.readFloat();
        condition = in.readParcelable(BidCondition.class.getClassLoader());
        result = in.readParcelable(BidResult.class.getClassLoader());
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

    public ScoreType getScoreType() {
        return scoreType;
    }

    public void setScoreType(ScoreType scoreType) {
        this.scoreType = scoreType;
    }

    public float getBidAmount() {
        return bidAmount;
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

    public BidResult getResult() {
        return result;
    }

    public void setResult(BidResult result) {
        this.result = result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(_id);
        parcel.writeParcelable(scoreType, i);
        parcel.writeFloat(bidAmount);
        parcel.writeParcelable(condition, i);
        parcel.writeParcelable(result, i);
    }
}
