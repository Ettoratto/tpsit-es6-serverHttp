package itismeucci.tpsit;

public class ServerMain {
    public static void main(String[] args) {

        ServerClass server = new ServerClass(8000);

        server.connect();
    }

}   