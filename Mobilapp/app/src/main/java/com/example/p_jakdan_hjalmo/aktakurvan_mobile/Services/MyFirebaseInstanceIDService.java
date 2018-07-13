package com.example.p_jakdan_hjalmo.aktakurvan_mobile.Services;

import android.util.Log;

import com.example.p_jakdan_hjalmo.aktakurvan_mobile.CloudMessaging.Token;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Jakob on 2017-12-20.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private final String TAG = "IDService";

    @Override
    public void onTokenRefresh(){
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        Token.getInstance().setTheToken(refreshedToken);
    }
}
