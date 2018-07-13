/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aktakurvangameserver;

import static aktakurvangameserver.GameState.*;
import java.util.ArrayList;

/**
 *
 * @author Jakob
 */
public class Game {

    private ArrayList<Player> players;
    private GameState gameState;
    private long id;

    public Game(long id, ArrayList<String> playerNames) {
        this.id = id;
        players = new ArrayList<>();
        for (String s : playerNames) {
            players.add(new Player(s));
        }

        gameState = LOBBY;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean allPlayersConnected() {
        for (Player p : players) {
            System.out.println("P: " + p.getEmail());
            if (!p.isConnected()) {
                System.out.println("A player isnt connected, returning false");
                return false;
            }
        }
        return true;
    }

}
