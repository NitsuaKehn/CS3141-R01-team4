package com.team_software.cs3141_project;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class ClientConnection implements Runnable {

    private Server parent;

    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;

    protected ClientConnection(Server parent, Socket socket) throws IOException {


        this.parent = parent;
        this.socket = socket;

        this.out    = new DataOutputStream(socket.getOutputStream());;
        this.in     = new DataInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        while(!this.socket.isClosed()) {
            try {
                String UserID = this.in.readUTF();

                String recipiantIP = parent.getUserIP(UserID);

                out.writeBytes(recipiantIP);

                out.flush();


            } catch (IOException e) {}
        }
    }
}