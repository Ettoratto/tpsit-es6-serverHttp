package itismeucci.tpsit;

import java.io.*;
import java.net.*;

public class ClientClass {

    int serverPort;
    String serverAddress;

    Socket client;
    BufferedReader stdIn, in;
    PrintWriter out;

    public ClientClass(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public Socket connect(){

        try {
            client = new Socket(serverAddress, serverPort);
            
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
            stdIn = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Connesso all'host: " + serverAddress + ":" + serverPort);
        } catch (UnknownHostException e) {
            System.err.println("Host is unknown");

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Something went wrong, closing client...");
            System.exit(1);
        }

        return client;
    }

    public void communicate(){

        String userInput;
            
        try {
            while (true) {
                userInput = stdIn.readLine();
                out.println(userInput);
                String str = in.readLine();
                System.out.println("Server: " + str);
                if (str.equals("Hai indovinato!"))
                    break;
            }
            
            closeConnection();
        
        } catch (IOException e) {
            System.out.println("Something went wrong...");
        }
    }

    public void closeConnection(){

        try {
            out.close();
            in.close();
            client.close();
        } catch (IOException e) {
            System.out.println("Something went wrong...");
        }
    }

}
