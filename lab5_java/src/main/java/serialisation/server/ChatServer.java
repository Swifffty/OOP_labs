package serialisation.server;

import serialisation.shared.ChatMessage;
import serialisation.shared.Message;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

public class ChatServer {
    private static final Logger log = Logger.getLogger(ChatServer.class.getName());

    private final int port;
    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private final LinkedList<Message> history = new LinkedList<>();
    private final int HISTORY_SIZE = 10;
    private final ArrayList<String> clientsName = new ArrayList<>();

    public ChatServer(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("Сервер запущен на порту: " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                log.info("Новое подключение: " + clientSocket.getInetAddress());
                ClientHandler handler = new ClientHandler(clientSocket, this);
                new Thread(handler).start();

            }
        } catch (IOException e) {
            log.severe("Ошибка сервера: " + e.getMessage());
        }
    }

    public synchronized void broadcast(Message message) {
        if (message instanceof ChatMessage) {
            ChatMessage chatMsg = (ChatMessage) message;
            if (chatMsg.getRecipient() != null) {
                for (ClientHandler client : clients) {
                    if (client.getUsername().equals(chatMsg.getRecipient()) ||
                            client.getUsername().equals(chatMsg.getSender())) {
                        client.sendMessage(message);
                    }
                }
                return;
            }
            if (history.size() >= HISTORY_SIZE) {
                history.removeFirst();
            }
            history.add(message);
        }
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    public void addClient(ClientHandler client) {
        clients.add(client);
        clientsName.add(client.getUsername());
    }

    public void removeClient(ClientHandler client) {
        clients.remove(client);
        clientsName.remove(client.getUsername());
    }

    public synchronized List<Message> getHistory() {
        return Collections.unmodifiableList(new ArrayList<>(history));
    }

    public synchronized List<String> getClientsName() {
        return Collections.unmodifiableList(new ArrayList<>(clientsName));}
}