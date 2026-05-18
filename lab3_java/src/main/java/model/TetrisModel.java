package model;
// 1. отрисовка границ на поле 2 луча по ширине +
// 2. отображание след фигуры в меню +
// 3. скольжение +
// 4. score в меню +
// 5. музыка с мьютом +
// 6. фигура появляется сразу +
// 7. рамка поля +



import model.figure.IFigure;
import model.figure.Square;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.GameStageI;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class TetrisModel {
    private static final Logger log = LoggerFactory.getLogger(TetrisModel.class);
    private final int countRows = 22;
    private final int countCol = 12;
    public final GameStageI stage;
    private Map<Integer, IFigure> shapes;
    private int[][] gameFiled = new int[22][12];
    private int[] currentPos = new int[2];
    private IFigure shapeNow;
    private int score;
    private int currentColor;
    private List<Integer> PermittedShape = new ArrayList<>();
    private int CountShape;
    private int nextColor;
    private int nextShape = -1;

    private void GetShapes() {
        log.info("Загрузка конфигурации фигур из файла /ConfigShape");
        try (InputStream is = getClass().getResourceAsStream("/ConfigShape")) {
            if (is == null) {
                log.error("Критическая ошибка: файл конфигурации /ConfigShape не найден!");
                throw new RuntimeException("Файл не найден!");
            }
            try (BufferedReader readerConfig = new BufferedReader(new InputStreamReader(is))) {
                String line;
                CountShape = 0;
                while ((line = readerConfig.readLine()) != null) {
                    String[] args = line.split(" ");
                    Class<? extends IFigure> clazz = (Class<? extends IFigure>) Class.forName(args[1]);
                    IFigure shape = clazz.getDeclaredConstructor().newInstance();
                    shapes.put(Integer.valueOf(args[0]), shape);
                    CountShape++;
                }
                log.info("Успешно загружено фигур: {}", CountShape);
            }
        } catch (Exception e) {
            log.error("Ошибка при чтении конфигурации: {}", e.getMessage());
            System.err.println("Проверьте конфиг" + e.getMessage());
        }
    }

    private int[][] copyGameFiled() {
        int[][] copy = new int[countRows][countCol];
        for (int i = 0; i < countRows; i++) {
            copy[i] = gameFiled[i].clone();
        }
        return copy;
    }

    private void NewShape() {
        int randomNumber;
        if (nextShape == -1) {
            int randomIndex = ThreadLocalRandom.current().nextInt(PermittedShape.size());
            randomNumber = PermittedShape.get(randomIndex);
            PermittedShape.remove(randomIndex);
            currentColor = ThreadLocalRandom.current().nextInt(0, 4);

            randomIndex = ThreadLocalRandom.current().nextInt(PermittedShape.size());
            nextShape = PermittedShape.get(randomIndex);
            PermittedShape.remove(randomIndex);
            nextColor = ThreadLocalRandom.current().nextInt(0, 4);
        } else {
            randomNumber = nextShape;
            currentColor = nextColor;
            int randomIndex = ThreadLocalRandom.current().nextInt(PermittedShape.size());
            nextShape = PermittedShape.get(randomIndex);
            PermittedShape.remove(randomIndex);
            if (PermittedShape.size() < 2) {
                refreshShape();
            }
            nextColor = ThreadLocalRandom.current().nextInt(0, 4);
        }

        log.info("Генерация новой фигуры. Выбран ID: {}", randomNumber);

        shapeNow = (shapes.get(randomNumber)).getCopy();
        int[][] coords = shapeNow.getCoords();
        currentPos[0] = 0;
        currentPos[1] = 4;

        int absolutPosX;
        int absolutPosY;

        for (int[] x : coords) {
            absolutPosX = currentPos[0] + x[0];
            absolutPosY = currentPos[1] + x[1];

            if (absolutPosX >= 0 && gameFiled[absolutPosX][absolutPosY] > 1) {
                log.info("Игра окончена: новая фигура столкнулась с установленными блоками при появлении");
                stage.GameOver(score);
                return;
            }
        }

        for (int[] x : coords) {
            absolutPosX = currentPos[0] + x[0];
            absolutPosY = currentPos[1] + x[1];

            if (absolutPosX >= 0) {
                gameFiled[absolutPosX][absolutPosY] = 1;
            }
        }

        stage.setNextShape((shapes.get(nextShape)).getCoords(), nextColor);
    }

    private void refreshShape() {
        for (int i = 0; i < CountShape; i++) {
            PermittedShape.add(i);
        }
    }

    public TetrisModel(GameStageI stage) {
        log.info("Инициализация модели TetrisModel");
        score = 0;
        shapes = new HashMap<>();
        GetShapes();
        refreshShape();
        this.stage = stage;
        NewShape();
        stage.getChange(copyGameFiled(), currentColor);
    }

    public void shiftLeft() {
        int[][] coords = shapeNow.getRevertCoords();
        int absolutPosX;
        int absolutPosy;

        for (int[] x : coords) {
            absolutPosX = currentPos[0] + x[0];
            absolutPosy = currentPos[1] + x[1];
            if (absolutPosy == 0 || absolutPosX < 0 || gameFiled[absolutPosX][absolutPosy - 1] > 1) {
                return;
            }
        }
        log.info("Сдвиг фигуры влево");
        for (int[] x : coords) {
            absolutPosX = currentPos[0] + x[0];
            absolutPosy = currentPos[1] + x[1];
            gameFiled[absolutPosX][absolutPosy] = -1;
            gameFiled[absolutPosX][absolutPosy - 1] = 1;
        }
        currentPos[1]--;
        stage.getChange(gameFiled, currentColor);
    }

    public void shiftRight() {
        int[][] coords = shapeNow.getCoords();
        int absolutPosX;
        int absolutPosY;

        for (int[] x : coords) {
            absolutPosX = currentPos[0] + x[0];
            absolutPosY = currentPos[1] + x[1];
            if (absolutPosY >= countCol - 1 || absolutPosX < 0 || gameFiled[absolutPosX][absolutPosY + 1] > 1) {
                return;
            }
        }
        log.info("Сдвиг фигуры вправо");
        for (int[] x : coords) {
            absolutPosX = currentPos[0] + x[0];
            absolutPosY = currentPos[1] + x[1];

            gameFiled[absolutPosX][absolutPosY] = -1;
            gameFiled[absolutPosX][absolutPosY + 1] = 1;
        }
        currentPos[1]++;
        stage.getChange(gameFiled, currentColor);
    }

    private Boolean checkRotate(int x, int y) {
        if (currentPos[0] + x >= countRows || currentPos[0] + x < 0) {
            return true;
        }
        if (currentPos[1] + y >= countCol || currentPos[1] + y < 0) {
            return true;
        }
        if (gameFiled[currentPos[0] + x][currentPos[1] + y] > 1) {
            return true;
        }
        return false;
    }

    private void rotateOnField(int[][] oldCoords, int[][] newCoords) {
        int absolutPosX;
        int absolutPosY;

        for (int[] x : oldCoords) {
            absolutPosX = currentPos[0] + x[0];
            absolutPosY = currentPos[1] + x[1];
            gameFiled[absolutPosX][absolutPosY] = -1;
        }
        for (int[] x : newCoords) {
            absolutPosX = currentPos[0] + x[0];
            absolutPosY = currentPos[1] + x[1];
            gameFiled[absolutPosX][absolutPosY] = 1;
        }
    }

    public void rotateUp() {
        if (shapeNow.getClass() == Square.class) {
            log.info("Куб не переворачивается");
            return;
        }
        int[][] coords = shapeNow.getCoords();
        int[][] newCoords = new int[4][2];
        for (int i = 0; i < 4; i++) {
            if (checkRotate(-coords[i][1], coords[i][0])) {
                log.info("Поворот вверх невозможен: препятствие или граница");
                return;
            }
            newCoords[i][0] = -coords[i][1];
            newCoords[i][1] = coords[i][0];
        }
        log.info("Выполнен поворот фигуры (вверх/против часовой)");

        Arrays.sort(newCoords, (a, b) -> {
            if (a[0] != b[0]) {
                return Integer.compare(a[0], b[0]);
            } else {
                return Integer.compare(b[1], a[1]);
            }
        });

        rotateOnField(coords, newCoords);
        shapeNow.changeCoords(newCoords);
        stage.getChange(gameFiled, currentColor);
    }


    private Boolean checkRow(int[] array, int value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] < value) {
                return false;
            }
        }
        return true;
    }

    private void removeRow(int row) {
        log.info("Удаление полной строки: {}", row);
        Boolean allZero = true;
        for (int j = 0; j < countCol; j++) {
            gameFiled[row][j] = -1;
        }
        for (int i = row - 1; i > -1; i--) {
            for (int j = 0; j < countCol; j++) {
                if (gameFiled[i][j] > 1) {
                    gameFiled[i + 1][j] = gameFiled[i][j];
                    gameFiled[i][j] = -1;
                    allZero = false;
                }
            }
            if (allZero) {
                return;
            }
            allZero = true;
        }
    }

    private void shapeFell() {
        log.info("Фигура зафиксирована на поле");
        for (int[] x : shapeNow.getCoords()) {
            int r = currentPos[0] + x[0];
            if (r < 0) {
                log.info("Игра окончена: фигура зафиксирована выше верхней границы");
                stage.GameOver(score);
                return;
            }
        }

        Set<Integer> rowsCheck = new HashSet<>();
        for (int[] x : shapeNow.getCoords()) {
            int absolutePosX = currentPos[0] + x[0];
            if (absolutePosX >= 0) {
                gameFiled[absolutePosX][x[1] + currentPos[1]] = currentColor + 2;
                rowsCheck.add(absolutePosX);
            }
        }
        int deletedRows = 0;
        int localScore = 0;
        for (int row : rowsCheck) {
            if (checkRow(gameFiled[row], 2)) {
                removeRow(row);
                localScore += 100;
                deletedRows++;
            }
        }
        if (deletedRows > 0) {
            score += localScore * deletedRows;
            log.info("Удалено рядов: {}. Начислено очков: {}. Текущий счет: {}", deletedRows, localScore * deletedRows, score);
            stage.changeScore(score);
        }
    }

    public void hardDrop() {
        log.info("Выполнен Hard Drop");
        IFigure currentShape = shapeNow;
        while (shapeNow == currentShape) {
            shapeDown();
        }
    }

    public void shapeDown() {
        int[][] DownCoords = shapeNow.getRevertCoords();

        if (currentPos[0] + DownCoords[0][0] < countRows - 1) {
            for (int[] x : DownCoords) {
                int absolutPosX = currentPos[0] + x[0];
                int absolutPosY = currentPos[1] + x[1];

                if (absolutPosX >= 0 && gameFiled[absolutPosX + 1][absolutPosY] > 1) {
                    shapeFell();
                    NewShape();
                    stage.getChange(gameFiled, currentColor);
                    return;
                }
            }
            for (int[] x : DownCoords) {
                int absolutPosX = currentPos[0] + x[0];
                int absolutPosY= currentPos[1] + x[1];
                if (absolutPosX >= 0) {
                    gameFiled[absolutPosX][absolutPosY] = -1;
                    gameFiled[absolutPosX + 1][absolutPosY] = 1;
                } else if (absolutPosX + 1 >= 0) {
                    gameFiled[absolutPosX + 1][absolutPosY] = 1;
                }
            }
            currentPos[0]++;
        } else {
            log.info("Фигура достигла дна поля");
            shapeFell();
            NewShape();
        }
        stage.getChange(gameFiled, currentColor);
    }
}