package com.team_software.cs3141_project;

import java.io.*;

import java.util.ArrayList;
import java.util.Scanner;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


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

    public static String getIP(String PeerID)
    {
        out.println(PeerID);
        out.flush();
        return in.next();
    }


    public void startUp(String UserID) throws IOException {

        userName = UserID;

        server = new Socket(serverIp, port);

        in = new Scanner(server.getInputStream());

        out = new PrintWriter(server.getOutputStream());


        out.println(UserID);
        out.flush();

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

    public void updateIP(String userID, String IP)
    {
        String buffer = "";
        try{
            File contactFile = new File("conversations\\" + userID + ".txt");
            Scanner fileIn = new Scanner(contactFile);


            fileIn.next();

            while(fileIn.hasNextLine())
            {
                buffer += fileIn.nextLine() + "\n";
            }

            FileWriter fileOut = new FileWriter(contactFile);

            fileOut.append(IP);
            fileOut.append(buffer);
            fileOut.close();
            fileIn.close();

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void startListener(){
        ClientListener newListener = new ClientListener(this);
        this.executor.execute(newListener);
    }

    public void getMessage(String peerID, String message)
    {
        File file = new File("Conversations/" + peerID + ".txt");
        try (PrintWriter out = new PrintWriter(file)){
            out.append("R " + message);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Message from PeerID: " + peerID + ": " + message);
    }

    public void sendMessage(String peerID, String message) throws IOException {


        File file = new File("conversations\\" + peerID + ".txt");
        Scanner fileIn = new Scanner(file);

        String peerIP = fileIn.next();

        Socket peerSocket = new Socket(peerIP.substring(1), port);

        PrintWriter peerOut = new PrintWriter(peerSocket.getOutputStream());

        peerOut.println(userName + " " + message);
        peerOut.flush();


    }

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
