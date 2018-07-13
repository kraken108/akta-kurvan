/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aktakurvangameserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 *
 * @author Jakob
 */
public class UdpListener implements Runnable {

    private DatagramSocket socket;

    public UdpListener() throws SocketException {
        socket = new DatagramSocket(GameHandler.getInstance().getUdpPort());
    }

    @Override
    public void run() {
        while (true) {
            try {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);
                String sentence = new String(receivePacket.getData());
                //System.out.println(sentence);
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                String[] splitted = sentence.split(" ");
                if (splitted[0].equals("P")) {
                    int gameId = Integer.parseInt(splitted[1]);
                    int playerId = Integer.parseInt(splitted[2]);
                    // float xPos = Float.parseFloat(splitted[3]);
                    // float yPos = Float.parseFloat(splitted[4]);

                    //loop through games
                    for (Game g : GameHandler.getInstance().getGames()) {
                        if (g.getId() == gameId && g.getGameState().equals(GameState.INGAME)) {
                            for (Player p : g.getPlayers()) {
                                if (p.getId() == playerId) {//update playerinfo
                                    p.setUdpIp(IPAddress);
                                    p.setUdpPort(port);
                                } else { //send the message to other players
                                    if (p.getUdpIp() != null) {
                                        byte[] responseData = sentence.getBytes();
                                        DatagramPacket sendPacket = new DatagramPacket(responseData, responseData.length, p.getUdpIp(), p.getUdpPort());
                                       // System.out.println("Sending to: " + p.getEmail());
                                        socket.send(sendPacket);
                                    }

                                }
                            }
                        }
                    }
                }else if(splitted[0].equals("GAMEOVER")){
                    System.out.println("GAME OVER");
                    System.out.println(sentence);
                    int gameId = Integer.parseInt(splitted[1].trim());
                    int playerId = Integer.parseInt(splitted[2].trim());
                    for (Game g : GameHandler.getInstance().getGames()) {
                        if (g.getId() == gameId && g.getGameState().equals(GameState.INGAME)) {
                            for (Player p : g.getPlayers()) {
                                if (p.getId() == playerId) {//update playerinfo
                                    p.setUdpIp(IPAddress);
                                    p.setUdpPort(port);
                                } else { //send the message to other players
                                    if (p.getUdpIp() != null) {
                                        byte[] responseData = sentence.getBytes();
                                        DatagramPacket sendPacket = new DatagramPacket(responseData, responseData.length, p.getUdpIp(), p.getUdpPort());
                                        socket.send(sendPacket);
                                    }

                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                System.out.println(e);
            }
        }

    }

}
