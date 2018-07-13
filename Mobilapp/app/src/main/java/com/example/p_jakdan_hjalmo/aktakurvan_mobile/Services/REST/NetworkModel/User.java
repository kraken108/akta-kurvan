package com.example.p_jakdan_hjalmo.aktakurvan_mobile.Services.REST.NetworkModel;

/**
 * Created by Jakob on 2017-12-21.
 */

public class User {
    private String email;
    private String tokenId;
    private String deviceToken;
    private String friendToAdd;
    private boolean didAccept;


    public User(String email, String tokenId, String deviceToken,String friendToAdd,boolean didAccept){
        this.email = email;
        this.tokenId = tokenId;
        this.deviceToken = deviceToken;
        this.friendToAdd = friendToAdd;
        this.didAccept = didAccept;
    }

    public User(String email, String tokenId) {
        this.email = email;
        this.tokenId = tokenId;
        this.deviceToken = "";
        this.friendToAdd = "";
        didAccept = false;
    }

    public User(String email, String tokenId, String friendToAdd){
        this.email = email;
        this.tokenId = tokenId;
        this.deviceToken = "";
        this.friendToAdd = friendToAdd;
        didAccept = false;
    }

    public User(String email, String tokenId, String friendToAdd, boolean didAccept){
        this.email = email;
        this.tokenId = tokenId;
        this.friendToAdd = friendToAdd;
        this.didAccept = didAccept;
    }

    public String getFriendToAdd() {
        return friendToAdd;
    }

    public void setFriendToAdd(String friendToAdd) {
        this.friendToAdd = friendToAdd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public boolean isDidAccept() {
        return didAccept;
    }

    public void setDidAccept(boolean didAccept) {
        this.didAccept = didAccept;
    }
}
