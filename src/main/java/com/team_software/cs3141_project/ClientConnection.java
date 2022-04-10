package com.team_software.cs3141_project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

//handler for P2P connection
public class ClientConnection implements  Runnable{
    private Client parent;

    private Socket socket;
    private PrintWriter out;
    private Scanner in;

    //constructor
    protected ClientConnection(Client parent, Socket socket) throws IOException {


        this.parent = parent;
        this.socket = socket;

        this.out = new PrintWriter(socket.getOutputStream());
        this.in = new Scanner(socket.getInputStream());
    }

    //main method
    @Override
    public void run() {
        //loops until socket is closed
        while(!this.socket.isClosed()) {
            try {
                //gets the whole message
                String message = this.in.nextLine();

                //splits the message into the UserName and the whole message
                String[] splitMessage = message.split(" ", 2);

                //calls get message to update the stuff
                parent.getMessage(splitMessage[0], splitMessage[1]);

                if(message == null)
                {
                    socket.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
