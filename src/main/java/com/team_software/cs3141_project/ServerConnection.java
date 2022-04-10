package com.team_software.cs3141_project;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

//handles the connections to client
public class ServerConnection implements Runnable {

    private Server parent;

    private Socket socket;
    private PrintWriter out;
    private Scanner in;

    //constructor
    protected ServerConnection(Server parent, Socket socket) throws IOException {


        this.parent = parent;
        this.socket = socket;

        this.out    = new PrintWriter(socket.getOutputStream());;
        this.in     = new Scanner(socket.getInputStream());
    }

    //runnable method
    @Override
    public void run() {
        //loops until the socket is closed
        while(!this.socket.isClosed()) {
            try {
                //reads in the userID
                String UserID = this.in.nextLine();
                //gets the IP
                String recipientIP = parent.getUserIP(UserID);
                //sends the IP back to the client
                out.println(recipientIP);

                System.out.println("recipient ip is " + recipientIP);

                out.flush();

                if(UserID == null)
                {
                    socket.close();
                }

            } catch (IOException e) {}
        }
    }
}