package com.team_software.cs3141_project;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.PrimitiveIterator;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Server {

    private static int portNumber = 6066;//default port number

    private static File contacts = new File("ContacrtsServer");

    public String findContact(String UserID)
    {
        try(Scanner in = new Scanner(contacts)){

            while(in.hasNext())
            {
                if(in.next().equals(UserID))
                {
                    return in.next();
                }
                else
                {
                    in.next();
                }
            }

        }catch(FileNotFoundException e){
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) throws IOException {

        Server server = new Server();//insatiate the server



        ServerSocket serverSocket = new ServerSocket(portNumber);//makes the server socket

        while(true)
        {
            //listens until it gets new client socket
            Socket clientSocket = serverSocket.accept();
            BufferedReader input = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));

            String clientID = input.readLine();

            if(server.findContact(clientID) == null)
            {
                PrintWriter out = new PrintWriter(contacts);

                out.println(clientID + clientSocket.getInetAddress());
            }

            String recipient = input.readLine();

            if(server.findContact(clientID) == null)
            {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream());

                out.write(server.findContact(recipient));
            }
            else
            {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
                out.write("That user doesn't exist");
            }


        }

    }

}