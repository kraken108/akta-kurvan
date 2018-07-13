package com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.Model;

/**
 * Created by Jakob on 2018-01-06.
 */

public class Game {
    private ConnectionState connectionState;

    public Game(){
        connectionState = ConnectionState.CONNECTING;
    }

    public ConnectionState getConnectionState() {
        return connectionState;
    }

    public void setConnectionState(ConnectionState connectionState) {
        this.connectionState = connectionState;
    }
}
