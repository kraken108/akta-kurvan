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
public class BackendCommunicator implements Runnable{

    private ServerSocket serverSocket;
    private BufferedReader in;
    private PrintWriter out;
    private final int port = 9212;
    private int gameId;
    
    public BackendCommunicator() throws IOException{
        serverSocket = new ServerSocket(port);
        gameId = 0;
    }
    
    @Override
    public void run() {
        while(true){
            BufferedReader newIn = null;
            PrintWriter newOut = null;
            Socket newConnection = null;
            try {
                newConnection = serverSocket.accept();
                System.out.println("New Connection!");
                newIn = new BufferedReader(new InputStreamReader(newConnection.getInputStream()));
                newOut = new PrintWriter(newConnection.getOutputStream(), true);
                
                String line = newIn.readLine();
                System.out.println(line);
                String[] splitted = line.split(" ");
                if(line.startsWith("STARTGAME") && splitted.length > 2){
                    //start game
                    ArrayList<String> playerNames = new ArrayList<>();

                    for(int i = 1; i < splitted.length; i++){
                        playerNames.add(splitted[i]);
                    }
                    Game g = new Game(gameId,playerNames);
                    GameHandler.getInstance().getGames().add(g);
                    System.out.println("STARTED GAME WITH ID: " + gameId);
                    newOut.println(gameId);
                    gameId += 1;
                    
                }
                
                
            } catch (IOException ex) {
                System.out.println(ex);
            }finally{
                if(newIn != null){
                    try {
                        newIn.close();
                    } catch (IOException ex) {}
                }
                if(newOut != null){
                    newOut.close();
                }
                if(newConnection != null){
                    try {
                        newConnection.close();
                    } catch (IOException ex) {
                        Logger.getLogger(BackendCommunicator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                //close readers and writers
            }
        }
    }
    
}
