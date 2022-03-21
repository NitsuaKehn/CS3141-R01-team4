package com.team_software.cs3141_project;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Server {

    private static int portNumber = 6066;//default port number

    private ArrayList<User> usersList = new ArrayList<>();//stores the users

    private Executor executor = Executors.newCachedThreadPool();//used to run the different connections

    public void handleConnection(Socket client) throws IOException {
        CientConnection newClient = new CientConnection(this, client);//creates a new client connection
        this.executor.execute(newClient);//runs that connection in a new thread
    }
    //gets the user from the userList returns null if uer doesn't exist
    public User getUserFromList(String userName)
    {
        for(User u: usersList)
        {
            if(u.getUserName().equals(userName))
            {
                return u;

            }
        }
        return null;
    }

    //sets users status to online if the user doesn't exist it creates one
    public void setUserOnline(String userName, Socket socket)
    {
        User temp = getUserFromList(userName);
        if(temp != null)
        {
            temp.setOnline(true);
            temp.setClientSocket(socket);
        }
        else
        {
            temp = new User(userName, true, socket);
            usersList.add(temp);
        }

        for( User u: usersList)
        {
            System.out.println(u.getUserName());
        }
    }

    //sets a users status to offline
    public void setUserOffline(String userName)
    {
        getUserFromList(userName).setOnline(false);
    }

    //sends a mesage to a the provided user
    public void sendMessage(String userName, String message) throws IOException
    {
        User messageRecpiant = getUserFromList(userName);
        if(messageRecpiant.getOnline())
        {
            System.out.println("inside if online");
            PrintWriter out = new PrintWriter(messageRecpiant.getClientSocket().getOutputStream());
            int l = message.length();
            out.write(l + message);
            out.flush();


        }
        else
        {
            //right now idk what to do
        }

    }

    public static void main(String[] args) throws IOException {

        Server server = new Server();//insatiate the server

        ServerSocket serverSocket = new ServerSocket(portNumber);//makes the server socket

        while(true)
        {
            //listens until it gets new client socket
            Socket clientSocket = serverSocket.accept();

            //prints out the ip of the client
            System.out.println("server connected to client ip:" + clientSocket.getInetAddress());

            //calls teh handler function
            server.handleConnection(clientSocket);

        }

    }


}