package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.io.FileWriter;

public class ScoreTable {
    private static final Logger log = LoggerFactory.getLogger(ScoreTable.class);
    private static final String nameFile = "Scores.txt";
    private static TreeMap<Integer, String> scoreTable = new TreeMap<>(Collections.reverseOrder());

    public TreeMap<Integer, String> getScoreTable() {
        if (!scoreTable.isEmpty()) {
            return new TreeMap<>(scoreTable);
        }

        log.info("Загрузка таблицы рекордов из файла: {}", nameFile);
        try (BufferedReader file = new BufferedReader(new FileReader(nameFile))) {
            String line;
            while ((line = file.readLine()) != null) {
                String[] args = line.split(" ");
                scoreTable.put(Integer.parseInt(args[0]), args[1]);
            }
            log.info("Таблица рекордов успешно загружена. Найдено записей: {}", scoreTable.size());
            return new TreeMap<>(scoreTable);
        } catch (Exception e) {
            log.error("Критическая ошибка при чтении файла рекордов: {}", e.getMessage());
            System.err.println(e.getMessage());
            throw new RuntimeException("Ошибка чтения файла с результатами");
        }
    }

    public void newScore(int score, String name) {
        log.info("Проверка нового результата: {} (игрок: {})", score, name);
        if (scoreTable.isEmpty()) {
            getScoreTable();
            scoreTable.put(score, name);
            log.info("Результат добавлен (таблица была пуста)");
            return;
        }
        if (scoreTable.size() < 10) {
            scoreTable.put(score, name);
            log.info("Результат добавлен в топ-10 (свободное место)");
            return;
        }
        if (score > scoreTable.lastKey()) {
            log.info("Новый рекорд! Вытеснение результата: {} — {}", scoreTable.lastKey(), scoreTable.get(scoreTable.lastKey()));
            scoreTable.pollLastEntry();
            scoreTable.put(score, name);
        } else {
            log.info("Результат не попал в топ-10");
        }
    }

    public static void saveToFile() {
        log.info("Начало сохранения таблицы рекордов в файл {}", nameFile);
        try (PrintWriter writer = new PrintWriter(new FileWriter(nameFile))) {
            for (Map.Entry<Integer, String> x : scoreTable.entrySet()) {
                writer.println(x.getKey() + " " + x.getValue());
            }
            log.info("Таблица рекордов успешно сохранена. Всего записей: {}", scoreTable.size());
        } catch (Exception e) {
            log.error("Не удалось сохранить файл с результатами: {}", e.getMessage());
            System.err.println("Не найден файл с результатами");
        }
    }
}
