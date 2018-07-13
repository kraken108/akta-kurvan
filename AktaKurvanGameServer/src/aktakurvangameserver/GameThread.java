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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jakob
 */
public class GameThread implements Runnable {

    private Game theGame;

    public GameThread(Game theGame) {
        this.theGame = theGame;
    }

    @Override
    public void run() {
        //are you ready?

        for (Player p : theGame.getPlayers()) {
            PrintWriter newOut = null;
            try {
                newOut = new PrintWriter(p.getSocket().getOutputStream(), true);
                newOut.println("READY");
            } catch (Exception e) {
                System.out.println(e);
            } finally {
              /*  if (newOut != null) {
                    newOut.close();
                }*/
            }
        }

        for (Player p : theGame.getPlayers()) {
            BufferedReader newIn = null;
            try {
                p.getSocket().setSoTimeout(20000);
                newIn = new BufferedReader(new InputStreamReader(p.getSocket().getInputStream()));
                String inc = newIn.readLine();
                p.getSocket().setSoTimeout(0);
                if(inc == null){
                    System.out.println("Received null, terminating thread!");
                    return;
                }
                if (!inc.startsWith("IMREADY")) {
                    System.out.println("Didnt receive IMREADY. Terminating game thread!");
                    return;
                }
            } catch (IOException ex) {
                System.out.println(ex);
                System.out.println("Terminating game thread");
            } finally {
               /* try {
                    newIn.close();
                } catch (IOException ex) {
                }*/
            }
        }

        System.out.println("ALL PLAYERS READY! SENDING GO MESSAGE");

        int i = 0;
        float spawnX = new Float(0.33);
        float spawnY = new Float(0.66);
        
        
        for (Player p : theGame.getPlayers()) {
            PrintWriter newOut = null;
            p.setId(i); //set the id of the player
            try {
                newOut = new PrintWriter(p.getSocket().getOutputStream(), true);
                newOut.println("GO " + GameHandler.getInstance().getUdpPort() + " " + i
                + " " + spawnX + " " + spawnY);
            } catch (Exception e) {
                System.out.println(e);
            } finally {
            /*    if (newOut != null) {
                    newOut.close();
                }*/
            }
            spawnX = new Float(0.66);
            i++;
        }

        //if ready send startgame
    }

}
