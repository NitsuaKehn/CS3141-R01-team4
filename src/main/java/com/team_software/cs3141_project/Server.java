package com.team_software.cs3141_project;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Server {

    private static int portNumber = 6066;//default port number

    private static File contacts = new File("ContactsServer");

    private Executor executor = Executors.newCachedThreadPool();


    //gets the IP address of the provided UserID from contactsServer file
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

        return "null";
    }

    //updates the clients IP address in the ContactsServer file
    public void updateContact(String UserID, Socket client)
    {
        String otherContacts = "";

        //opens scanner to read the contacts file
        try(Scanner in = new Scanner(contacts)){

            //sets the delimiter for the scanner
            in.useDelimiter(",|\n");

            //loops through the Contacts until it finds the correct one then removes it
            while(in.hasNext())
            {
                String userName = in.next();
                if(userName.equals(UserID))
                {
                    in.next();
                }
                else
                {
                    otherContacts += userName + "," + in.next() + "\n";
                }
            }


        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        //adds the current user with updated IP at the end
        try (PrintWriter out = new PrintWriter(contacts)){


            out.println(otherContacts + UserID + "," + client.getInetAddress());
            out.flush();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    //creates new thread to handle a client socket
    public void handleConnection(Socket client) throws IOException {
        ServerConnection newClient = new ServerConnection(this, client);
        this.executor.execute(newClient);
    }

    //
    public static void main(String[] args) throws IOException {

        Server server = new Server();//insatiate the server

        ServerSocket serverSocket = new ServerSocket(portNumber);//makes the server socket

        //loops forever
        while(true)
        {
            //listens until it gets new client socket
            Socket clientSocket = serverSocket.accept();
            System.out.println("Server connected to: " + clientSocket.getInetAddress());

            //reads in the user's username
            BufferedReader input = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));

            String clientID = input.readLine();

            if(clientID.equals("DefaultUser"))
            {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
                File contacts = new File("ContactsServer");
                Scanner in = new Scanner(contacts);

                while(in.hasNextLine())
                {
                    out.print(in.nextLine());
                }

                in.close();
                out.close();
                clientSocket.close();

            }
            else
            {
                //updates contacts file
                server.updateContact(clientID, clientSocket);
                //handles connection
                server.handleConnection(clientSocket);
            }





        }



    }

}