package com.team_software.cs3141_project;

import java.io.*;

import java.util.ArrayList;
import java.util.Scanner;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

//client app
public class Client {

    private static String serverIp = "141.219.231.100";
    private static int port = 6066;


    static String input;
    static Socket server;
    static PrintWriter out;
    static Scanner in;
    static String userName;
    private ArrayList<String> peerIPs = new ArrayList<>();

    private Executor executor = Executors.newCachedThreadPool();

    //method to get the IP address of a User from the server
    public static String getIP(String PeerID)
    {
        out.println(PeerID);
        out.flush();
        return in.next();
    }


    //startup method
    public void startUp(String UserID) throws IOException {
        //this clients username
        userName = UserID;
        //socket of the server
        server = new Socket(serverIp, port);
        //inits server
        in = new Scanner(server.getInputStream());
        //inits output
        out = new PrintWriter(server.getOutputStream());

        //sends our username so server can update our IP
        out.println(UserID);
        out.flush();

        //gets the IP address of the all our know clients
        File directoryPath = new File("conversations");
        String contents[] = directoryPath.list();

        for(int i = 0; i < contents.length; i++)
        {
            String peerID = contents[i].replace(".txt", "");

            String peerIP = getIP(peerID);

            if(!peerIP.equals("null"))
            {
                this.updateIP(peerID, peerIP);
            }


            System.out.println("client updated ip of: " + peerID + " to: " + peerIP);

        }
    }


    //updates the IP address up a contact
    public void updateIP(String userID, String IP)
    {
        String buffer = "";
        try{
            //opens the contacts folder
            File contactFile = new File("conversations\\" + userID + ".txt");
            Scanner fileIn = new Scanner(contactFile);

            //eats the old IP address
            fileIn.next();

            //reads in the rest of the file
            while(fileIn.hasNextLine())
            {
                buffer += fileIn.nextLine() + "\n";
            }


            FileWriter fileOut = new FileWriter(contactFile);

            //writes new IP address
            fileOut.append(IP);
            //adds the rest back in
            fileOut.append(buffer);
            fileOut.close();
            fileIn.close();

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    //creates new thread to always listen for new Peer connections
    public void startListener(){
        ClientListener newListener = new ClientListener(this);
        this.executor.execute(newListener);
    }

    //handle message from peer
    public void getMessage(String peerID, String message)
    {
        //open the file for given contact
        File file = new File("Conversations/" + peerID + ".txt");

        //add the message with the recived tag
        try (PrintWriter out = new PrintWriter(file)){
            out.append("R " + message);
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Message from PeerID: " + peerID + ": " + message);
    }

    //method to send Message to given Peer
    public void sendMessage(String peerID, String message) throws IOException {

        //opens contact file name
        File file = new File("conversations\\" + peerID + ".txt");
        Scanner fileIn = new Scanner(file);

        //gets the IP address of the peer
        String peerIP = fileIn.next();

        //opens the socket
        Socket peerSocket = new Socket(peerIP.substring(1), port);

        //intis output
        PrintWriter peerOut = new PrintWriter(peerSocket.getOutputStream());

        //sends the message to the Peer
        peerOut.println(userName + " " + message);
        peerOut.flush();


    }

    //main method
    public static void main(String[] args) throws IOException {

        Client client = new Client();

        Scanner systemIn = new Scanner(System.in);

        System.out.println("what is your username?");

        client.startListener();

        client.startUp(systemIn.nextLine());

        System.out.println("who would you like to talk to");

        String peer = systemIn.nextLine();

        System.out.println("what is your message");

        String message = systemIn.nextLine();

        client.sendMessage(peer, message);


    }
}
