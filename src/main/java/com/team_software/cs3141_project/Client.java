package com.team_software.cs3141_project;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JOptionPane;

public class Client {

    private static String serverIp = "141.219.196.118";
    private static int port = 6066;

    public static void main(String[] args)throws IOException {

        Socket server = new Socket(serverIp, port);
        PrintWriter out = new PrintWriter(server.getOutputStream(), true);
        out.println("this is a test my user name is ______");
        server.close();

    }
}