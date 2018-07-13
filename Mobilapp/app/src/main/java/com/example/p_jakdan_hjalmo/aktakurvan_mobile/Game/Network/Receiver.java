package com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.Network;

import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.Model.ConnectionState;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.Model.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by Jakob on 2018-01-06.
 */

public class Receiver implements Runnable{

    private BufferedReader in;
    private Game game;

    public Receiver(Socket s,Game game) throws IOException {
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        this.game = game;
    }

    @Override
    public void run() {
        while(true){
            try{
                String incoming = in.readLine();
                if(incoming.equals("READY")){
                    System.out.println("RECEIVED READY");
                    game.setConnectionState(ConnectionState.WAITING);
                }
                if(incoming.startsWith("GO ")){
                    String[] splitted = incoming.split(" ");
                    if(splitted.length > 2){
                        ServerInfo.getInstance().setUdpPort(Integer.parseInt(splitted[1]));
                        ServerInfo.getInstance().setPlayerId(Integer.parseInt(splitted[2]));

                    }

                }
            }catch(Exception e){
                System.out.println(e);
                System.out.println("TERMINATING RECEIVER");
                return;
            }
        }
    }
}
