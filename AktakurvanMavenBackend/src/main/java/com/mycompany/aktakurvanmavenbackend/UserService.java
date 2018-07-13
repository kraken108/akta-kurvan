/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.aktakurvanmavenbackend;

import ViewModel.VUser;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jakob
 */
public class UserService extends AbstractVerticle {

    private FirebaseApp firebaseApp;

    @Override
    public void start() {

        Router router = Router.router(vertx);

        //testa om servicen fungerar
        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response
                    .putHeader("content-type", "text/html")
                    .end("<h1>Hello from my first Vert.x 3 application</h1>");
        });

        //skapa bodyhandler för att kunna läsa body i requests
        router.route(HttpMethod.POST, "/*").handler(BodyHandler.create());

        router.post("/api/login").handler(this::login);
        router.post("/api/updatedevicetoken").handler(this::updateDeviceToken);
        router.post("/api/addfriend").handler(this::addFriend);
        router.post("/api/getpendingfriends").handler(this::getPendingFriends);
        router.post("/api/respondfriendrequest").handler(this::respondFriendRequest);
        router.post("/api/getfriends").handler(this::getFriends);
        router.post("/api/challenge").handler(this::challengeFriend);

        //Initiera firebase
        FileInputStream serviceAccount;
        try {
            serviceAccount = new FileInputStream("D:\\Firebase\\mobilapp-67b55-firebase-adminsdk-ijhds-43eb2c5271.json");
            FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://mobilapp-67b55.firebaseio.com")
                    .build();
            firebaseApp = FirebaseApp.initializeApp(options);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Couldnt start User service");
        } catch (IOException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Couldnt start User service");
        }

        vertx.createHttpServer().requestHandler(router::accept).listen(7932);
        System.out.println("User service started");

    }

    private void challengeFriend(RoutingContext rc) {
        System.out.println("Challenging friend");
        VUser u = Json.decodeValue(rc.getBodyAsString(), VUser.class);
        startBackgroundTask(RestFunctions.CHALLENGEFRIEND, rc, u);

    }

    private void addFriend(RoutingContext rc) {
        System.out.println("Adding fwend");
        VUser u = Json.decodeValue(rc.getBodyAsString(), VUser.class);
        startBackgroundTask(RestFunctions.ADDFRIEND, rc, u);

    }

    private void login(RoutingContext rc) {
        VUser u = Json.decodeValue(rc.getBodyAsString(), VUser.class);
        startBackgroundTask(RestFunctions.LOGIN, rc, u);
    }

    private void respondFriendRequest(RoutingContext rc) {
        VUser u = Json.decodeValue(rc.getBodyAsString(), VUser.class);
        startBackgroundTask(RestFunctions.RESPONDFRIENDREQUEST, rc, u);

    }

    private void getFriends(RoutingContext rc) {
        VUser u = Json.decodeValue(rc.getBodyAsString(), VUser.class);
        startBackgroundTask(RestFunctions.GETFRIENDS, rc, u);

    }

    private void updateDeviceToken(RoutingContext rc) {
        System.out.println("Attempting to update device token");
        VUser u = Json.decodeValue(rc.getBodyAsString(), VUser.class);

        startBackgroundTask(RestFunctions.UPDATEDEVICETOKEN, rc, u);

    }

    private void getPendingFriends(RoutingContext rc) {
        System.out.println("Retrieving all posts");
        VUser u = Json.decodeValue(rc.getBodyAsString(), VUser.class);
        startBackgroundTask(RestFunctions.GETPENDINGFRIENDS, rc, u);
    }

    private void startBackgroundTask(RestFunctions function, RoutingContext rc, VUser u) {
        EventListenerImpl listener = new EventListenerImpl(rc, u);
        VerifyBackgroundTask bgTask = new VerifyBackgroundTask();
        bgTask.registerListener(listener);
        try {
            bgTask.verifyUser(u.getTokenId(), u.getEmail(), FirebaseAuth.getInstance(firebaseApp), function);
        } catch (Exception ex) {
            rc.response().setStatusCode(404).putHeader("content-type", "text/html").end("Unsuccessful login");
            System.out.println(ex);
        }
    }
}
