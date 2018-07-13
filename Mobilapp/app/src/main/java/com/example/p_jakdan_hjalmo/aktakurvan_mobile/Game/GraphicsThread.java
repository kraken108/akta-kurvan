package com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game;

import android.content.Intent;
import android.util.Log;

import com.example.p_jakdan_hjalmo.aktakurvan_mobile.CloudMessaging.NotificationModel;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.GameController.GameState;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.Model.GameModel;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.GameInvitePopup;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.GameOverPopup;

class GraphicsThread extends Thread {

    private GameView view;
    private boolean running = true;
    private long sleepTime;
    private long timeStamp;


    GraphicsThread(GameView view, long sleepTime) {
        this.view = view;
        this.sleepTime = sleepTime;
        this.running = true;
        timeStamp = System.currentTimeMillis();
    }

    protected synchronized void setRunning(boolean b) {
        running = b;
    }

    protected synchronized boolean isRunning() {
        return running;
    }

    public void run() {
        Log.i("Var jag hÃ¤r?", "run");
        while (running) {

            view.spawnApple();
            view.collisionDetected();
            view.move();
            view.addTail();
            view.draw();

            try {
                Thread.sleep(sleepTime);

            } catch (InterruptedException ie) {
            }

            if(GameModel.getInstance().getGameState().equals(GameState.GAMEOVER)){
                running = false;
            }

        }

        if(GameModel.getInstance().getGameState().equals(GameState.GAMEOVER)){
            Intent myIntent = new Intent(NotificationModel.getInstance().getActivity(), GameOverPopup.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            NotificationModel.getInstance().getActivity().startActivity(myIntent);
        }

    }

    void requestExitAndWait() {
        running = false;
        try {
            this.join();
        } catch (InterruptedException ie) {
        }
    }

    void onWindowResize(int w, int h) {
        // Deal with change in surface size
    }
}