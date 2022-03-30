package com.team_software.cs3141_project;

import java.io.*;
import java.net.Socket;

public class ServerConnection implements Runnable {

    private Server parent;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    protected ServerConnection(Server parent, Socket socket) throws IOException {


        this.parent = parent;
        this.socket = socket;

        this.out    = new PrintWriter(socket.getOutputStream());;
        this.in     = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        while(!this.socket.isClosed()) {
            try {
                String UserID = this.in.readLine();

                String recipiantIP = parent.getUserIP(UserID);

                out.println(recipiantIP);

                System.out.println("recpiant ip is " + recipiantIP);

                out.flush();

                if(UserID == null)
                {
                    socket.close();
                }




            } catch (IOException e) {}
        }
    }
}