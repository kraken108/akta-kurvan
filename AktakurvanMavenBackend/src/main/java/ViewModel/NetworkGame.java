package ViewModel;

/**
 * Created by Jakob on 2018-01-05.
 */

public class NetworkGame {
    private String ip;
    private int port;
    private int id;

    public NetworkGame(){
        this.ip = "";
        this.port = 0;
        this.id = 0;
    }

    public NetworkGame(String ip, int port, int id){
        this.ip = ip;
        this.port = port;
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
