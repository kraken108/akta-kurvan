/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.aktakurvanmavenbackend;

/**
 *
 * @author Jakob
 */
public interface EventListener {
    public void onComplete();

    public void login();
    public void addFriend();
    public void challengeFriend();
    public void respondFriendRequest();
    public void getFriends();
    public void updateDeviceToken();
    public void getPendingFriend();
    public void getUsersFriends();
    public void onFailure();
}
