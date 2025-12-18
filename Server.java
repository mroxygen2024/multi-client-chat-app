import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    private static final int PORT = 8000;
    private static Set<ClientHandler> clients = new HashSet<>();

    public static void main(String[] args) {

        System.out.println("Server started on port " + PORT);

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);

            while (true) {
                Socket socket = serverSocket.accept(); // connect to client
                System.out.println("New client connected");

                ClientHandler handler = new ClientHandler(socket);
                clients.add(handler);

                Thread thread = new Thread(handler);
                thread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Send message to all clients except sender
    public static void broadcast(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    public static void removeClient(ClientHandler client) {
        clients.remove(client);
    }
}

class ClientHandler implements Runnable {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String message;

            while ((message = in.readLine()) != null) {
                System.out.println("Received: " + message);
                Server.broadcast(message, this);
            }

        } catch (IOException e) {
            System.out.println("Client disconnected");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
            Server.removeClient(this);
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}
