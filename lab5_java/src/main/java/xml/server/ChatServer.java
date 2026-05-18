package xml.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatServer {
    private final int port;
    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private final LinkedList<String> history = new LinkedList<>();
    private final int HISTORY_SIZE = 10;

    public ChatServer(int port) {
        this.port = port;
    }

    public void start() {
        System.out.println("Сервер запущен на порту: " + port);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Новое физическое подключение: " + socket.getRemoteSocketAddress());
                ClientHandler handler = new ClientHandler(socket, this);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            System.err.println("Ошибка запуска сервера: " + e.getMessage());
        }
    }

    public void addClient(ClientHandler client) {
        clients.add(client);
        System.out.println("Пользователь " + client.getUsername() + " успешно авторизован.");
    }

    public void removeClient(ClientHandler client) {
        clients.remove(client);
        System.out.println("Пользователь " + client.getUsername() + " отключился.");
    }

    public void broadcast(String xmlString) {
        if (xmlString.contains("name=\"message\"") && !xmlString.contains("type=\"private\"")) {
            if (history.size() >= HISTORY_SIZE) {
                history.removeFirst();
            }
            history.add(xmlString);
        }
        for (ClientHandler client : clients) {
            client.sendXml(xmlString);
        }
    }

    public void sendPrivateXml(String xmlString, String sender, String recipient) {
        for (ClientHandler client : clients) {
            if (client.getUsername().equals(sender) || client.getUsername().equals(recipient)) {
                client.sendXml(xmlString);
            }
        }
    }

    public synchronized List<String> getHistory() {
        return new ArrayList<>(history);
    }

    public synchronized List<String> getClientsName() {
        List<String> names = new ArrayList<>();
        for (ClientHandler client : clients) {
            names.add(client.getUsername());
        }
        return names;
    }

    public static void main(String[] args) {
        int port = 8080;
        ChatServer server = new ChatServer(port);
        server.start();
    }
}