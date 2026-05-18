package xml.client;

import xml.shared.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class NetworkClient {
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private final String host;
    private final int port;

    private String sessionId;
    private String myUsername;
    private final Consumer<String> onMessageReceived;
    private final Consumer<List<String>> onUserListUpdated;
    private final Consumer<String> onSystemEvent;

    public NetworkClient(String host, int port, Consumer<String> onMessageReceived,
                         Consumer<List<String>> onUserListUpdated, Consumer<String> onSystemEvent) {
        this.host = host;
        this.port = port;
        this.onMessageReceived = onMessageReceived;
        this.onUserListUpdated = onUserListUpdated;
        this.onSystemEvent = onSystemEvent;
    }

    public void connect(String username) throws IOException {
        this.myUsername = username;
        socket = new Socket(host, port);
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());

        new Thread(new IncomingReader()).start();

        String loginXml = "<command name=\"login\">" +
                "<name>" + username + "</name>" +
                "<type>CHAT_CLIENT_XML</type>" +
                "</command>";
        sendXml(loginXml);
    }

    public void sendChatMessage(String text, String recipient) {
        if (sessionId == null) return;

        StringBuilder msgXml = new StringBuilder("<command name=\"message\">");
        msgXml.append("<message>").append(text).append("</message>");
        if (recipient != null) {
            msgXml.append("<to>").append(recipient).append("</to>");
        }
        msgXml.append("<session>").append(sessionId).append("</session>");
        msgXml.append("</command>");

        sendXml(msgXml.toString());
    }

    public void requestUserList() {
        if (sessionId == null) return;
        String listXml = "<command name=\"list\"><session>" + sessionId + "</session></command>";
        sendXml(listXml);
    }

    private void sendXml(String xmlString) {
        try {
            byte[] data = xmlString.getBytes("UTF-8");
            out.writeInt(data.length);
            out.write(data);
            out.flush();
        } catch (IOException e) {
            System.err.println("Ошибка отправки: " + e.getMessage());
        }
    }

    private class IncomingReader implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    int length = in.readInt();
                    byte[] data = new byte[length];
                    in.readFully(data);
                    String xmlData = new String(data, "UTF-8");

                    Document doc = XmlUtils.parseXml(xmlData);
                    Element root = doc.getDocumentElement();

                    if (root.getTagName().equals("success")) {
                        if (root.getElementsByTagName("session").getLength() > 0) {
                            sessionId = root.getElementsByTagName("session").item(0).getTextContent();

                            NodeList historyNodes = root.getElementsByTagName("history");
                            if (historyNodes.getLength() > 0) {
                                Element historyEl = (Element) historyNodes.item(0);
                                NodeList savedMessages = historyEl.getElementsByTagName("event");

                                for (int i = 0; i < savedMessages.getLength(); i++) {
                                    Element eventEl = (Element) savedMessages.item(i);

                                    if ("message".equals(eventEl.getAttribute("name"))) {
                                        String sender = eventEl.getElementsByTagName("name").item(0).getTextContent();
                                        String text = eventEl.getElementsByTagName("message").item(0).getTextContent();
                                        onMessageReceived.accept(sender + ": " + text);
                                    }
                                }
                            }
                            requestUserList();
                        }
                        else if (root.getElementsByTagName("listusers").getLength() > 0) {
                            List<String> users = new ArrayList<>();
                            NodeList userNodes = root.getElementsByTagName("user");
                            for (int i = 0; i < userNodes.getLength(); i++) {
                                Element userEl = (Element) userNodes.item(i);
                                users.add(userEl.getElementsByTagName("name").item(0).getTextContent());
                            }
                            onUserListUpdated.accept(users);
                        }
                    }
                    else if (root.getTagName().equals("event")) {
                        String eventName = root.getAttribute("name");

                        if (eventName.equals("message")) {
                            String sender = root.getElementsByTagName("name").item(0).getTextContent();
                            String text = root.getElementsByTagName("message").item(0).getTextContent();

                            String type = root.getAttribute("type");
                            if ("private".equals(type)) {
                                String to = root.getElementsByTagName("to").item(0).getTextContent();
                                onMessageReceived.accept("[ЛС] " + sender + " -> " + to + ": " + text);
                            } else {
                                onMessageReceived.accept(sender + ": " + text);
                            }
                        }
                        else if (eventName.equals("userlogin")) {
                            String newUser = root.getElementsByTagName("name").item(0).getTextContent();
                            onSystemEvent.accept(">>> " + newUser + " присоединился к чату.");
                            requestUserList();
                        }
                        else if (eventName.equals("userlogout")) {
                            String goneUser = root.getElementsByTagName("name").item(0).getTextContent();
                            onSystemEvent.accept("<<< " + goneUser + " покинул чат.");
                            requestUserList();
                        }
                    }
                }
            } catch (Exception e) {
                onSystemEvent.accept("Соединение с сервером закрыто.");
            }
        }
    }

    public void disconnect() {
        try {
            if (sessionId != null) {
                String logoutXml = "<command name=\"logout\"><session>" + sessionId + "</session></command>";
                sendXml(logoutXml);
            }
            socket.close();
        } catch (Exception ignored) {
        }
    }
}