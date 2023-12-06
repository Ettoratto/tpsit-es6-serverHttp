package itismeucci.tpsit;

import java.io.*;
import java.net.*;

public class ServerClass {

    int port;
    ServerSocket server;
    Socket client;
    BufferedReader in;
    PrintWriter out;

    public ServerClass(int port) {

        this.port = port;
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void connect() {

        try {
            for (;;) {
                System.out.println("Waiting for connection...");
                client = server.accept();new PrintWriter(client.getOutputStream(), true);

                System.out.println("Client connesso: " + client.getInetAddress() + ":" + port);
                ServerThread serverThread = new ServerThread(client);
                serverThread.start();
            }
        } catch (IOException e) {

            System.out.println("Something went wrong!");
        }
    }
    

       
}