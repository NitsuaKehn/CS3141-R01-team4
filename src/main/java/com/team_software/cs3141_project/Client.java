package com.team_software.cs3141_project;

import java.io.*;

import java.util.Scanner;
import java.net.Socket;


public class Client {

    private static String serverIp = "141.219.231.100";
    private String peerIp;
    private static int port = 6066;

    static Scanner myObj = new Scanner(System.in);
    static String input;
    static Socket server;
    static PrintWriter out;
    static DataInputStream in;


    public static void main(String[] args) throws IOException {

        Socket server = new Socket(serverIp, port);

        Scanner in = new Scanner(System.in);

        PrintWriter out = new PrintWriter(server.getOutputStream());

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

    }
}
