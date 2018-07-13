package com.example.p_jakdan_hjalmo.aktakurvan_mobile;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.p_jakdan_hjalmo.aktakurvan_mobile.CloudMessaging.NotificationModel;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Services.REST.NetworkModel.User;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Services.REST.UserRestClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.w3c.dom.Text;

public class AddFriendActivity extends AppCompatActivity {

    private TextView emailField;
    private Button addButton;
    private FirebaseAuth mAuth;
    private ProgressDialog dialog;
    private final String TAG = "AddFriendA";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_add_friend);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        mAuth = FirebaseAuth.getInstance();

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.6), (int) (height * 0.4));

        emailField = (TextView) findViewById(R.id.addFriendEmail);
        addButton = (Button) findViewById(R.id.addFriend);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String friendToAdd = emailField.getText().toString();
                if(friendToAdd.equals("")){
                    showToast("Please enter an email");
                }else{
                    dialog = ProgressDialog.show(AddFriendActivity.this, "",
                            "Loading. Please wait...", true);
                    addFriend(friendToAdd);
                }
            }
        });

        NotificationModel.getInstance().setActivity(this);
    }

    public void addFriend(final String friendToAdd) {
        System.out.println("Trying to add friend");
        try {
            // Send friend request
            final FirebaseUser user = mAuth.getCurrentUser();

            user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                @Override
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                    if (task.isSuccessful()) {
                        Log.i(TAG, "DATOKEN:");
                        Log.i(TAG, task.getResult().getToken());
                        sendAddFriendRequest(new User(user.getEmail(), task.getResult().getToken(), friendToAdd));
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

    private void sendAddFriendRequest(User user) {
        System.out.println("BLABLABLA: " + user.getFriendToAdd());
        UserRestClient client = new UserRestClient();
        client.sendFriendRequest(user, this, dialog);
    }

    private void showToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
