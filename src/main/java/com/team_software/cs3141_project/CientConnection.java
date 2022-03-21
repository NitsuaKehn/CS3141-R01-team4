package com.team_software.cs3141_project;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;


public class CientConnection implements Runnable {

    private Server parent;
    private Socket socket;
    private DataOutputStream out;
    private BufferedReader in;

    //constructor
    protected CientConnection(Server parent, Socket socket) throws IOException {
        try {
            socket.setSoTimeout(0);
            socket.setKeepAlive(true);
        } catch (SocketException e) {}

        this.parent = parent;
        this.socket = socket;

        this.out = new DataOutputStream(socket.getOutputStream());


        this.in = new BufferedReader( new InputStreamReader(socket.getInputStream()));

    }

    @Override
    public void run() {

        //loops until the socket is closed
        while(!this.socket.isClosed()) {



            //gets the input string
            try {
                //String fullData = this.in.readLine();
                int nextEvent = this.in.read();
                nextEvent -= 48;
                System.out.println(nextEvent);

                String commandData = in.readLine();

                switch (nextEvent)
                {
                    case 1:
                        //this is the startup command format: 1usernameOfClient
                        parent.setUserOnline(commandData, socket);
                        System.out.println("case 1");
                        break;
                    case 2:
                        //this is the command to send a message to another user format: 2usernameOfRecpient message
                        String[] strings = commandData.split(" ",2);
                        parent.sendMessage(strings[0], strings[1]);
                        System.out.println("case 2");
                        break;
                    case 3:
                        //this is the shutDown command format: 3usernameOfCleint
                        parent.setUserOffline(commandData);
                        System.out.println("case 3");
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;

                    default:
                        //means that the socket has been close on client side so close it here too
                        System.out.println("default case");
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}


