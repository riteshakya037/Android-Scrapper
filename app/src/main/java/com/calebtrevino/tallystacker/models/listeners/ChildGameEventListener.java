package com.calebtrevino.tallystacker.models.listeners;


import com.calebtrevino.tallystacker.models.Game;

/**
 * @author Ritesh Shakya
 */
@SuppressWarnings({"EmptyMethod", "unused", "UnusedParameters"})
public interface ChildGameEventListener {
    void onChildAdded(Game game);

    void onChildChanged(Game game);

    void onChildRemoved(Game game);
}
