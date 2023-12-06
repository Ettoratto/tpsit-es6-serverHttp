package itismeucci.tpsit;

import java.io.*;
import java.net.*;

public class ServerThread extends Thread {
    
    ServerSocket server;
    Socket client;
    BufferedReader in;
    PrintWriter out;

    public ServerThread(Socket client) throws IOException {

        this.client = client;
        server = new ServerSocket();
        in = null;
        out = null;
    }

    public void run() {

        try {
            communicate();
        } catch (Exception e) {

        }
    }
    
    
    public void communicate() {

        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Something went wrong!");
        }
        
        for (;;) {

            String str;
            try {
                    
                closeConnection();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void closeConnection() {

        System.out.println("Closing connection...");
        try {
            in.close();
            out.close();
            client.close();
        } catch (Exception e) {
            System.out.println("Something went wrong!");
        }
    }

}
