package com.example.p_jakdan_hjalmo.aktakurvan_mobile;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.p_jakdan_hjalmo.aktakurvan_mobile.CloudMessaging.NotificationModel;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Services.REST.NetworkModel.User;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Services.REST.UserRestClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

/**
 * Created by Jakob on 2017-12-30.
 */

public class FriendPendingTab  extends Fragment {

    private FirebaseAuth mAuth;
    private ProgressDialog dialog;
    private final String TAG = "FriendPending";
    private ListView friendPendingList;
    private Button acceptButton, declineButton;
    private int currentlySelected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.friendpendingtab, container, false);
        friendPendingList = (ListView) rootView.findViewById(R.id.pendingFriendsList);
        acceptButton = (Button) rootView.findViewById(R.id.acceptFriendButton);
        declineButton = (Button) rootView.findViewById(R.id.declineFriendButton);
        acceptButton.setVisibility(View.INVISIBLE);
        declineButton.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();

        currentlySelected = 0;

        friendPendingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentlySelected = i;
                acceptButton.setVisibility(View.VISIBLE);
                declineButton.setVisibility(View.VISIBLE);
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = ProgressDialog.show(getActivity(), "",
                        "Loading. Please wait...", true);
                respondFriendRequest(true);
            }
        });

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = ProgressDialog.show(getActivity(), "",
                        "Loading. Please wait...", true);
                respondFriendRequest(false);
            }
        });

        dialog = ProgressDialog.show(getActivity(), "",
                "Loading. Please wait...", true);
        getPendingFriends();

        NotificationModel.getInstance().setActivity(getActivity());


        return rootView;
    }

    public void respondFriendRequest(final boolean didAccept){
        System.out.println("Trying to respond friend request");
        try {
            // Send friend request
            final FirebaseUser user = mAuth.getCurrentUser();

            user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                @Override
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                    if (task.isSuccessful()) {
                        sendRespondFriendRequest(new User(user.getEmail(), task.getResult().getToken(),(String)friendPendingList.getItemAtPosition(currentlySelected),didAccept));
                    } else {
                        Log.i(TAG, "COULDNT GET TOKEN FFS");
                        dialog.dismiss();
                    }
                }
            });

        } catch (Exception e) {
            dialog.dismiss();
            showToast(e.getMessage());
            Log.i(TAG, e.toString());
        }
    }

    private void sendRespondFriendRequest(User user){
        System.out.println("RESPONDING TO REQUEST FROM: " + user.getFriendToAdd());
        UserRestClient client = new UserRestClient();
        client.sendRespondFriendRequest(user, getActivity(), dialog);
    }

    public void getPendingFriends() {
        System.out.println("trying to get pending friends");
        try {
            // Send friend request
            final FirebaseUser user = mAuth.getCurrentUser();

            user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                @Override
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                    if (task.isSuccessful()) {
                        sendGetPendingFriends(new User(user.getEmail(), task.getResult().getToken()));
                    } else {
                        Log.i(TAG, "COULDNT GET TOKEN FFS");
                        dialog.dismiss();
                    }
                }
            });

        } catch (Exception e) {
            dialog.dismiss();
            showToast(e.getMessage());
            Log.i(TAG, e.toString());
        }


    }

    private void sendGetPendingFriends(User user) {
        System.out.println("BLABLABLA: " + user.getFriendToAdd());
        UserRestClient client = new UserRestClient();
        client.sendGetPendingFriends(user, getActivity(), dialog,friendPendingList);
    }

    private void showToast(String msg) {
        Toast toast = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}