package itismeucci.tpsit;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

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
                uri = "src/main/java/itismeucci/tpsit/resources" + (uri = request.split(" ")[1]);
                if (request.substring(5, 8).equals("img"))
                    sendImage(uri);
                else if(getBody(uri + "/index.html") != null)
                    sendPage(uri);
                else
                    sendError();
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

    public void sendPage(String uri) {

        String body = getBody(uri + "/index.html");
        out.println("HTTP /1.1 200 OK");
        out.println("Content-Type: text/html");
        out.println("Content-Length: " + body.getBytes().length);
        out.println();
        out.println(body);
        closeConnection();
    }
    
    public void sendImage(String uri) {
        
        File content = new File(uri);
        out.println("HTTP /1.1 200 OK");
        out.println("Content-Type: image/jpeg");
        out.println("Content-Length: " + content.length());
        out.println();

        //chatgpt
        try(FileInputStream fileInputStream = new FileInputStream(content)){
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                String imageData = Base64.getEncoder().encodeToString(buffer); // non funziona non capisco piango
                out.println(imageData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }//chatgpt
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
