/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aktakurvangameserver;

import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author Jakob
 */
public class Player {
    private boolean connected;
    private Socket socket;
    private String email;
    private int id;
    private InetAddress udpIp;
    private int udpPort;
    
    public Player(String email){
        this.email = email;
        this.socket = null;
        this.connected = false;
        this.id = -1;
        udpIp = null;
        udpPort = 0;
    }

    public InetAddress getUdpIp() {
        return udpIp;
    }

    public void setUdpIp(InetAddress udpIp) {
        this.udpIp = udpIp;
    }

    public int getUdpPort() {
        return udpPort;
    }

    public void setUdpPort(int udpPort) {
        this.udpPort = udpPort;
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    
}
