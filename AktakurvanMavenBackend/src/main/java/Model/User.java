/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jakob
 */
@Entity
@Table(name = "User")
@XmlRootElement
public class User implements Serializable {

    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "devicetoken")
    private String deviceToken;

    @OneToMany(targetEntity = Friendship.class, mappedBy = "sendingFriend")
    private List<Friendship> sentFriendRequests;
    
    @OneToMany(targetEntity = Friendship.class, mappedBy = "receivingFriend")
    private List<Friendship> receivedFriendRequest;
    
    
    public User(String email, String deviceToken) {
        this.email = email;
        this.deviceToken = deviceToken;
        sentFriendRequests = new ArrayList<>();
        receivedFriendRequest = new ArrayList<>();
    }

    public User(String email) {
        this.email = email;
        deviceToken = "";
        sentFriendRequests = new ArrayList<>();
        receivedFriendRequest = new ArrayList<>();
    }

    public User() {
        this.email = "";
        deviceToken = "";
        sentFriendRequests = new ArrayList<>();
        receivedFriendRequest = new ArrayList<>();
    }
    
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public List<Friendship> getSentFriendRequests() {
        return sentFriendRequests;
    }

    public void setSentFriendRequests(List<Friendship> sentFriendRequests) {
        this.sentFriendRequests = sentFriendRequests;
    }

    public List<Friendship> getReceivedFriendRequest() {
        return receivedFriendRequest;
    }

    public void setReceivedFriendRequest(List<Friendship> receivedFriendRequest) {
        this.receivedFriendRequest = receivedFriendRequest;
    }

    
    
    
    
}
