package com.calebtrevino.tallystacker.models.listeners;

import com.calebtrevino.tallystacker.models.base.BaseModel;

/**
 * Created by fatal on 9/9/2016.
 */
public interface ChildEventListener {
    void onChildAdded(BaseModel baseModel);

    void onChildChanged(BaseModel baseModel);

    void onChildRemoved(BaseModel baseModel);
}
