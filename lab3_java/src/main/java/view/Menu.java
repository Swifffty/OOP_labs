package view;

import controller.ActionController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import model.ScoreTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Menu extends Application {
    private static final Logger log = LoggerFactory.getLogger(Menu.class);
    public static void main(String[] args) {
        log.info("Запуск приложения Tetris");
        launch(args);
    }

    @Override
    public void stop() {
        ScoreTable.saveToFile();
        log.info("Данные сохранены, приложение завершило работу");
    }

    private TextFlow createTetrisLogo() {
        String word = "TETRIS";
        Color[] colors = {Color.CYAN, Color.YELLOW, Color.PURPLE, Color.LIME, Color.RED, Color.ORANGE};
        TextFlow textFlow = new TextFlow();

        for (int i = 0; i < word.length(); i++) {
            Text letter = new Text(String.valueOf(word.charAt(i)));
            letter.setFont(Font.font("Monospaced", FontWeight.BOLD, 60));
            letter.setFill(colors[i % colors.length]);
            letter.setStroke(Color.BLACK);
            letter.setStrokeWidth(1);
            textFlow.getChildren().add(letter);
        }
        return textFlow;
    }

    @Override
    public void start(Stage primaryStage) {
        log.info("Инициализация главного меню");

        Button startButton = new Button("Start");
        Button scoreButton = new Button("Table Score");

        String buttonStyle = "-fx-background-color: #3c3f41; " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 5; " +
                "-fx-padding: 10 20 10 20;";

        startButton.setStyle(buttonStyle);
        scoreButton.setStyle(buttonStyle);
        startButton.setPrefSize(200, 50);
        scoreButton.setPrefSize(200, 50);
        startButton.setFont(Font.font("Arial", 20));
        scoreButton.setFont(Font.font("Arial", 20));

        TextFlow nameGame = createTetrisLogo();
        nameGame.setTextAlignment(TextAlignment.CENTER);

        VBox root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(100, 0, 0, 0));
        root.getChildren().addAll(nameGame, startButton, scoreButton);
        root.setSpacing(40);
        root.setStyle("-fx-background-color: #2b2b2b;");

        Scene scene = new Scene(root, 500, 500);
        primaryStage.setTitle("TetrisMenu");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        startButton.setOnAction(event -> {
            log.info("Начало новой игры");
            ActionController.PressKey(event);
            primaryStage.close();
        });

        scoreButton.setOnAction(event -> {
            log.info("Открытие таблицы рекордов");
            ActionController.PressKey(event);
            primaryStage.close();
        });
    }
}