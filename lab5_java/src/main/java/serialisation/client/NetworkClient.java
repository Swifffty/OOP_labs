package serialisation.client;

import serialisation.shared.EventMessage;
import serialisation.shared.LoginMessage;
import serialisation.shared.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.Consumer;

public class NetworkClient {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private final String host;
    private final int port;
    private String nameUser;

    private Consumer<Message> onMessageReceived;

    public NetworkClient(String host, int port, Consumer<Message> onMessageReceived) {
        this.host = host;
        this.port = port;
        this.onMessageReceived = onMessageReceived;
    }

    public void connect(String username) throws IOException {
        this.nameUser = username;

        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());

        new Thread(new IncomingReader()).start();

        sendMessage(new LoginMessage(username));
    }

    public void sendMessage(Message message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            System.err.println("Ошибка отправки: " + e.getMessage());
        }
    }

    public void disconnect() {
        try {
            out.writeObject(new EventMessage("USER_LOGOUT", nameUser));
            out.flush();
            socket.close();
        } catch (Exception ignored) {
        }
    }

    private class IncomingReader implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    Message message = (Message) in.readObject();
                    onMessageReceived.accept(message);
                }
            } catch (Exception e) {
                System.out.println("Отключено от сервера");
            }
        }
    }
}