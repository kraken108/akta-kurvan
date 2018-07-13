/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aktakurvangameserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jakob
 */
public class FrontendCommunicator implements Runnable {

    private ServerSocket serverSocket;
    private final int port = 8212;
    private BufferedReader in;
    private PrintWriter out;

    public FrontendCommunicator() throws IOException {
        serverSocket = new ServerSocket(port);
    }

    
    @Override
    public void run() {
        while(true){
            BufferedReader newIn = null;
            PrintWriter newOut = null;

            try {
                Socket newConnection = serverSocket.accept();
                System.out.println("New Client!");
                newIn = new BufferedReader(new InputStreamReader(newConnection.getInputStream()));
                newOut = new PrintWriter(newConnection.getOutputStream(), true);
                
                String line = newIn.readLine();
                System.out.println(line);
                String[] splitted = line.split(" ");
                if(line.startsWith("JOINGAME") && splitted.length > 2){
                    
                    String email = splitted[1];
                    int gameId = Integer.parseInt(splitted[2]);
                    
                    
                    int i = 0;
                    //loop through games
                    
                    for(Game g : GameHandler.getInstance().getGames()){
                        if(g.getId() == gameId){
                            for(Player p : g.getPlayers()){
                                if(p.getEmail().equals(email)){
                                    System.out.println("ADDING PLAYER");
                                    p.setConnected(true);
                                    p.setSocket(newConnection);
                                    break;
                                }
                            }
                            if(g.allPlayersConnected()){
                                //start game and remove it from gamehandler
                                System.out.println("ALL PLAYERS FOR A GAME FOUND! STARTING GAME");
                                g.setGameState(GameState.INGAME);
                                Thread t = new Thread(new GameThread(g));
                                t.start();
                            }
                        }
                        i++;
                    }
                    
                }
                
                
            } catch (IOException ex) {
                System.out.println(ex);
            }finally{
                /*if(newIn != null){
                    try {
                        newIn.close();
                    } catch (IOException ex) {}
                }
                if(newOut != null){
                    newOut.close();
                }*/
                //close readers and writers
            }
        }
    }

}
