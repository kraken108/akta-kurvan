/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ViewModel;

/**
 *
 * @author Jakob
 */
public class VUser {
    private String email;
    private String tokenId;
    private String deviceToken;
    private String friendToAdd;
    private boolean didAccept;
    
    public VUser(String email, String tokenId, String deviceToken,String friendToAdd,boolean didAccept) {
        this.email = email;
        this.tokenId = tokenId;
        this.deviceToken = deviceToken;
        this.friendToAdd = friendToAdd;
        this.didAccept = didAccept;
    }

    public VUser() {
    }

    public boolean isDidAccept() {
        return didAccept;
    }

    public void setDidAccept(boolean didAccept) {
        this.didAccept = didAccept;
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

    public String getFriendToAdd() {
        return friendToAdd;
    }

    public void setFriendToAdd(String friendToAdd) {
        this.friendToAdd = friendToAdd;
    }
    
    
    
    
}
