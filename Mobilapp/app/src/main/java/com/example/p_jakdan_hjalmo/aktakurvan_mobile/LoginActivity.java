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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.p_jakdan_hjalmo.aktakurvan_mobile.CloudMessaging.NotificationModel;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Services.REST.UserRestClient;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Services.REST.UserRestClientApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Services.REST.NetworkModel.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signInButton;
    private TextView createTextView;
    private FirebaseAuth mAuth;
    private ProgressDialog dialog;

    private final String TAG = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = (EditText) findViewById(R.id.c_emailTextField);
        passwordEditText = (EditText) findViewById(R.id.c_passwordTextField);
        signInButton = (Button) findViewById(R.id.submitButto);
        createTextView = (TextView) findViewById(R.id.createTextField);

        signInButton.setOnClickListener(new SignInClickListener());
        createTextView.setOnClickListener(new CreateClickListener());

        mAuth = FirebaseAuth.getInstance();

        NotificationModel.getInstance().setActivity(this);
    }

    private class CreateClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent myIntent = new Intent(LoginActivity.this, CreateAccountActivity.class);
            startActivity(myIntent);
        }
    }

    private class SignInClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            dialog = ProgressDialog.show(LoginActivity.this, "",
                    "Loading. Please wait...", true);
            signIn(emailEditText.getText().toString(),passwordEditText.getText().toString());
        }
    }

    public void signIn(String email, String password){
        Log.i(TAG,"Trying to login with " + email + " and " + password);
        try{
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                final FirebaseUser user = mAuth.getCurrentUser();

                                user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                                        if(task.isSuccessful()){
                                            Log.i(TAG,"DATOKEN:");
                                            Log.i(TAG,task.getResult().getToken());
                                            sendLoginRequest(new User(user.getEmail(),task.getResult().getToken()));
                                        }else{
                                            Log.i(TAG,"COULDNT GET TOKEN FFS");
                                            dialog.dismiss();
                                        }
                                    }
                                });


                                // updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                dialog.dismiss();
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                showToast(task.getException().getMessage());

                                // updateUI(null);
                            }

                            // ...
                        }
                    });
        }catch(Exception e){
            dialog.dismiss();
            showToast(e.getMessage());
            Log.i(TAG,e.toString());
        }


    }

    private void showToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void sendLoginRequest(User user){
        UserRestClient client = new UserRestClient();
        client.sendLoginRequest(user,this,dialog);
    }
}
