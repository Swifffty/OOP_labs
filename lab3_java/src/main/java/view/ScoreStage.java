package view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ScoreStage {
    private static final Logger log = LoggerFactory.getLogger(ScoreStage.class);

    public void show(Map<Integer, String> scoreTable) {
        log.info("Открытие таблицы рекордов (записей: {})", scoreTable.size());

        Stage stage = new Stage();
        stage.setTitle("Top 10 Scores");

        VBox root = new VBox(20);
        root.setPadding(new javafx.geometry.Insets(20));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #2b2b2b;");

        ListView<String> listView = new ListView<>();
        listView.setPrefWidth(400);
        listView.setPrefHeight(550);
        listView.setStyle("-fx-font-size: 16px; " +
                "-fx-background-radius: 5; " +
                "-fx-control-inner-background: #3c3f41; " +
                "-fx-text-fill: white;");

        int rank = 1;
        for (Map.Entry<Integer, String> entry : scoreTable.entrySet()) {
            listView.getItems().add(rank + ". " + entry.getValue() + " — " + entry.getKey());
            rank++;
        }

        if (listView.getItems().isEmpty()) {
            log.info("Таблица рекордов пуста");
            listView.getItems().add("No records yet.");
        }

        Button closeBtn = new Button("Back to Menu");
        closeBtn.setFont(Font.font(16));
        closeBtn.setStyle("-fx-background-color: #3c3f41; -fx-text-fill: white; -fx-background-radius: 5;");

        closeBtn.setOnAction(e -> {
            log.info("Возврат в главное меню");
            try {
                new Menu().start(new Stage());
                stage.close();
            } catch (Exception ex) {
                log.error("Ошибка при возврате в меню", ex);
            }
        });

        root.getChildren().addAll(listView, closeBtn);

        Scene scene = new Scene(root, 400, 550);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}