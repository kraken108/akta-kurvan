package com.example.p_jakdan_hjalmo.aktakurvan_mobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import com.example.p_jakdan_hjalmo.aktakurvan_mobile.CloudMessaging.NotificationModel;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.MGameActivity;
import com.google.firebase.auth.FirebaseAuth;

public class GameInvitePopup extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button acceptButton,declineButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_invite_popup);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        mAuth = FirebaseAuth.getInstance();

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.9), (int) (height * 0.3));


        acceptButton = (Button) findViewById(R.id.acceptGameButton);
        declineButton = (Button) findViewById(R.id.declineGameButton);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(GameInvitePopup.this, MGameActivity.class);
                startActivity(myIntent);
            }
        });

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(GameInvitePopup.this, NotificationModel.getInstance().getActivity().getClass());
                startActivity(myIntent);
            }
        });
        // TODO: Add button functionality
        // TODO: Connect to gameserver using info from singleton game info model, updated by notification handler.
    }
}
