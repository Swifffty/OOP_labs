package serialisation.server;

import serialisation.shared.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final ChatServer server;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String username;

    public ClientHandler(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            socket.setSoTimeout(300000);

            while (true) {
                Message message = (Message) in.readObject(); // ????
                handleMessage(message);
            }
        } catch (SocketTimeoutException e) {
            System.out.println("Клиент отключен по таймауту: " + username);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Соединение с клиентом потеряно: " + username);
        } finally {
            disconnect();
        }
    }

    private void handleMessage(Message message) {
        if (message instanceof LoginMessage) {
            this.username = ((LoginMessage) message).getUsername();
            server.broadcast(new EventMessage("USER_LOGIN", username));
            server.addClient(this);
            sendMessage(new UserListMessage(server.getClientsName()));

            for (Message msg : server.getHistory()) {
                sendMessage(msg);
            }

        } else if (message instanceof ChatMessage) {
            server.broadcast(message);
        }
    }

    public void sendMessage(Message message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void disconnect() {
        server.removeClient(this);
        server.broadcast(new EventMessage("USER_LOGOUT", username));
        try { socket.close(); } catch (IOException ignored) {}
    }

    public String getUsername() {
        return username;
    }

}
