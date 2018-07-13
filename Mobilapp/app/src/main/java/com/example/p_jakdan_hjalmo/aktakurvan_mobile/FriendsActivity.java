package com.example.p_jakdan_hjalmo.aktakurvan_mobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.p_jakdan_hjalmo.aktakurvan_mobile.CloudMessaging.NotificationModel;

import java.util.ArrayList;

public class FriendsActivity extends AppCompatActivity {

    private ListView friendListView;
    private ArrayList<String> strings;
    private Button addFriendButton;
    private Button challengeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        strings = new ArrayList<>();
        strings.add("Tja");
        strings.add("Hur e läget");
        strings.add("this is a row m8");
        strings.add("this is a row m8");
        strings.add("this is a row m8");
        strings.add("this is a row m8");
        strings.add("this is a row m8");
        strings.add("this is a row m8");
        strings.add("this is a row m8");strings.add("this is a row m8");
        strings.add("this is a row m8");
        strings.add("this is a row m8");strings.add("this is a row m8");
        strings.add("this is a row m8");
        strings.add("this is a row m8");

        friendListView = (ListView) findViewById(R.id.friendListView);

        ArrayAdapter ad = new ArrayAdapter(FriendsActivity.this,
                android.R.layout.simple_expandable_list_item_1,strings);
        friendListView.setAdapter(ad);
        friendListView.setOnItemClickListener(new FriendListListener());
        addFriendButton = (Button) findViewById(R.id.addFriendButton);

        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FriendsActivity.this,AddFriendActivity.class));
            }
        });


        challengeButton = (Button) findViewById(R.id.challengeButton);
        challengeButton.setVisibility(View.INVISIBLE);

        NotificationModel.getInstance().setActivity(this);
    }

    private class FriendListListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            challengeButton.setVisibility(View.VISIBLE);
        }
    }
}
