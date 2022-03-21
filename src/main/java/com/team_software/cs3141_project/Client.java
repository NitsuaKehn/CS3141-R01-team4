package com.team_software.cs3141_project;

import java.io.*;

import java.util.Scanner;
import java.net.Socket;
import java.nio.charset.StandardCharsets;



public class Client {


    private static String serverIp = "141.219.196.118";
    private static int port = 6066;


    public static void main(String[] args)throws IOException {

        Scanner myObj = new Scanner(System.in);
        String input;
        Socket server = new Socket(serverIp, port);
        PrintWriter out = new PrintWriter(server.getOutputStream(), true);
        DataInputStream in = new DataInputStream(server.getInputStream());
        //out.println("Austin has connected to the server");
        System.out.println("Input:  ");

        while(true)
        {
//            String inStr = null;
//            inStr = in.readLine();
//            if(inStr != null)
//            {
//                System.out.println(inStr);
//                break;
//            }

            input = myObj.nextLine();
            if(input.equals("exit") || input.equals("Exit"))
            {
                //out.println("Closing closing due to exit call");

                //server.close();
                break;
            }
            // else if (input.equals("get"))
            // {
            //     if(in.)
            //     System.out.println(in.readLine());
            // }
            else
            {
                out.println(input);
                out.flush();

            }
        }
        System.out.println("sent messsage:");
        //System.out.println(in.readUTF());
        int length = in.read();
        length -= 48;
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