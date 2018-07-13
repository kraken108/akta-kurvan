package com.example.p_jakdan_hjalmo.aktakurvan_mobile.CloudMessaging;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Jakob on 2018-01-05.
 */

public class NotificationModel {

    private static NotificationModel theModel;
    private FragmentActivity activity;
    public static NotificationModel getInstance(){
        if(theModel == null){
            theModel = new NotificationModel();
        }
        return theModel;
    }

    private NotificationModel(){
    }

    public FragmentActivity getActivity() {
        return activity;
    }

    public void setActivity(FragmentActivity activity) {
        this.activity = activity;
    }
}
