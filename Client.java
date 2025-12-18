import java.io.*;
import java.net.*;

public class Client {

    private static final String SERVER_NAME = "localhost";
    private static final int PORT = 8000;

    public static void main(String[] args) {

        try {
            Socket socket = new Socket(SERVER_NAME, PORT);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true);

            BufferedReader keyboard = new BufferedReader(
                    new InputStreamReader(System.in));

            // Thread to receive messages from server
            Thread receiveThread = new Thread(() -> {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        System.out.println(msg);
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server");
                }
            });

            receiveThread.start();

            // Send messages to server
            String userInput;
            while ((userInput = keyboard.readLine()) != null) {
                out.println(userInput);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
