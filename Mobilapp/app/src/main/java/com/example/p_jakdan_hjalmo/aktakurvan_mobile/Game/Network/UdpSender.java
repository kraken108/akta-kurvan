package com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.Network;

import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.GameController.GameState;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.Model.GameModel;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.ViewModel.ScreenInfo;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by Jakob on 2018-01-06.
 */

public class UdpSender implements Runnable {

    private DatagramSocket socket;

    public UdpSender(DatagramSocket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        long lastSend = 0;
        try{
            InetAddress ip = InetAddress.getByName(ServerInfo.getInstance().getServerIp());
            int port = ServerInfo.getInstance().getUdpPort();
            while(GameModel.getInstance().isActive()){
                //if game is disconnected, return
                if(GameModel.getInstance().getGameState().equals(GameState.GAMEOVER)){
                    String msg = "GAMEOVER " + ServerInfo.getInstance().getGameId() + " " + ServerInfo.getInstance().getPlayerId();
                    byte[] responseData = msg.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(responseData, responseData.length, ip, port);
                    socket.send(sendPacket);
                    return;
                }
                if(System.currentTimeMillis() > lastSend+100){//send 10 times per second
                    String sentence = "P " + ServerInfo.getInstance().getGameId() + " "
                            + ServerInfo.getInstance().getPlayerId()
                            + " "
                            + GameModel.getInstance().getHead().getX()/ ScreenInfo.getInstance().getWidth()
                            + " " + GameModel.getInstance().getHead().getY() / ScreenInfo.getInstance().getHeight();
                    byte[] responseData = sentence.getBytes();

                    DatagramPacket sendPacket = new DatagramPacket(responseData, responseData.length, ip, port);
                    socket.send(sendPacket);
                    lastSend = System.currentTimeMillis();
                }
            }
        }catch(Exception e){

        }

    }
}
