package com.team_software.cs3141_project;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ServerConnection implements Runnable {

    private Server parent;

    private Socket socket;
    private PrintWriter out;
    private Scanner in;

    protected ServerConnection(Server parent, Socket socket) throws IOException {


        this.parent = parent;
        this.socket = socket;

        this.out    = new PrintWriter(socket.getOutputStream());;
        this.in     = new Scanner(socket.getInputStream());
    }

    @Override
    public void run() {
        while(!this.socket.isClosed()) {
            try {
                String UserID = this.in.nextLine();

                String recipientIP = parent.getUserIP(UserID);

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