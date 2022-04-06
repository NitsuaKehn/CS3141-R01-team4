package com.team_software.cs3141_project;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ClientListener implements Runnable{

    private static int portNumber = 6066;//default port number

    private Executor executor = Executors.newCachedThreadPool();

    private Client parent;

    public void handleConnection(Socket peer) throws IOException {
        ClientConnection newPeer = new ClientConnection(parent, peer);
        this.executor.execute(newPeer);
    }

    protected ClientListener(Client parent)
    {
        this.parent = parent;
    }

    @Override
    public void run(){

        ServerSocket thisPeer = null;
        try {
            thisPeer = new ServerSocket(portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true)
        {
            try {

                Socket peerSocket = thisPeer.accept();
                System.out.println("this peer connected to: " + peerSocket.getInetAddress());
                this.handleConnection(peerSocket);

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }


}
