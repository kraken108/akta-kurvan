package com.example.p_jakdan_hjalmo.aktakurvan_mobile.Services.REST;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.p_jakdan_hjalmo.aktakurvan_mobile.FriendsTabActivity;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.MGameActivity;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.Network.ServerInfo;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.MainActivity;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Services.REST.NetworkModel.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jakob on 2017-12-22.
 */

public class UserRestClient {

    private final String TAG = "UserRestClient";
    private final String BASEURL = "http://78.71.246.211:7932/api/";


    public void sendUpdateDeviceTokenRequest(User user, AppCompatActivity activity, final ProgressDialog dialog) {

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        UserRestClientApi client = retrofit.create(UserRestClientApi.class);
        Call<User> call = client.updatedevicetoken(user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                // dialog.dismiss();
                if (response.code() == 302) {
                    Log.i(TAG, "Great success! Device token updated");
                } else {
                    Log.i(TAG, "Device token update failed. Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                //  dialog.dismiss();
                Log.i(TAG, t.toString());
            }
        });
    }

    public void challengeFriend(User user, final FragmentActivity activity, final ProgressDialog dialog) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        UserRestClientApi client = retrofit.create(UserRestClientApi.class);
        Call<NetworkGame> call = client.challengeFriend(user);

        call.enqueue(new Callback<NetworkGame>() {
            @Override
            public void onResponse(Call<NetworkGame> call, Response<NetworkGame> response) {

                dialog.dismiss();

                Log.i(TAG, "Great success! Challenged friend");
                System.out.println("Great success! Challenged friend");

                // TODO: Connect to gameserver using info from response and switch to game waiting activity.
                NetworkGame game = response.body();

                try {
                    ServerInfo.getInstance().setGameId(game.getId());
                    ServerInfo.getInstance().setServerIp(game.getIp());
                    ServerInfo.getInstance().setTcpPort(game.getPort());
                } catch (Exception e) {
                    System.out.println(e);
                }

                Intent myIntent = new Intent(activity, MGameActivity.class);
                activity.startActivity(myIntent);


            }

            @Override
            public void onFailure(Call<NetworkGame> call, Throwable t) {
                dialog.dismiss();
                Log.i(TAG, t.toString());
                showToast("Something went wrong with rest call: " + t.toString(), activity);
            }
        });
    }

    public void sendLoginRequest(User user, final AppCompatActivity activity, final ProgressDialog dialog) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        UserRestClientApi client = retrofit.create(UserRestClientApi.class);
        Call<User> call = client.login(user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                dialog.dismiss();
                if (response.code() == 302) {
                    Log.i(TAG, "Great success! Logged in");
                    System.out.println("Great success! Logged in");
                    Intent myIntent = new Intent(activity, MainActivity.class);
                    activity.startActivity(myIntent);
                } else {
                    Log.i(TAG, "Login failed. Error: " + response.code());
                    showToast("Login failed: " + response.code(), activity);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                dialog.dismiss();
                Log.i(TAG, t.toString());
                showToast("Something went wrong with rest call: " + t.toString(), activity);
            }
        });
    }


    public void sendFriendRequest(User user, final AppCompatActivity activity, final ProgressDialog dialog) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        UserRestClientApi client = retrofit.create(UserRestClientApi.class);
        Call<User> call = client.addfriend(user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                dialog.dismiss();
                if (response.code() == 302) {
                    System.out.println("Great success! Friend request sent");
                    showToast("Friend request sent!", activity);
                    Intent myIntent = new Intent(activity, FriendsTabActivity.class);
                    activity.startActivity(myIntent);
                } else {
                    System.out.println("Failure!,Received 404 when adding friend");
                    showToast("Friend request failed: " + response.code(), activity);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                dialog.dismiss();
                Log.i(TAG, t.toString());
                showToast("Something went wrong with rest call: " + t.toString(), activity);
            }
        });
    }

    public void sendRespondFriendRequest(User user, final FragmentActivity activity, final ProgressDialog dialog) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        UserRestClientApi client = retrofit.create(UserRestClientApi.class);
        Call<User> call = client.respondfriendrequest(user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                dialog.dismiss();
                if (response.code() == 302) {
                    System.out.println("Great success! Responded to friend request");
                    showToast("Responded to friend request!", activity);
                    Intent myIntent = new Intent(activity, FriendsTabActivity.class);
                    activity.startActivity(myIntent);
                } else {
                    System.out.println("Failure!,Received 404 when responding to friend request");
                    showToast("Friend request response failed: " + response.code(), activity);
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                dialog.dismiss();
                Log.i(TAG, t.toString());
                showToast("Something went wrong with rest call: " + t.toString(), activity);
            }
        });
    }

    public void sendGetFriends(User user, final FragmentActivity activity, final ProgressDialog dialog, final ListView friendList) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        UserRestClientApi client = retrofit.create(UserRestClientApi.class);
        Call<List<User>> call = client.getfriends(user);

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                dialog.dismiss();

                List<User> responseList = response.body();
                List<String> stringList = new ArrayList<>();

                if (responseList != null) {
                    for (User u : responseList) {
                        stringList.add(u.getEmail());
                    }
                }

                ArrayAdapter ad = new ArrayAdapter(activity,
                        android.R.layout.simple_expandable_list_item_1, stringList);
                friendList.setAdapter(ad);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                dialog.dismiss();
                Log.i(TAG, t.toString());
                showToast("Something went wrong with rest call: " + t.toString(), activity);
            }
        });
    }

    public void sendGetPendingFriends(User user, final FragmentActivity activity, final ProgressDialog dialog, final ListView friendList) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        UserRestClientApi client = retrofit.create(UserRestClientApi.class);
        Call<List<User>> call = client.getpendingfriends(user);

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                dialog.dismiss();

                List<User> responseList = response.body();
                List<String> stringList = new ArrayList<>();

                for (User u : responseList) {
                    stringList.add(u.getEmail());
                }
                ArrayAdapter ad = new ArrayAdapter(activity,
                        android.R.layout.simple_expandable_list_item_1, stringList);
                friendList.setAdapter(ad);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                dialog.dismiss();
                Log.i(TAG, t.toString());
                showToast("Something went wrong with rest call: " + t.toString(), activity);
            }
        });
    }

    private void showToast(String msg, FragmentActivity activity) {
        Toast toast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void showToast(String msg, AppCompatActivity activity) {
        Toast toast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
