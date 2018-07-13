/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BO;

import Model.Friendship;
import Model.User;
import ViewModel.VUser;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 *
 * @author Jakob
 */
public class UserHandler {

    private final static String PERSISTENCE_NAME = "com.mycompany_AktakurvanMavenBackend_war_1.0-SNAPSHOTPU";

    public boolean login(VUser u) {

        EntityManager em;
        EntityManagerFactory emf;
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_NAME);
        em = emf.createEntityManager();

        try {

            Query q = em.createQuery(
                    "SELECT u FROM User u WHERE u.email LIKE :email");
            q.setParameter("email", u.getEmail());
            q.setMaxResults(1);
            q.getSingleResult();
            return true;
        } catch (NoResultException e) {
            em.getTransaction().begin();
            User userToInsert = new User(u.getEmail());
            em.persist(userToInsert);
            em.flush();
            em.getTransaction().commit();
            return true;
        } finally {
            if (em != null) {
                em.close();
            }
            emf.close();
        }
    }

    public boolean updateDeviceToken(VUser u) {
        EntityManager em;
        EntityManagerFactory emf;
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_NAME);
        em = emf.createEntityManager();
        System.out.println("Updating token: " + u.getDeviceToken());
        try {
            em.getTransaction().begin();
            User user;
            user = (User) em.createQuery(
                    "SELECT u FROM User u WHERE u.email LIKE :email")
                    .setParameter("email", u.getEmail())
                    .setMaxResults(1)
                    .getSingleResult();
            user.setDeviceToken(u.getDeviceToken());
            em.persist(user);
            em.flush();
            em.getTransaction().commit();

            return true;
        } catch (NoResultException e) {
            return false;
        } finally {
            if (em != null) {
                em.close();
            }
            emf.close();
        }

    }

    public List<VUser> getAllUsers(VUser u) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean addFriend(VUser u) {
        EntityManager em;
        EntityManagerFactory emf;
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_NAME);
        em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            System.out.println(u.getEmail());
            System.out.println(u.getFriendToAdd());

            try {
                List<Friendship> f = (List<Friendship>) em.createQuery("SELECT f from Friendship f WHERE f.receivingFriend.email LIKE :email1 "
                        + "AND f.sendingFriend.email LIKE :email2")
                        .setParameter("email1", u.getEmail())
                        .setParameter("email2", u.getFriendToAdd())
                        .getResultList();
                if (!f.isEmpty()) {
                    return false;
                }
            } catch (Exception e) {

            }

            try {

                List<Friendship> f = (List<Friendship>) em.createQuery("SELECT f from Friendship f WHERE f.sendingFriend.email LIKE :email1 "
                        + "AND f.receivingFriend.email LIKE :email2")
                        .setParameter("email1", u.getEmail())
                        .setParameter("email2", u.getFriendToAdd())
                        .getResultList();
                if (!f.isEmpty()) {
                    return false;
                }
            } catch (Exception e) {

            }

            User sender = getUserByEmail(u.getEmail());
            User receiver = getUserByEmail(u.getFriendToAdd());

            Friendship friendship = new Friendship(sender, receiver);
            em.persist(friendship);
            em.flush();
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (em != null) {
                em.close();
            }
            emf.close();
        }
    }

    public List<VUser> getFriends(VUser user) {
        EntityManager em;

        EntityManagerFactory emf;
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_NAME);
        em = emf.createEntityManager();

        try {
            List<Friendship> list = (List<Friendship>) em.createQuery(
                    "SELECT f FROM Friendship f WHERE f.receivingFriend.email LIKE :email "
                    + "OR f.sendingFriend.email LIKE :email2")
                    .setParameter("email", user.getEmail())
                    .setParameter("email2", user.getEmail())
                    .getResultList();

            List<VUser> friends = new ArrayList<>();

            for (Friendship f : list) {
                if (f.isDidAccept()) {
                    if (f.getSendingFriend().getEmail().equals(user.getEmail())) {
                        friends.add(new VUser(f.getReceivingFriend().getEmail(), "", "", "", false));
                    } else {
                        friends.add(new VUser(f.getSendingFriend().getEmail(), "", "", "", false));
                    }
                }
            }

            return friends;
        } catch (Exception e) {
            return null;
        } finally {
            if (em != null) {
                em.close();
            }
            emf.close();
        }
    }

    public List<VUser> getPendingFriends(VUser user) {

        EntityManager em;

        EntityManagerFactory emf;
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_NAME);
        em = emf.createEntityManager();

        try {
            List<Friendship> list = (List<Friendship>) em.createQuery(
                    "SELECT f FROM Friendship f WHERE f.receivingFriend.email LIKE :email "
                    + "AND f.didRespond LIKE :didrespond")
                    .setParameter("email", user.getEmail())
                    .setParameter("didrespond", false)
                    .getResultList();

            List<VUser> senders = new ArrayList<>();

            for (Friendship f : list) {
                senders.add(new VUser(f.getSendingFriend().getEmail(), "", "", "", false));
            }
            return senders;
        } catch (Exception e) {
            return null;
        } finally {
            if (em != null) {
                em.close();
            }
            emf.close();
        }
    }

    private User getUserByEmail(String email) {
        User tempUser;

        EntityManager em;
        EntityManagerFactory emf;

        emf = Persistence.createEntityManagerFactory(PERSISTENCE_NAME);
        em = emf.createEntityManager();

        try {
            tempUser = (User) em.createQuery(
                    "SELECT u FROM User u WHERE u.email LIKE :email")
                    .setParameter("email", email)
                    .getSingleResult();
            return tempUser;
        } catch (NoResultException e) {
            throw (e);
        } catch (Exception e) {
            throw (e);
        } finally {
            if (em != null) {
                em.close();
            }
            if (emf != null) {
                emf.close();
            }
        }
    }

    public boolean respondFriendRequest(VUser user) {
        EntityManager em;

        EntityManagerFactory emf;
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_NAME);
        em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            System.out.println("Receiving friend: " + user.getEmail());
            System.out.println("Sending friend: " + user.getFriendToAdd());

            Friendship f = (Friendship) em.createQuery("SELECT f from Friendship f WHERE f.receivingFriend.email LIKE :email1 "
                    + "AND f.sendingFriend.email LIKE :email2")
                    .setParameter("email1", user.getEmail())
                    .setParameter("email2", user.getFriendToAdd())
                    .getSingleResult();
            f.setDidRespond(true);
            f.setDidAccept(user.isDidAccept());
            em.persist(f);
            em.flush();
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        } finally {
            if (em != null) {
                em.close();
            }
            emf.close();
        }
    }

    private static String getAccessToken() throws IOException {
        FileInputStream serviceAccount = new FileInputStream("D:\\Firebase\\mobilapp-67b55-firebase-adminsdk-ijhds-43eb2c5271.json");
        String[] scope = {"https://www.googleapis.com/auth/firebase.messaging"};
        GoogleCredential googleCredential = GoogleCredential
                .fromStream(serviceAccount)
                .createScoped(Arrays.asList("https://www.googleapis.com/auth/firebase.messaging"));
        googleCredential.refreshToken();
        return googleCredential.getAccessToken();
    }

    private HttpPost getHttpURL() throws IOException {
        HttpPost post = new HttpPost("https://fcm.googleapis.com/v1/projects/mobilapp-67b55/messages:send");
        post.setHeader("Authorization", "Bearer " + getAccessToken());
        post.setHeader("Content-Type", "application/json; UTF-8");
        return post;
    }

    public int sendChallengeRequest(VUser user) {
        EntityManager em;

        EntityManagerFactory emf;
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_NAME);
        em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            System.out.println("Challenged by: " + user.getEmail());
            System.out.println("Challenged friend: " + user.getFriendToAdd());

            User friend = getUserByEmail(user.getFriendToAdd());

            GameServerCommunicator gameComm = new GameServerCommunicator();
            ArrayList<String> playerNames = new ArrayList<>();
            playerNames.add(user.getEmail());
            playerNames.add(user.getFriendToAdd());
            int gameId = gameComm.requestNewGame(playerNames);
            if(gameId == -1){
                //if something went wrong
                return -1;
            }
            
            //Send cloud message to friend
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("body", "CHALLENGE " + user.getEmail() + " " 
            + GameInfo.getInstance().getIp() + " " + GameInfo.getInstance().getClientPort()
            + " " + gameId);
            notificationObj.put("title", "A Challenge");

            JSONObject messageObj = new JSONObject();
            messageObj.put("token", friend.getDeviceToken());
            messageObj.put("notification", notificationObj);

            JSONObject mainObject = new JSONObject();
            mainObject.put("message", messageObj);

            HttpPost url = getHttpURL();
            url.setEntity(new StringEntity(mainObject.toString()));

            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(url);
            System.out.println("Response:");
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
            System.out.println(responseString);
            em.flush();
            em.getTransaction().commit();
            return gameId;
        } catch (Exception e) {
            System.out.println(e);
            return -1;
        } finally {
            if (em != null) {
                em.close();
            }
            emf.close();
        }
    }

}
