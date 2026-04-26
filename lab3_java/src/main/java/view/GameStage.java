package view;

import controller.ActionController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class GameStage {
    private static final Logger log = LoggerFactory.getLogger(GameStage.class);
    private Stage stage;
    private Text scoreText;
    private VBox rootMenu;
    private int lastScore;
    private GridPane field;
    private GridPane nextShape;
    private static final int columns = 12;
    private static final int rows = 22;
    private static final double tileSize = 40;
    private Timeline timeline;
    private double rate = 1.0;
    private Rectangle[][] cells = new Rectangle[rows][columns];
    private Rectangle[][] cellsNext = new Rectangle[4][4];
    private Line[] lines = new Line[columns];
    private int MaxPosY = -1;
    private int MinPosY = columns;
    private VBox rootGame;
    private HBox root;
    private boolean volMusic = true;
    private final Color[] colors = new Color[] {
            Color.RED,
            Color.CYAN,
            Color.LIME,
            Color.YELLOW
    };
    private MediaPlayer mediaPlayer;

    public GameStage() {
        log.info("Создание игрового окна и сетки {}x{}", columns, rows);
        lastScore = 0;
        field = new GridPane();
        nextShape = new GridPane();

        for (int i = 0; i < columns; i++) {
            field.getColumnConstraints().add(new ColumnConstraints(tileSize));
            if (i < 4) {
                nextShape.getColumnConstraints().add(new ColumnConstraints(tileSize));
            }
        }
        for (int i = 0; i < rows; i++) {
            field.getRowConstraints().add(new RowConstraints(tileSize));
            if (i < 4) {
                nextShape.getRowConstraints().add(new RowConstraints(tileSize));
            }
        }

        field.setAlignment(Pos.CENTER);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Rectangle rect = new Rectangle(tileSize, tileSize, Color.TRANSPARENT);
                rect.setStroke(Color.web("#444444"));

                field.add(rect, j, i);
                cells[i][j] = rect;
            }
        }

        for (int j = 1; j < columns; j++) {
            Line line = new Line(0, 0, 0, (tileSize * rows));
            line.setStroke(Color.web("#444444"));
            field.add(line, j, 0);
            GridPane.setRowSpan(line, rows);
            lines[j] = line;
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Rectangle rect = new Rectangle(tileSize, tileSize, Color.TRANSPARENT);
                rect.setStroke(Color.web("#555555"));
                nextShape.add(rect, j, i);
                cellsNext[i][j] = rect;
            }
        }

        field.setMaxWidth(Region.USE_PREF_SIZE);
        field.setMaxHeight(Region.USE_PREF_SIZE);
        field.setStyle("-fx-border-color: #DCDCDC; -fx-border-width: 3;");


        scoreText = new Text("Score: 0");
        scoreText.setFont(Font.font("Arial", 30));
        scoreText.setFill(Color.WHITE);

        StackPane scorePane = new StackPane(scoreText);
        scorePane.setPrefHeight(100);

        Button musicBtn = new Button("Music");

        musicBtn.setStyle("-fx-background-color: #3c3f41; " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 5; " +
                "-fx-padding: 10 20 10 20;");
        musicBtn.setPrefSize(250, 50);
        musicBtn.setFont(Font.font("Arial", 20));
        musicBtn.setFocusTraversable(false);


        root = new HBox();
        root.setStyle("-fx-background-color: #2b2b2b;");

        rootGame = new VBox();
        rootGame.setAlignment(Pos.TOP_LEFT);
        rootGame.getChildren().addAll(field);
        rootGame.setPrefSize(525, 940);

        rootMenu = new VBox();
        rootMenu.getChildren().addAll(musicBtn, scorePane, nextShape);

        root.getChildren().addAll(rootGame, rootMenu);
        stage = new Stage();
        stage.setTitle("Game");
        Scene scene = new Scene(root, 700, 885);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        URL musicURL = getClass().getResource("/03-b-type-music.wav");
        if (musicURL != null) {
            Media media = new Media(musicURL.toExternalForm());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setVolume(0.3);
            mediaPlayer.play();
        } else {
            System.err.println("Файл с музыкой не найден!");
        }


        initGameLoop();

        musicBtn.setOnAction(Event -> {
            if (volMusic) {
                mediaPlayer.stop();
                volMusic = false;
            } else {
                mediaPlayer.play();
                volMusic = true;
            }
        });

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DOWN) {
                log.info("Ускорение падения");
                timeline.setRate(10.0);
            } else {
                ActionController.PressKey(event);
            }
        });


        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.DOWN) {
                log.info("Обычная скорость падения");
                timeline.setRate(rate);
            }
        });
    }


    public void setNextShape(int[][] coords, int color) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                cellsNext[i][j].setFill(Color.TRANSPARENT);
            }
        }
        int x0 = 1;
        int y0 = 1;
        for (int[] x : coords) {
            cellsNext[x0 + x[0]][y0 + x[1]].setFill(colors[color]);
        }

    }

    public void initGameLoop() {
        log.info("Запуск игрового таймера (базовая скорость 500ms)");
        timeline = new Timeline(new KeyFrame(Duration.millis(500), event -> ActionController.PressKey(event)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void changeScore(int newScore) {
        scoreText.setText("Score: " + newScore);
        if (newScore - lastScore >= 500) {
            rate += 0.2;
            timeline.setRate(rate);
            lastScore = newScore;
            log.info("Счёт: {}. Ускорение падения до x{}", newScore, String.format("%.1f", timeline.getRate()));
        }
    }

    private void setPanes(int Pos, Color color) {
        if (Pos < columns && Pos > 0) {
            lines[Pos].setStroke(color);
        }
    }

    public void getChange(int[][] GameFiled, int numberColor) {
        setPanes(MaxPosY, Color.web("#444444"));
        setPanes(MinPosY, Color.web("#444444"));

        MaxPosY = -1;
        MinPosY = columns;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                int state = GameFiled[i][j];
                switch (state) {
                    case 1 -> {
                        cells[i][j].setFill(colors[numberColor]);
                        MaxPosY = Math.max(MaxPosY, j);
                        MinPosY = Math.min(MinPosY, j);
                    }
                    case 0, -1 -> cells[i][j].setFill(Color.TRANSPARENT);
                    default -> cells[i][j].setFill(colors[GameFiled[i][j] - 2]);
                }
            }
        }
        MaxPosY++;

        setPanes(MinPosY, Color.WHITESMOKE);
        setPanes(MaxPosY, Color.WHITESMOKE);
    }

    public void GameOver(int score) {
        log.info("Игра завершена. Итоговый счёт: {}", score);
        timeline.stop();
        rootGame.getChildren().clear();

        root.getChildren().remove(rootMenu);
        root.setAlignment(Pos.CENTER);

        Text gameOverText = new Text("GAME OVER");
        gameOverText.setFont(Font.font("Arial", 50));
        gameOverText.setFill(Color.RED);

        scoreText.setText("Your Score: " + score);

        TextField nameInput = new TextField();
        nameInput.setPromptText("Enter your name");
        nameInput.setMaxWidth(250);
        nameInput.setStyle("-fx-background-color: #3c3f41; -fx-text-fill: white; -fx-font-size: 20px;");
        nameInput.setAlignment(Pos.CENTER);

        Button saveBtn = new Button("Save Result");
        saveBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-size: 18px;");

        saveBtn.setOnAction(e -> {
            log.info("Сохранение результата для игрока: {}", nameInput.getText());
            ActionController.endGame(nameInput, score);
            saveBtn.setDisable(true);
            new Menu().start(new Stage());
            mediaPlayer.stop();
            stage.close();
        });

        rootGame.getChildren().addAll(gameOverText, scoreText, nameInput, saveBtn);
        rootGame.setSpacing(30);
        rootGame.setAlignment(Pos.CENTER);
    }
}