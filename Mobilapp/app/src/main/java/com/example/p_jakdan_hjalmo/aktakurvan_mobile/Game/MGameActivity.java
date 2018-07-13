package com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.p_jakdan_hjalmo.aktakurvan_mobile.CloudMessaging.NotificationModel;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.Model.Game;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.Model.GameModel;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.Network.Sender;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.ViewModel.ScreenInfo;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MGameActivity extends AppCompatActivity {

    private ProgressDialog dialog;
    private FirebaseAuth mAuth;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mgame);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        GameModel.getInstance().clearModel();
        GameModel.getInstance().setActive(true);
        GameModel.getInstance().setMultiplayer(true);
        game = new Game();
        NotificationModel.getInstance().setActivity(this);

        Drawable playerSprite = getResources().getDrawable(R.drawable.theblue);
        ScreenInfo.getInstance().setPlayerDrawable(playerSprite);
        ScreenInfo.getInstance().setEnemyDrawable(getResources().getDrawable(R.drawable.thered));

        dialog = ProgressDialog.show(MGameActivity.this, "",
                "Contacting game server..", true);
        try{

           // Thread sender = new Thread(new Sender(s,"JOINGAME " + currentUser.getEmail() + " " + ServerInfo.getInstance().getGameId()));
            Thread sender = new Thread(new Sender(currentUser.getEmail(),game,this,dialog,new Handler()));
            sender.start();

        }catch(Exception e){
            System.out.println(e);
            System.out.println("FAILED TO SEND MESSAGE");
            //TERMINATE AND CHANGE VIEW
        }

        //när man fått kontakt, ändra till "Waiting for other players"
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GameModel.getInstance().setActive(false);
    }
}
