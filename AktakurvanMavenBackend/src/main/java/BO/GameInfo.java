/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BO;

/**
 *
 * @author Jakob
 */
public class GameInfo {
    private static GameInfo theModel;
    private final String ip;
    private final int clientPort;
    private final int backendPort;
    
    
    public static GameInfo getInstance(){
        if(theModel == null){
            theModel = new GameInfo();
        }
        return theModel;
    }
    
    private GameInfo(){
        ip = "192.168.43.5";
        clientPort = 8212;
        backendPort = 9212;
    }
    

    public String getIp() {
        return ip;
    }

    public int getClientPort() {
        return clientPort;
    }

    public int getBackendPort() {
        return backendPort;
    }
    
    
}
