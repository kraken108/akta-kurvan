/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aktakurvangameserver;

import java.util.ArrayList;

/**
 *
 * @author Jakob
 */
public class GameHandler {
    
    private static GameHandler theHandler;
    private ArrayList<Game> games;
    private final int udpPort = 7212;
    
    public static GameHandler getInstance(){
        if(theHandler == null){
            theHandler = new GameHandler();
        }
        return theHandler;
    }
    
    private GameHandler(){
        games = new ArrayList<>();
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public void setGames(ArrayList<Game> games) {
        this.games = games;
    }   

    public int getUdpPort() {
        return udpPort;
    }
    
    
    
}
