package xml.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.List;

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

        networkClient = new NetworkClient("localhost", 8080,
                this::appendChatMessage,
                this::updateUserList,
                this::appendSystemEvent);
        try {
            networkClient.connect(currentUsername);
        } catch (Exception e) {
            chatArea.appendText("Ошибка XML подключения: " + e.getMessage() + "\n");
        }
    }

    private void sendMessage() {
        String text = messageField.getText().trim();
        if (!text.isEmpty()) {
            networkClient.sendChatMessage(text, selectedRecipient);
            messageField.clear();
        }
    }

    private void appendChatMessage(String msg) {
        Platform.runLater(() -> chatArea.appendText(msg + "\n"));
    }

    private void appendSystemEvent(String event) {
        Platform.runLater(() -> chatArea.appendText(event + "\n"));
    }

    private void updateUserList(List<String> users) {
        Platform.runLater(() -> {
            usersList.getItems().clear();
            usersList.getItems().addAll(users);
        });
    }
}