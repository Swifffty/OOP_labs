package xml.server;

import xml.shared.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.UUID;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final ChatServer server;
    private DataOutputStream out;
    private DataInputStream in;

    private String username;
    private String sessionId;
    private boolean isDisconnected = false;

    public ClientHandler(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            socket.setSoTimeout(300000);

            while (true) {
                String xmlData = readXml();
                handleXmlCommand(xmlData);
            }
        } catch (SocketTimeoutException e) {
            System.out.println("Таймаут: " + username);
        } catch (Exception e) {
            System.out.println("Соединение потеряно: " + username);
        } finally {
            disconnect();
        }
    }

    private String readXml() throws IOException {
        int length = in.readInt();
        byte[] data = new byte[length];
        in.readFully(data);
        return new String(data, "UTF-8");
    }

    public void sendXml(String xmlString) {
        try {
            byte[] data = xmlString.getBytes("UTF-8");
            out.writeInt(data.length);
            out.write(data);
            out.flush();
        } catch (IOException e) {
            System.err.println("Ошибка отправки XML: " + e.getMessage());
        }
    }

    private void handleXmlCommand(String xmlData) {
        try {
            Document doc = XmlUtils.parseXml(xmlData);
            Element root = doc.getDocumentElement();

            if (root.getTagName().equals("command")) {
                String commandName = root.getAttribute("name");

                switch (commandName) {
                    case "login":
                        String requestedName = root.getElementsByTagName("name").item(0).getTextContent();
                        if (server.getClientsName().contains(requestedName)) {
                            sendXml("<error><message>Username is already taken</message></error>");
                            socket.close();
                        } else {
                            this.username = requestedName;
                            this.sessionId = UUID.randomUUID().toString();
                            server.broadcast("<event name=\"userlogin\"><name>" + username + "</name></event>");
                            server.addClient(this);
                            StringBuilder successResponse = new StringBuilder("<success>");
                            successResponse.append("<session>").append(sessionId).append("</session>");

                            successResponse.append("<history>");
                            for (String oldMsgXml : server.getHistory()) {
                                successResponse.append(oldMsgXml);
                            }
                            successResponse.append("</history>");
                            successResponse.append("</success>");
                            sendXml(successResponse.toString());
                        }
                        break;

                    case "message":
                        String incomingSession = root.getElementsByTagName("session").item(0).getTextContent();
                        if (this.sessionId.equals(incomingSession)) {
                            String msgText = root.getElementsByTagName("message").item(0).getTextContent();
                            String recipient = null;
                            if (root.getElementsByTagName("to").getLength() > 0) {
                                recipient = root.getElementsByTagName("to").item(0).getTextContent();
                            }

                            if (recipient != null) {
                                String privateEventXml = "<event name=\"message\" type=\"private\">" +
                                        "<message>" + msgText + "</message>" +
                                        "<name>" + username + "</name>" +
                                        "<to>" + recipient + "</to></event>";

                                server.sendPrivateXml(privateEventXml, username, recipient);
                            } else {
                                String eventXml = "<event name=\"message\">" +
                                        "<message>" + msgText + "</message>" +
                                        "<name>" + username + "</name></event>";
                                server.broadcast(eventXml);
                            }
                            sendXml("<success></success>");
                        } else {
                            sendXml("<error><message>Invalid Session</message></error>");
                        }
                        break;

                    case "list":
                        StringBuilder listXml = new StringBuilder("<success><listusers>");
                        for (String user : server.getClientsName()) {
                            listXml.append("<user><name>").append(user).append("</name><type>Client</type></user>");
                        }
                        listXml.append("</listusers></success>");
                        sendXml(listXml.toString());
                        break;

                    case "logout":
                        disconnect();
                        break;
                }
            }
        } catch (Exception e) {
            sendXml("<error><message>Bad XML Format</message></error>");
        }
    }

    public String getUsername() { return username; }

    private synchronized void disconnect() {
        if (isDisconnected) return;
        isDisconnected = true;

        server.removeClient(this);
        server.broadcast("<event name=\"userlogout\"><name>" + username + "</name></event>");
        try { socket.close(); } catch (IOException ignored) {}
    }
}