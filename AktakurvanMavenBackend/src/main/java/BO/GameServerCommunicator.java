/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jakob
 */
public class GameServerCommunicator {
    
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    
    public GameServerCommunicator() throws IOException{
        socket = new Socket(GameInfo.getInstance().getIp(),GameInfo.getInstance().getBackendPort());
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
    
    public int requestNewGame(ArrayList<String> playerNames){
        String s = "STARTGAME";
        for(String player : playerNames){
            s += " " + player;
        }
        
        try{
            out.println(s);
            String response = in.readLine();
            int responseInt = Integer.parseInt(response);
            return responseInt;
        }catch(Exception e){
            return -1;
        }finally{
            if(socket != null){
                try {
                    socket.close();
                } catch (IOException ex) {
                    Logger.getLogger(GameServerCommunicator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(out != null){
                out.close();
            }
            if(in != null){
                try {
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(GameServerCommunicator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }
}
