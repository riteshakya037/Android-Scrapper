package com.calebtrevino.tallystacker.presenters.bases;

import android.os.Parcelable;

/**
 * Created by fatal on 9/6/2016.
 */
public interface BasePositionStateMapper {

    public Parcelable getPositionState();

    public void setPositionState(Parcelable state);
}
