package com.team_software.cs3141_project;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

public class Server {

    private static int portNumber = 6066;

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(portNumber);
        Socket clientSocket = serverSocket.accept();

        DataInputStream in = new DataInputStream(clientSocket.getInputStream());

        System.out.println(in.readUTF());

        clientSocket.close();


    }


}