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

    private static File contacts = new File("ContactsServer");

    private Executor executor = Executors.newCachedThreadPool();

    public String getUserIP(String UserID)
    {
        try(Scanner in = new Scanner(contacts)){


            in.useDelimiter(",|\\n");

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

    public void updateContact(String UserID, Socket client)
    {
        String newContacts = "";

        try(Scanner in = new Scanner(contacts)){


            in.useDelimiter(",|\n");

            while(in.hasNext())
            {
                String userName = in.next();
                if(userName.equals(UserID))
                {
                    in.next();
                }
                else
                {
                    newContacts += userName + "," + in.next() + "\n";
                }
            }

        }catch(FileNotFoundException e){
            e.printStackTrace();
        }

        try (PrintWriter out = new PrintWriter(contacts)){


            out.write(newContacts);
            out.write(UserID + "," + client.getInetAddress() + "\n");


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    public void handleConnection(Socket client) throws IOException {
        ClientConnection newClient = new ClientConnection(this, client);
        this.executor.execute(newClient);
    }

    public static void main(String[] args) throws IOException {

        Server server = new Server();//insatiate the server



        ServerSocket serverSocket = new ServerSocket(portNumber);//makes the server socket

        while(true)
        {
            //listens until it gets new client socket
            Socket clientSocket = serverSocket.accept();
            System.out.println("Server connected to: " + clientSocket.getInetAddress());

            BufferedReader input = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));

            String clientID = input.readLine();

            server.updateContact(clientID, clientSocket);
            server.handleConnection(clientSocket);




        }



    }

}