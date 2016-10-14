package com.calebtrevino.tallystacker;

import com.calebtrevino.tallystacker.models.Game;

interface ServiceListener {
    void databaseReady(in Game game);
}
