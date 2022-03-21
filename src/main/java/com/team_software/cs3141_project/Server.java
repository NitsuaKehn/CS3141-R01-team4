package com.team_software.cs3141_project;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.swing.JOptionPane;

public class Server {

    private static int portNumber = 6066;

    private static ArrayList<Socket>  users = new ArrayList<Socket>();

    private Executor executor = Executors.newCachedThreadPool();;

    public void handleConnection(Socket client) throws IOException {
        clientConnection newClient = new clientConnection(this, client);
        this.executor.execute(newClient);
    }

    public static void main(String[] args) throws IOException {

        Server server = new Server();

        ServerSocket serverSocket = new ServerSocket(portNumber);

        while(true)
        {

            Socket clientSocket = serverSocket.accept();

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            //System.out.println(in.readLine());
            System.out.println("server connected to " + clientSocket.getInetAddress());

            server.handleConnection(clientSocket);

            users.add(clientSocket);
        }








    }


}