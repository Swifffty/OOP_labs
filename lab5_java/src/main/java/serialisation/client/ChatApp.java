package serialisation.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import serialisation.shared.ChatMessage;
import serialisation.shared.EventMessage;
import serialisation.shared.Message;
import serialisation.shared.UserListMessage;

public class ChatApp extends Application {

    private NetworkClient networkClient;
    private String currentUsername;
    private String selectedRecipient = null;

    private TextArea chatArea;
    private TextField messageField;
    private ListView<String> usersList;
    private Label sendToLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Chat");

        VBox loginLayout = new VBox(10);
        loginLayout.setPadding(new Insets(20));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Введите никнейм");

        Button connectBtn = new Button("Войти в чат");
        connectBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: black;");

        loginLayout.getChildren().addAll(
                new Label("Имя пользователя:") {{ setStyle("-fx-text-fill: black;"); }},
                usernameField,
                connectBtn
        );

        Scene loginScene = new Scene(loginLayout, 300, 200);
        primaryStage.setScene(loginScene);
        primaryStage.show();

        connectBtn.setOnAction(e -> {
            String username = usernameField.getText().trim();
            if (!username.isEmpty()) {
                currentUsername = username;
                initChatWindow(primaryStage);
            }
        });
    }

    private void initChatWindow(Stage stage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        chatArea = new TextArea();
        chatArea.setEditable(false);
        root.setCenter(chatArea);

        usersList = new ListView<>();
        usersList.setPrefWidth(150);
        usersList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(currentUsername)) {
                selectedRecipient = newValue;
                sendToLabel.setText("Кому: " + selectedRecipient + " (ЛС)");
                sendToLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
            } else {
                selectedRecipient = null;
                sendToLabel.setText("Кому: Всем");
                sendToLabel.setStyle("-fx-text-fill: gray;");
            }
        });
        root.setRight(usersList);

        VBox bottomBox = new VBox(5);
        sendToLabel = new Label("Кому: Всем");
        sendToLabel.setStyle("-fx-text-fill: gray;");

        HBox inputControl = new HBox(10);
        messageField = new TextField();
        HBox.setHgrow(messageField, Priority.ALWAYS);
        Button sendBtn = new Button("Отправить");
        inputControl.getChildren().addAll(messageField, sendBtn);

        bottomBox.getChildren().addAll(sendToLabel, inputControl);
        root.setBottom(bottomBox);

        sendBtn.setOnAction(e -> sendMessage());
        messageField.setOnAction(e -> sendMessage());

        Scene chatScene = new Scene(root, 600, 400);
        stage.setScene(chatScene);

        stage.setOnCloseRequest(event -> {
            networkClient.disconnect();
            Platform.exit();
            System.exit(0);
        });

        networkClient = new NetworkClient("localhost", 8080, this::handleIncomingMessage);
        try {
            networkClient.connect(currentUsername);
        } catch (Exception e) {
            chatArea.appendText("Ошибка подключения: " + e.getMessage() + "\n");
        }
    }

    private void sendMessage() {
        String text = messageField.getText().trim();
        if (!text.isEmpty()) {
            networkClient.sendMessage(new ChatMessage(currentUsername, text, selectedRecipient));
            messageField.clear();
        }
    }

    private void handleIncomingMessage(Message message) {
        Platform.runLater(() -> {
            if (message instanceof ChatMessage) {
                ChatMessage chatMsg = (ChatMessage) message;
                if (chatMsg.getRecipient() != null) {
                    chatArea.appendText("[ЛС] " + chatMsg.getSender() + " -> " + chatMsg.getRecipient() + ": " + chatMsg.getText() + "\n");
                } else {
                    chatArea.appendText(chatMsg.getSender() + ": " + chatMsg.getText() + "\n");
                }
            }
            else if (message instanceof UserListMessage) {
                UserListMessage usersMsg = (UserListMessage) message;
                usersList.getItems().clear();
                usersList.getItems().addAll(usersMsg.getUsers());
            }
            else if (message instanceof EventMessage) {
                EventMessage eventMsg = (EventMessage) message;
                if ("USER_LOGIN".equals(eventMsg.getEventType())) {
                    chatArea.appendText(">>> " + eventMsg.getUsername() + " присоединился к чату.\n");
                    usersList.getItems().add(eventMsg.getUsername());
                } else if ("USER_LOGOUT".equals(eventMsg.getEventType())) {
                    chatArea.appendText("<<< " + eventMsg.getUsername() + " покинул чат.\n");
                    usersList.getItems().remove(eventMsg.getUsername());
                    usersList.getSelectionModel().clearSelection();
                }
            }
        });
    }
}