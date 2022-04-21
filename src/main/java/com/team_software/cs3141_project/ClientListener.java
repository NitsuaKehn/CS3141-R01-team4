package com.team_software.cs3141_project;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

//class to listen for new peer connections
public class ClientListener implements Runnable{

    private static int portNumber = 6066;//default port number

    private Executor executor = Executors.newCachedThreadPool();

    private Client parent;

    //method to create new thread for P2P connection
    public void handleConnection(Socket peer) throws IOException {
        ClientConnection newPeer = new ClientConnection(parent, peer);
        this.executor.execute(newPeer);
    }

    //constructor
    protected ClientListener(Client parent)
    {
        this.parent = parent;
    }


    //main method
    @Override
    public void run(){

        //new server socket
        ServerSocket thisPeer = null;
        try {
            thisPeer = new ServerSocket(portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //loops forever
        while(true)
        {
            //waits for new connection by peer
            try {

                Socket peerSocket = thisPeer.accept();
                System.out.println("this peer connected to: " + peerSocket.getInetAddress());
                //creates new thread
                this.handleConnection(peerSocket);

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }


}
