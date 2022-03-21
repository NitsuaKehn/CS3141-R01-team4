package com.team_software.cs3141_project;

import java.io.*;

import java.util.Scanner;
import java.net.Socket;
import javax.swing.JOptionPane;

public class Client {


    private static String serverIp = "141.219.196.118";
    private static int port = 6066;


    public static void main(String[] args)throws IOException {

        Scanner myObj = new Scanner(System.in);
        String input;
        Socket server = new Socket(serverIp, port);
        PrintWriter out = new PrintWriter(server.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
        //out.println("Austin has connected to the server");
        System.out.println("Input:  ");

        while(true)
        {
            //System.out.println(in.readLine());
            input = myObj.nextLine();
            if(input.equals("exit") || input.equals("Exit"))
            {
                out.println("Closing closing due to exit call");

                server.close();
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