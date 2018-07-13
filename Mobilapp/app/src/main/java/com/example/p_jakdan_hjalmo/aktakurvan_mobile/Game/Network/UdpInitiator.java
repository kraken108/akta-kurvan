package com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.Network;

import java.net.DatagramSocket;

/**
 * Created by Jakob on 2018-01-06.
 */

public class UdpInitiator implements Runnable{

    private DatagramSocket socket;

    public UdpInitiator(){
        try{
            socket = new DatagramSocket();
            Thread udpReceiver = new Thread(new UdpReceiver(socket));
            udpReceiver.start();

            Thread udpSender = new Thread(new UdpSender(socket));
            udpSender.start();

        }catch(Exception e){
            System.out.println(e);
            System.out.println("COULDNT START DATAGRAMSOCKET");
        }
    }

    @Override
    public void run() {

    }
}
