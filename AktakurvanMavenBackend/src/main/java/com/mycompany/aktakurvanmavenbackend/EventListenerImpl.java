/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.aktakurvanmavenbackend;

import BO.GameInfo;
import BO.UserHandler;
import ViewModel.NetworkGame;
import ViewModel.VUser;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import java.util.List;

/**
 *
 * @author Jakob
 */
public class EventListenerImpl implements EventListener {

    private RoutingContext rc;
    private VUser u;

    public EventListenerImpl(RoutingContext rc, VUser u) {
        this.rc = rc;
        this.u = u;
    }

    @Override
    public void onComplete() {

        System.out.println("FOOOKIN HELL m8");
    }

    public void login() {
        UserHandler uh = new UserHandler();
        if (uh.login(u)) {
            System.out.println("Successful login!");
            rc.response().setStatusCode(302).putHeader("content-type", "text/html").end("Successful login!");
        } else {
            System.out.println("Unsuccessful login");
            rc.response().setStatusCode(404).putHeader("content-type", "text/html").end("Unsuccessful login");
        }
    }

    @Override
    public void addFriend() {

        UserHandler uh = new UserHandler();
        if (uh.addFriend(u)) {
            System.out.println("Successfully sent friend request!");
            rc.response().setStatusCode(302).putHeader("content-type", "text/html").end("Successfully sent friend request!");
        } else {
            System.out.println("Couldnt send friend request");
            rc.response().setStatusCode(404).putHeader("content-type", "text/html").end("Couldnt send friend request");
        }
    }

    @Override
    public void challengeFriend() {

        UserHandler uh = new UserHandler();
        int response = uh.sendChallengeRequest(u);

        NetworkGame game = new NetworkGame(GameInfo.getInstance().getIp(),
                GameInfo.getInstance().getClientPort(), response);
        System.out.println(game.getId());
        System.out.println(game.getIp());
        System.out.println(game.getPort());
        rc.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(game));
    }

    @Override
    public void respondFriendRequest() {

        UserHandler uh = new UserHandler();
        if (uh.respondFriendRequest(u)) {
            System.out.println("Successfully responded to friend request");
            rc.response().setStatusCode(302).putHeader("content-type", "text/html").end("Successfully responded to friend request");
        } else {
            System.out.println("Failed to respond to friend request");
            rc.response().setStatusCode(404).putHeader("content-type", "text/html").end("Failed to respond to friend request");
        }
    }

    @Override
    public void getFriends() {

        UserHandler uh = new UserHandler();

        System.out.println("wait");
        List<VUser> list = (List<VUser>) uh.getFriends(u);

        System.out.println("done");
        rc.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(list));
        System.out.println("oki");
    }

    @Override
    public void updateDeviceToken() {

        UserHandler uh = new UserHandler();
        if (uh.updateDeviceToken(u)) {
            System.out.println("Successful device token update!");
            rc.response().setStatusCode(302).putHeader("content-type", "text/html").end("Successful device token update!");
        } else {
            System.out.println("Unsuccessful device token update");
            rc.response().setStatusCode(404).putHeader("content-type", "text/html").end("Unsuccessful device token update");
        }
    }

    @Override
    public void getPendingFriend() {
        UserHandler uh = new UserHandler();

        List<VUser> list = (List<VUser>) uh.getPendingFriends(u);

        rc.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(list));
    }

    @Override
    public void getUsersFriends() {

        UserHandler uh = new UserHandler();
        List<ViewModel.VUser> users = uh.getAllUsers(u);

        rc.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(users));
    }

    @Override
    public void onFailure() {
        System.out.println("Fail");
        rc.response().setStatusCode(404).putHeader("content-type", "text/html").end("Unsuccessful device token update");
    }

}
