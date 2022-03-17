package com.team_software.cs3141_project;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

public class Networking {

    public static void main(String[] args) throws IOException {
        String IP = JOptionPane.showInputDialog("Input Your IP Server: ");
        ServerSocket serverSock = new ServerSocket(6066);
        Socket Sock = serverSock.accept();
        DataOutputStream out = new DataOutputStream(Sock.getOutputStream());
        out.writeUTF("i am fine, thank you");
        DataInputStream in = new DataInputStream(Sock.getInputStream());
        System.out.println(in.readUTF());
        Sock.close();
    }


}
