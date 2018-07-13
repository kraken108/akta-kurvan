package com.example.p_jakdan_hjalmo.aktakurvan_mobile.CloudMessaging;

/**
 * Created by Jakob on 2017-12-20.
 */

public class Token {
    private static Token token;

    public static Token getInstance(){
        if(token == null){
            token = new Token();
        }

        return token;
    }

    private String theToken;

    public Token() {
        this.theToken = "";
    }

    public String getTheToken() {
        return theToken;
    }

    public void setTheToken(String theToken) {
        this.theToken = theToken;
    }
}
