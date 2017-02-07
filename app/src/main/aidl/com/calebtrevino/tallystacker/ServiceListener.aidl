package com.calebtrevino.tallystacker;

import com.calebtrevino.tallystacker.models.Game;

interface ServiceListener {
    void gameAdded(in Game game);
    void gameModified(in Game game);
    void gameDeleted(in Game game);
}
