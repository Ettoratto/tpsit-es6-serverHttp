package itismeucci.tpsit;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class ServerThread extends Thread {
    
    ServerSocket server;
    Socket client;
    BufferedReader in;
    PrintWriter out;
    DataOutputStream dataout;

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
            dataout = new DataOutputStream(client.getOutputStream());
        } catch (IOException e) {
            System.out.println("Something went wrong!");
        }
    
    
        String uri = "", request;
        try {
            if(!(request = in.readLine()).isEmpty()){
                String fileName = request.split(" ")[1];
                if (fileName.endsWith("/")) {
                    fileName += "index.html";
                }
               send(fileName.substring(1)); // rimuove la / iniziale
                System.out.println("Request: " + request);
                System.out.println("URI: " + uri);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }

    public String getBody(String uri){

        File content = new File(uri);
        try {
            String body = Files.readString(content.toPath());
            return body;
        } catch (IOException e) {
            return null;
        }

    }
    
    public void send(String fileName) {
        try{
            // recupera un file dalle risorse (dal classpath) compresa evemtuale sottocartella
            URL resource =  getClass().getClassLoader().getResource(fileName);

            File file = new File(resource.toURI());
            //legge tutti i byte del file
            byte[] buffer = Files.readAllBytes(file.toPath());

            out.println("HTTP /1.1 200 OK");
            out.println("Content-Type: " + contentType(fileName));
            out.println("Content-Length: " + buffer.length);
            out.println();

            dataout.write(buffer);
            dataout.flush();
            out.flush();
        } catch (Exception e) {
            sendError();
        } finally {
            closeConnection();
        }//profGPT
    }

    private String contentType(String fileName) {
        if (fileName.endsWith("html")) {
            return "text/html";
        } else if (fileName.endsWith("jpeg") || fileName.endsWith("jpg")) {
            return "image/jpeg";
        } else if (fileName.endsWith("css")) {
            return "text/css";
        } else if (fileName.endsWith("js")) {
            return "application/javascript";
        }
        throw new RuntimeException();
    }

    public void sendError(){
        try{
            URL resource =  getClass().getClassLoader().getResource("error404.html");
            File page = new File(resource.toURI());
            String body = Files.readString(page.toPath());
            out.println("HTTP /1.1 404 Page not found");
            out.println("Content-Type: text/html");
            out.println("Content-Length: " + body.getBytes().length );
            out.println(""); 
            out.println(body);
            out.flush();
            closeConnection();
        }catch(Exception e){
            
        }
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
