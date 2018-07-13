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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Services.REST.NetworkModel.*;
public class CreateAccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private final String TAG = "CreateAccountActivity";

    private final String url = "http://localhost:7932/api/";

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText passwordEditText2;
    private Button submitButton;
    private TextView goBackButton;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = (EditText) findViewById(R.id.c_emailTextField);
        passwordEditText = (EditText) findViewById(R.id.c_passwordTextField);
        passwordEditText2 = (EditText) findViewById(R.id.c_passwordTextField2);
        submitButton = (Button) findViewById(R.id.submitButto);
        goBackButton = (TextView) findViewById(R.id.c_go_back_button);

        submitButton.setOnClickListener(new OnSubmitListener());
        goBackButton.setOnClickListener(new OnGoBackListener());

        NotificationModel.getInstance().setActivity(this);
    }

    private class OnSubmitListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (passwordsMatch()) {
                dialog = ProgressDialog.show(CreateAccountActivity.this, "",
                        "Loading. Please wait...", true);

                createUser(emailEditText.getText().toString(), passwordEditText.getText().toString());
            } else {
                showToast("Passwords does not match.");
                clearPasswordFields();
            }
        }
    }

    private boolean passwordsMatch() {
        if (passwordEditText.getText().toString().equals(passwordEditText2.getText().toString())) {
            return true;
        } else {
            return false;
        }
    }

    private class OnGoBackListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent myIntent = new Intent(CreateAccountActivity.this, LoginActivity.class);
            startActivity(myIntent);
        }
    }

    private void createUser(String email, String password) {
        try {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                final FirebaseUser user = mAuth.getCurrentUser();

                                user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                                        if (task.isSuccessful()) {
                                            Log.i(TAG, "DATOKEN:");
                                            Log.i(TAG, task.getResult().getToken());
                                            sendLoginRequest(new User(user.getEmail(), task.getResult().getToken()));
                                        } else {
                                            Log.i(TAG, "Something went wrong. (Couldn't get token");

                                            dialog.dismiss();
                                            showToast("Something went wrong. (Couldn't get token");
                                        }
                                    }
                                });
                                //  updateUI(user);
                            } else {
                                dialog.dismiss();
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                showToast(task.getException().getMessage());
                                clearPasswordFields();
                                // updateUI(null);
                            }

                            // ...
                        }
                    });
        } catch (Exception e) {
            dialog.dismiss();
            clearPasswordFields();
            showToast(e.getMessage());
            Log.i(TAG, e.toString());
        }

    }

    private void showToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void clearFields() {
        emailEditText.setText("");
        passwordEditText.setText("");
        passwordEditText2.setText("");
    }

    private void clearPasswordFields() {
        passwordEditText.setText("");
        passwordEditText2.setText("");
    }


    private void sendLoginRequest(User user){
        UserRestClient client = new UserRestClient();
        client.sendLoginRequest(user,this,dialog);
    }
}
