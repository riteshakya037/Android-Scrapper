package com.calebtrevino.tallystacker.models.enums;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

/**
 * @author Ritesh Shakya
 */
public enum BidResult implements Parcelable {
    POSITIVE("+"), NEGATIVE("-"), NEUTRAL("null");

    private final String value;

    @SuppressWarnings("unused")
    BidResult(Parcel in) {
        value = in.readString();
    }


    BidResult(String value) {
        this.value = value;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BidResult> CREATOR = new Creator<BidResult>() {
        @Override
        public BidResult createFromParcel(Parcel in) {
            return match(in.readString());
        }

        @Override
        public BidResult[] newArray(int size) {
            return new BidResult[size];
        }
    };

    public String getValue() {
        return value;
    }


    public static BidResult match(String s) {
        for (BidResult bidResult : Arrays.asList(BidResult.values())) {
            if (bidResult.getValue().equals(s)) {
                return bidResult;
            }
        }
        return BidResult.NEUTRAL;
    }
}
