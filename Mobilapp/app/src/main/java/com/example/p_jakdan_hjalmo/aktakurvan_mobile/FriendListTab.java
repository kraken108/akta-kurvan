package com.example.p_jakdan_hjalmo.aktakurvan_mobile;

/**
 * Created by Jakob on 2017-12-30.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.p_jakdan_hjalmo.aktakurvan_mobile.CloudMessaging.NotificationModel;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Services.REST.NetworkModel.User;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Services.REST.UserRestClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

public class FriendListTab extends Fragment{

    private FirebaseAuth mAuth;
    private ProgressDialog dialog;
    private final String TAG = "FriendList";
    private ListView friendList;
    private Button addFriendButton,challengeButton;
    private int currentlySelected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.friendlisttab, container, false);
        mAuth = FirebaseAuth.getInstance();
        friendList = (ListView) rootView.findViewById(R.id.friendListView);
        currentlySelected = 0;

        challengeButton = (Button) rootView.findViewById(R.id.challengeButton);
        challengeButton.setVisibility(View.INVISIBLE);


        friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentlySelected = i;
                challengeButton.setVisibility(View.VISIBLE);
            }
        });



        addFriendButton = (Button) rootView.findViewById(R.id.addFriendButton);
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),AddFriendActivity.class));
            }
        });

        challengeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = ProgressDialog.show(getActivity(), "",
                        "Loading. Please wait...", true);
                try {
                    // Send friend request
                    final FirebaseUser user = mAuth.getCurrentUser();

                    user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        @Override
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                User u = new User(user.getEmail(),task.getResult().getToken());
                                u.setFriendToAdd(friendList.getItemAtPosition(currentlySelected).toString());
                                sendChallengeFriend(u);
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
        });

        dialog = ProgressDialog.show(getActivity(), "",
                "Loading. Please wait...", true);
        getFriends();

        NotificationModel.getInstance().setActivity(getActivity());

        return rootView;
    }

    public void getFriends() {
        System.out.println("trying to get pending friends");
        try {
            // Send friend request
            final FirebaseUser user = mAuth.getCurrentUser();

            user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                @Override
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                    if (task.isSuccessful()) {
                        sendGetFriends(new User(user.getEmail(), task.getResult().getToken()));
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


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void sendChallengeFriend(User user){
        UserRestClient client = new UserRestClient();
        client.challengeFriend(user,getActivity(),dialog);
    }

    private void sendGetFriends(User user) {
        System.out.println("BLABLABLA: " + user.getFriendToAdd());
        UserRestClient client = new UserRestClient();
        client.sendGetFriends(user, getActivity(), dialog,friendList);
    }

    private void showToast(String msg) {
        Toast toast = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
