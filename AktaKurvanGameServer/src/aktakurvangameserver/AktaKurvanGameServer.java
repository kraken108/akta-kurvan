/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aktakurvangameserver;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jakob
 */
public class AktaKurvanGameServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Thread backendThread = new Thread(new BackendCommunicator());
            backendThread.start();
            System.out.println("Started backendThread");
            
            Thread frontendThread = new Thread(new FrontendCommunicator());
            frontendThread.start();
            System.out.println("Started frontendThread");
            
            Thread udpListener = new Thread(new UdpListener());
            udpListener.start();
            System.out.println("Started udpListener");
            
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    
}
