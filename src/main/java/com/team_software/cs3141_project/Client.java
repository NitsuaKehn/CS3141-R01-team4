package com.team_software.cs3141_project;

import java.io.*;

import java.util.Scanner;
import java.net.Socket;


public class Client {


    private static String serverIp = "141.219.196.118";
    private static int port = 6066;


    public static void main(String[] args)throws IOException {

        Scanner myObj = new Scanner(System.in);
        String input;
        Socket server = new Socket(serverIp, port);
        PrintWriter out = new PrintWriter(server.getOutputStream(), true);
        DataInputStream in = new DataInputStream(server.getInputStream());
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