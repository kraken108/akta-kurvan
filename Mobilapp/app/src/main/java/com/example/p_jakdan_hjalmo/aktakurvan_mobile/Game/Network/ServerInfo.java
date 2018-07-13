package com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.Network;

/**
 * Created by Jakob on 2018-01-06.
 */

public class ServerInfo {
    private static ServerInfo serverInfo;
    private String serverIp;
    private int tcpPort;
    private int udpPort;
    private int gameId;
    private String enemy;
    private int playerId;

    public static ServerInfo getInstance(){
        if(serverInfo == null){
            serverInfo = new ServerInfo();
        }
        return serverInfo;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    public int getUdpPort() {
        return udpPort;
    }

    public void setUdpPort(int udpPort) {
        this.udpPort = udpPort;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getEnemy() {
        return enemy;
    }

    public void setEnemy(String enemy) {
        this.enemy = enemy;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
