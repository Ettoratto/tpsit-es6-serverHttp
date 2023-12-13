package itismeucci.tpsit;

import java.io.*;
import java.net.*;
import java.nio.file.Files;

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
    
    
        String uri = "", request;
        try {
            if(!(request = in.readLine()).isEmpty()){
                uri = "src/main/java/itismeucci/tpsit/resources" + (uri = request.split(" ")[1]) + "/index.html";
                if(getBody(uri) == null)
                    sendError();
                else
                    sendPage(uri);
                System.out.println("Request: " + request);
                System.out.println("URI: " + uri);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }

    public String getBody(String uri){

        File page = new File(uri);
        String body = "";
        try {
            body = Files.readString(page.toPath());//fare lollo.svg !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            return body;
        } catch (IOException e) {
            return null;
        }

    }

    public void sendPage(String uri){

        String body = getBody(uri);
        out.println("HTTP /1.1 200 OK");
        out.println("Content-Type: text/html");
        out.println("Content-Length: " + body.getBytes().length );
        out.println("");
        out.println(body);
        closeConnection();
    }

    public void sendError(){

        File page = new File("src/main/java/itismeucci/tpsit/resources/error404.html");
        String body = "";
        try {
            body = Files.readString(page.toPath());
        } catch (IOException e) {
        }
        out.println("HTTP /1.1 404 Page not found");
        out.println("Content-Type: text/html");
        out.println("Content-Length: " + body.getBytes().length );
        out.println(""); 
        out.println(body);
        closeConnection();
    }
    
    public void closeConnection() {

        System.out.println("Closing connection...");
        try {
            in.close();
            out.close();
            //client.close();
        } catch (Exception e) {
            System.out.println("Something went wrong!");
        }
    }

}
