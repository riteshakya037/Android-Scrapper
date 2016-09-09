package com.calebtrevino.tallystacker.models.listeners;


import com.calebtrevino.tallystacker.models.Game;

/**
 * Created by fatal on 9/9/2016.
 */
public interface ChildGameEventListener {
    void onChildAdded(Game game);

    void onChildChanged(Game game);

    void onChildRemoved(Game game);
}
