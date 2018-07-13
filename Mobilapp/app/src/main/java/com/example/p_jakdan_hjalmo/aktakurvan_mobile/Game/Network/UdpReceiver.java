package com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.Network;

import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.GameController.GameState;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.Model.GameModel;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.Model.Sprite;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.ViewModel.ScreenInfo;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by Jakob on 2018-01-06.
 */

public class UdpReceiver implements Runnable {
    private DatagramSocket socket;

    public UdpReceiver(DatagramSocket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        while (GameModel.getInstance().isActive()) {
            try {
                //if game is disconnected, return

                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);
                String sentence = new String(receivePacket.getData());
                String[] splitted = sentence.split(" ");
                if(splitted[0].equals("P") && splitted.length > 4){
                    float x = Float.parseFloat(splitted[3])* ScreenInfo.getInstance().getWidth();
                    float y = Float.parseFloat(splitted[4]) * ScreenInfo.getInstance().getHeight();
                    Sprite player = GameModel.getInstance().getHead();

                    Sprite tailSprite = new Sprite(x, y);

                    while(GameModel.getInstance().isSpritesToAddLock()){

                    }
                    GameModel.getInstance().getSpritesToAdd().add(tailSprite);

                }else if(splitted[0].equals("GAMEOVER")){
                    System.out.println("GAME OVER GAME OVER GAME OVER");
                    GameModel.getInstance().setGameState(GameState.GAMEOVER);
                    return;
                }
            }catch(Exception e){

            }
        }
    }
}
