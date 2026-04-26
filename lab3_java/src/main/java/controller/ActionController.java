package controller;

import javafx.animation.KeyFrame;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.ScoreTable;
import model.TetrisModel;

import javafx.scene.input.KeyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.ScoreStage;

public class ActionController {
    private static final Logger log = LoggerFactory.getLogger(ActionController.class);
    private static TetrisModel model;
    private static ScoreTable scoreTable = new ScoreTable();

    private static void MenuAction(Button button) {
        log.info("Контроллер: Обработка нажатия кнопки меню: {}", button.getText());
        switch (button.getText()) {
            case "Start" -> {
                log.info("Контроллер: Инициализация новой модели игры");
                model = new TetrisModel();
            }
            case "Table Score" -> {
                log.info("Контроллер: Запрос данных таблицы рекордов");
                new ScoreStage().show(scoreTable.getScoreTable());
            }
        }
    }

    private static void GameAction(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT -> model.shiftLeft();
            case RIGHT -> model.shiftRight();
            case UP -> model.rotateUp();
            case SPACE -> model.hardDrop();
            default -> {}
        }
    }

    public static void PressKey(Event event) {
        if (event instanceof KeyEvent keyEvent) {
            GameAction(keyEvent);
        } else if (event.getSource() instanceof Button btn) {
            MenuAction(btn);
        } else if (event.getSource() instanceof KeyFrame) {
            model.shapeDown();
        }
    }

    public static void endGame(TextField nameInput, int score) {
        String playerName = nameInput.getText().trim();
        if (playerName.isEmpty()) {
            log.info("Контроллер: Поле имени пустое, присвоено имя Anonymous");
            playerName = "Anonymous";
        }
        log.info("Контроллер: Финализация игры. Игрок: {}, Счет: {}", playerName, score);
        scoreTable.newScore(score, playerName);
    }
}