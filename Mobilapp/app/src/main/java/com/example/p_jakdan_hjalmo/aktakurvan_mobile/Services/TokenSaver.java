package com.example.p_jakdan_hjalmo.aktakurvan_mobile.Services;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.p_jakdan_hjalmo.aktakurvan_mobile.CloudMessaging.Token;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.MainActivity;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Services.REST.NetworkModel.User;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Services.REST.UserRestClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jakob on 2017-12-20.
 */

public class TokenSaver implements Runnable {

    private AppCompatActivity activity;

    private final String TAG = "TokenSaver";

    private static String FILENAME = "firebasetoken";

    public TokenSaver(AppCompatActivity activity){
        this.activity = activity;
    }

    @Override
    public void run() {
        while(true){
            if(!Token.getInstance().getTheToken().equals("")){
                //save to local storage
                try {
                    writeToFile(Token.getInstance().getTheToken());
                    sendTokenToServer(Token.getInstance().getTheToken());
                    Token.getInstance().setTheToken("");
                    Log.i("TokenSaver","Saved token to local storage");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //set it to "" again
            }
        }
    }

    private void sendTokenToServer(final String token){
        try {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
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
                                null,
                                null
                        );
                    }else{
                        Log.i(TAG,"Couldnt update device token");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeToFile(String token) throws Exception {
        PrintWriter writer = null;
        OutputStream os = null;
        try {
            os = activity.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            writer = new PrintWriter(os);

            writer.println(token);


        } catch (FileNotFoundException e) {
            throw new Exception(e.toString());
        } catch (Exception e) {
            throw(e);
        } finally {
            try {
                if (writer != null) writer.close();
            } catch (Exception e) {
            }

            if(os != null){
                os.close();
            }
        }
    }


}
