package com.team_software.cs3141_project;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.io.IOException;
import java.net.Socket;
import javax.swing.JOptionPane;

public class Client {

    public static void main(String[] args)throws IOException {
        int Port =Integer.parseInt(JOptionPane.showInputDialog("Input Your Port : "));
        String IP = JOptionPane.showInputDialog("Input Your IP Server : ");
        Socket sock=new Socket("localhost", 6066);
        DataInputStream in= new DataInputStream(sock.getInputStream());
        System.out.println(in.readUTF());
        DataOutputStream out =new DataOutputStream(sock.getOutputStream());
        out.writeUTF("waiting for connection");
        sock.close();
    }
}