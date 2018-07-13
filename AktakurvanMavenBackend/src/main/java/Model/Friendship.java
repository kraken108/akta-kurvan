/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javax.persistence.GenerationType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
/**
 *
 * @author Jakob
 */
@Entity
@Table(name = "Friendship")
@XmlRootElement
public class Friendship implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @ManyToOne
    private User sendingFriend;
    
    @ManyToOne
    private User receivingFriend;
    
    @Column(name = "didAccept")
    private boolean didAccept;
    
    @Column(name = "didRespond")
    private boolean didRespond;

    public Friendship(User sendingFriend, User receivingFriend, boolean didAccept, boolean didRespond) {
        this.sendingFriend = sendingFriend;
        this.receivingFriend = receivingFriend;
        this.didAccept = didAccept;
        this.didRespond = didRespond;
    }

    public Friendship(User sendingFriend, User receivingFriend) {
        this.sendingFriend = sendingFriend;
        this.receivingFriend = receivingFriend;
        this.didAccept = false;
        this.didRespond =false;
    }

    public Friendship() {
    }    

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getSendingFriend() {
        return sendingFriend;
    }

    public void setSendingFriend(User sendingFriend) {
        this.sendingFriend = sendingFriend;
    }

    public User getReceivingFriend() {
        return receivingFriend;
    }

    public void setReceivingFriend(User receivingFriend) {
        this.receivingFriend = receivingFriend;
    }

    public boolean isDidAccept() {
        return didAccept;
    }

    public void setDidAccept(boolean didAccept) {
        this.didAccept = didAccept;
    }

    public boolean isDidRespond() {
        return didRespond;
    }

    public void setDidRespond(boolean didRespond) {
        this.didRespond = didRespond;
    }
    
    
    
    
    
}
