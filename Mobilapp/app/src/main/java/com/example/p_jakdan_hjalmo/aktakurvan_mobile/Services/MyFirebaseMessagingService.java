package com.example.p_jakdan_hjalmo.aktakurvan_mobile.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.p_jakdan_hjalmo.aktakurvan_mobile.AddFriendActivity;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.CloudMessaging.NotificationModel;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.Network.ServerInfo;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.GameInvitePopup;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.LoginActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String TAG = "MessagingService";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...


        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        System.out.println("NEW CLOUD MESSAGE");

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
               // scheduleJob();
            } else {
                // Handle message within 10 seconds
               // handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            System.out.println("MESSAGE TITLE: " + remoteMessage.getNotification().getTitle());
            System.out.println("MESSAGE BODY: " + remoteMessage.getNotification().getBody());


            String[] splitted = remoteMessage.getNotification().getBody().split(" ");
            if(splitted[0].equals("CHALLENGE") && splitted.length > 4){
                try{
                    String challenger = splitted[1];
                    String serverIp = splitted[2];
                    int serverPort = Integer.parseInt(splitted[3]);
                    int gameId = Integer.parseInt(splitted[4]);

                    ServerInfo.getInstance().setGameId(gameId);
                    ServerInfo.getInstance().setServerIp(serverIp);
                    ServerInfo.getInstance().setTcpPort(serverPort);
                    ServerInfo.getInstance().setEnemy(challenger);

                    Intent myIntent = new Intent(this, GameInvitePopup.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(myIntent);
                }catch(Exception e){
                    System.out.println(e);
                }
            }


        }



        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
}
