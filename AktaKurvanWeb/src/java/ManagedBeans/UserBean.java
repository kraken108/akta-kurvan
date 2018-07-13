/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBeans;

import Rest.UserRestClient;
import ViewModel.*;
import java.io.IOException;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author Jakob
 */
@ManagedBean
@SessionScoped
public class UserBean {

    private boolean isLoggedIn;
    private String email;
    private String password;
    private String statusMessage;

    public UserBean() {
        isLoggedIn = false;
        email = "";
        password = "";
        statusMessage = "";
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public void logMeIn(String email) {
        this.email = email;
        isLoggedIn = true;
    }

    public String logout() {
        email = "";
        password = "";
        isLoggedIn = false;
        return "logout.xhtml";
    }

    public List<VUser> getFriends() {

        UserRestClient client = new UserRestClient();
        
        return (List<VUser>) client.sendGetFriends(new VUser(email,"","","",true));

    }

    public String doLogin() {
        //// TODOOO
        isLoggedIn = true;
        return "index.xhtml";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String redirectLoginPage() {
        return "login.xhtml";
    }

    public boolean getIsLoggedIn() {
        return isLoggedIn;
    }
}
