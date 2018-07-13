package com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.p_jakdan_hjalmo.aktakurvan_mobile.CloudMessaging.NotificationModel;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.GameController.GameState;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.Model.GameModel;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.ViewModel.ScreenInfo;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.R;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.Button;

public class GameActivity extends AppCompatActivity {


    private GameView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if(!GameModel.getInstance().isActive()){
            GameModel.getInstance().clearModel();
        }

        GameModel.getInstance().setActive(true);

        NotificationModel.getInstance().setActivity(this);

        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        Drawable playerSprite = getResources().getDrawable(R.drawable.theblue);
        ScreenInfo.getInstance().setEnemyDrawable(getResources().getDrawable(R.drawable.thered));
        ScreenInfo.getInstance().setPlayerDrawable(playerSprite);
        view = new GameView(this, metrics.widthPixels, metrics.heightPixels);
        this.setContentView(view);
    }


    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GameModel.getInstance().setActive(false);
    }
}
