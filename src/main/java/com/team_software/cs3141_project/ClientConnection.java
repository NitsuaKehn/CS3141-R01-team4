package com.team_software.cs3141_project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientConnection implements  Runnable{
    private Client parent;

    private Socket socket;
    private PrintWriter out;
    private Scanner in;

    protected ClientConnection(Client parent, Socket socket) throws IOException {


        this.parent = parent;
        this.socket = socket;

        this.out = new PrintWriter(socket.getOutputStream());;
        this.in = new Scanner(socket.getInputStream());
    }

    @Override
    public void run() {
        while(!this.socket.isClosed()) {
            try {
                String message = this.in.nextLine();

                String[] splitMessage = message.split(" ", 1);

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
