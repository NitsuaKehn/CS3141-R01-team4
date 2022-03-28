package com.team_software.cs3141_project;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class ClientConnection implements Runnable {

    private Server parent;

    private Socket socket;
    private PrintWriter out;
    private DataInputStream in;

    protected ClientConnection(Server parent, Socket socket) throws IOException {


        this.parent = parent;
        this.socket = socket;

        this.out    = new PrintWriter(socket.getOutputStream());;
        this.in     = new DataInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        while(!this.socket.isClosed()) {
            try {
                String UserID = this.in.readUTF();

                String recipiantIP = parent.getUserIP(UserID);

                out.println(recipiantIP);

                out.flush();


            } catch (IOException e) {}
        }
    }
}