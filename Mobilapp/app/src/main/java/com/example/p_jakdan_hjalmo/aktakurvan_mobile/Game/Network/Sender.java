package com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.Network;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.Model.ConnectionState;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.Model.Game;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.GameView;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.Model.GameModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Jakob on 2018-01-06.
 */

public class Sender implements Runnable{
    private PrintWriter out;
    private Socket s;
    private String userEmail;
    private Game game;
    private BufferedReader in;
    private AppCompatActivity activity;
    private ProgressDialog dialog;
    private Handler handler;

    public Sender(String userEmail, Game game, AppCompatActivity activity, ProgressDialog dialog,Handler handler) throws IOException {
        this.userEmail = userEmail;
        out = null;
        this.game = game;
        this.activity = activity;
        this.dialog = dialog;
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            s = new Socket(ServerInfo.getInstance().getServerIp(), ServerInfo.getInstance().getTcpPort());
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));


            out = new PrintWriter(s.getOutputStream(), true);
            out.println("JOINGAME " + userEmail + " " + ServerInfo.getInstance().getGameId());

            handler.post(new UpdateProgressDialog("Waiting for players.."));


            String incoming = in.readLine();
            if(incoming.equals("READY")){
                System.out.println("RECEIVED READY");
                out.println("IMREADY");
                game.setConnectionState(ConnectionState.WAITING);
            }else{
                //terminate
                System.out.println("DIdnt receive READY");
            }

            handler.post(new UpdateProgressDialog("Starting.."));

            incoming = in.readLine();

            if(incoming.startsWith("GO ")){
                System.out.println("RECEIVED GO");
                String[] splitted = incoming.split(" ");
                if(splitted.length > 3){
                    ServerInfo.getInstance().setUdpPort(Integer.parseInt(splitted[1]));
                    ServerInfo.getInstance().setPlayerId(Integer.parseInt(splitted[2]));
                    float startX = Float.parseFloat(splitted[3]);
                    float startY = Float.parseFloat(splitted[4]);
                    System.out.println("STARTX: " + startX);
                    System.out.println("STARTY: " + startY);
                    GameModel.getInstance().setStartX(startX);
                    GameModel.getInstance().setStartY(startY);

                    handler.post(new GoToGameView());
                    Thread t = new Thread(new UdpInitiator()); //Start udp listener and sender
                    t.start();
                    //Start game
                }
            }else{
                //terminate
                dialog.dismiss();
                System.out.println("Didnt receive GO");
            }
        } catch (Exception e) {
            dialog.dismiss();
            e.printStackTrace();
        }
    }

    private class UpdateProgressDialog implements Runnable {
        private String message;

        public UpdateProgressDialog(String message) {
            this.message = message;
        }

        public void run() {
            dialog.dismiss();
            dialog = ProgressDialog.show(activity, "",
                    message, true);
        }
    }

    private class GoToGameView implements Runnable {

        public GoToGameView(){
        }

        public void run() {
            dialog.dismiss();
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            GameView view = new GameView(activity, metrics.widthPixels, metrics.heightPixels);
            activity.setContentView(view);
        }
    }

}
