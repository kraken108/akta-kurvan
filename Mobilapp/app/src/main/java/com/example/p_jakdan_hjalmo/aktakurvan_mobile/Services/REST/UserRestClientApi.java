package com.example.p_jakdan_hjalmo.aktakurvan_mobile.Services.REST;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Services.REST.NetworkModel.*;

import java.util.List;

/**
 * Created by Jakob on 2017-12-21.
 */

public interface UserRestClientApi {

    @POST("updatedevicetoken")
    Call<User> updatedevicetoken(@Body User user);

    @POST("login")
    Call<User> login(@Body User user);

    @POST("addfriend")
    Call<User> addfriend(@Body User user);

    @POST("getpendingfriends")
    Call<List<User>> getpendingfriends(@Body User user);

    @POST("respondfriendrequest")
    Call<User> respondfriendrequest(@Body User user);

    @POST("getfriends")
    Call<List<User>> getfriends(@Body User user);

    @POST("challenge")
    Call<NetworkGame> challengeFriend(@Body User user);


}
