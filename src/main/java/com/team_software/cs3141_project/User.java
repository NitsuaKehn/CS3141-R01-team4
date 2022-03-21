package com.team_software.cs3141_project;


import java.net.Socket;

public class User {

    private String userName;
    private Boolean online;
    private Socket clientSocket;

    public User(String userName, Boolean online, Socket clientSocket)
    {
        this.userName = userName;
        this.online = online;
        this.clientSocket = clientSocket;
    }

    public String getUserName() {
        return userName;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
}
