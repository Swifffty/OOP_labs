package serialisation;

import serialisation.server.ChatServer;

public class MainServer {
    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer(8080);
        chatServer.start();
    }
}
