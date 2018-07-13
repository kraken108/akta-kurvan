/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.aktakurvanmavenbackend;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;

/**
 *
 * @author Jakob
 */
public class VerifyBackgroundTask {

    private EventListener listener;

    public void registerListener(EventListener listener) {
        this.listener = listener;
    }

    public void verifyUser(String tokenId, String email, FirebaseAuth auth, RestFunctions function) throws Exception {

        new Thread(new verifyThread(tokenId, email, auth, listener, function)).start();

    }

    private class verifyThread implements Runnable {

        private String idToken;
        private String email;
        private FirebaseAuth auth;
        private EventListener listener;
        private RestFunctions function;

        public verifyThread(String idToken, String email, FirebaseAuth auth, EventListener listener, RestFunctions function) {
            this.idToken = idToken;
            this.email = email;
            this.auth = auth;
            this.listener = listener;
            this.function = function;
        }

        @Override
        public void run() {
            try {
                System.out.println("Trying to verify");
                ApiFuture<FirebaseToken> future = auth.verifyIdTokenAsync(idToken);
                ApiFutures.addCallback(future, new ApiFutureCallback() {
                    @Override
                    public void onFailure(Throwable thrwbl) {
                        listener.onFailure();
                    }

                    @Override
                    public void onSuccess(Object v) {
                        FirebaseToken token = (FirebaseToken) v;
                        System.out.println(token.getEmail());
                        if (token.getEmail().equals(email)) {
                            listener.login();
                            switch (function) {
                                case LOGIN:
                                    listener.login();
                                    break;
                                case ADDFRIEND:
                                    listener.addFriend();
                                    break;
                                case CHALLENGEFRIEND:
                                    listener.challengeFriend();
                                    break;
                                case UPDATEDEVICETOKEN:
                                    listener.updateDeviceToken();
                                    break;
                                case GETFRIENDS:
                                    listener.getFriends();
                                    break;
                                case GETPENDINGFRIENDS:
                                    listener.getPendingFriend();
                                    break;
                                case RESPONDFRIENDREQUEST:
                                    listener.respondFriendRequest();
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            listener.onFailure();
                        }

                    }
                });

            } catch (Exception e) {
                System.out.println(e);
            }
        }

    }
}
