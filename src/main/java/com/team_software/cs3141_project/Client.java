package com.team_software.cs3141_project;

import java.io.*;

import java.util.Scanner;
import java.net.Socket;


public class Client {

    private static String serverIp = "141.219.196.118";
    private String peerIp;
    private static int port = 6066;

    static Scanner myObj = new Scanner(System.in);
    static String input;
    static Socket server;
    static PrintWriter out;
    static DataInputStream in;

    static {
        try {
            server = new Socket(serverIp, port);
            in = new DataInputStream(server.getInputStream());
            out = new PrintWriter(server.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructor for client class.
     * Initializes peerIp.
     * @param peerIp
     * @throws IOException
     */
    public Client(String peerIp) throws IOException {
        peerIp = peerIp;
    }

    /**(WIP)
     * Called by UI send button
     * @param msg
     * @return true if msg sent successfully
     * @return false otherwise
     * @throws IOException
     */
    public boolean sendMessage(String msg) throws IOException {
        try {

        } catch (Exception e){
            System.out.println("Message wasn't sent");
            return false;
        }
        return true;
    }

    /**
     * Getter for getting the peer ip.
     * @return String peerIp
     */
    public String getPeerIp() {
        return peerIp;
    }

    /**
     * Getter for getting the server IP.
     * @return String serverIp
     */
    public String getServerIp() {
        return serverIp;
    }

    public static void main(String[] args) throws IOException {

//        Scanner myObj = new Scanner(System.in);
//        String input;
//        Socket server = new Socket(serverIp, port);
//        PrintWriter out = new PrintWriter(server.getOutputStream(), true);
//        DataInputStream in = new DataInputStream(server.getInputStream());
        System.out.println("Input:  ");

        while(true)
        {

            input = myObj.nextLine();
            if(input.equals("exit") || input.equals("Exit"))
            {
                break;
            }

            else
            {
                out.println(input);
                out.flush();
            }
        }

        //have to read in data this way since tcp you never know if the data with all be sent at once
        int length = in.read();//first byte is how long the string is
        length -= 48;

        //loops until the whole string is read in then prints it out
        boolean end = false;
        String dataString = "";
        byte[] messageByte = new byte[1000];

        while(!end)
        {
            int bytesRead = in.read(messageByte);
            dataString += new String(messageByte, 0, bytesRead);
            if (dataString.length() == length)
            {
                end = true;
            }
        }
        System.out.println("MESSAGE: " + dataString);
    }
}
