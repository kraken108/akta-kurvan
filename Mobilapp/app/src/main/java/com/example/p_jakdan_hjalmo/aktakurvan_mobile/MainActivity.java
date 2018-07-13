package com.example.p_jakdan_hjalmo.aktakurvan_mobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.p_jakdan_hjalmo.aktakurvan_mobile.CloudMessaging.NotificationModel;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.GameActivity;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.Model.GameModel;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Services.REST.NetworkModel.User;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Services.REST.UserRestClient;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Services.REST.UserRestClientApi;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Services.TokenFileReader;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Services.TokenSaver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView tokenTextView;
    private Button showTokenButton;
    private TextView logoutTextView;
    private Button friendsButton,playButton;

    private ProgressDialog dialog;

    private FirebaseAuth mAuth;

    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GameModel.getInstance().setActive(false);

        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        NotificationModel.getInstance().setActivity(this);
    }

    @Override
    public void onStart(){
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user){
        if(user == null){
            initiateLoginScreen();
        }else{
            initiateMainMenu();
        }
    }

    private void initiateLoginScreen(){
        Intent myIntent = new Intent(this, LoginActivity.class);
        startActivity(myIntent);
    }


    public void initiateMainMenu(){
        updateDeviceToken();
        logoutTextView = (TextView) findViewById(R.id.logoutTextView);
        friendsButton = (Button) findViewById(R.id.friendsButton);
        playButton = (Button) findViewById(R.id.playButton);

        playButton.setOnClickListener(new PlayButtonListener());
        friendsButton.setOnClickListener(new FriendsButtonListener());
        logoutTextView.setOnClickListener(new LogOutListener());

        Thread t = new Thread(new TokenSaver(this));
        t.start();
    }

    private class PlayButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent myIntent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(myIntent);
        }
    }


    private class FriendsButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent myIntent = new Intent(MainActivity.this, FriendsTabActivity.class);
            startActivity(myIntent);
        }
    }

    private class LogOutListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            mAuth.signOut();
            initiateLoginScreen();
        }
    }


    private void updateDeviceToken(){
        TokenFileReader reader = new TokenFileReader();
        try {
            final String token = reader.getTheToken(this);
            final FirebaseUser user = mAuth.getCurrentUser();
            user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                @Override
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                    if(task.isSuccessful()){
                        Log.i(TAG,"DATOKEN:");
                        Log.i(TAG,task.getResult().getToken());
                        UserRestClient client = new UserRestClient();
                        User u = new User(user.getEmail(),task.getResult().getToken());
                        u.setDeviceToken(token);
                        client.sendUpdateDeviceTokenRequest(
                                u,
                                MainActivity.this,
                                dialog
                        );
                    }else{
                        showToast("Couldnt update device token");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showToast("Couldnt update device token");
        }
    }
    private void showToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
