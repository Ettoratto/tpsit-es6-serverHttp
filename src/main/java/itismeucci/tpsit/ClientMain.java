package itismeucci.tpsit;


public class ClientMain {
    public static void main(String[] args) {

        ClientClass client = new ClientClass("localhost", 6789);
        client.connect();
        client.communicate();
    }
}
