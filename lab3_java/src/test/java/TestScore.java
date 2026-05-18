
import model.ScoreTable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class ScoreTableTest {
    private ScoreTable scoreTable;
    private final File originalFile = new File("Scores.txt");
    private final File backupFile = new File("Scores.txt.bak");

    @BeforeEach
    void setUp() throws IOException {
        scoreTable = new ScoreTable();
        scoreTable.clearTable();

        if (originalFile.exists()) {
            Files.move(originalFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        originalFile.createNewFile();
    }

    @AfterEach
    void tearDown() throws IOException {
        if (originalFile.exists()) {
            originalFile.delete();
        }

        if (backupFile.exists()) {
            Files.move(backupFile.toPath(), originalFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    @Test
    void testAddNewScore() {
        scoreTable.newScore(100, "Player1");
        TreeMap<Integer, String> table = scoreTable.getScoreTable();

        assertEquals(1, table.size(), "В пустой таблице должна быть только 1 запись");
        assertEquals("Player1", table.get(100));
    }

    @Test
    void testScoreSorting() {
        scoreTable.newScore(50, "C");
        scoreTable.newScore(150, "A");
        scoreTable.newScore(100, "B");

        TreeMap<Integer, String> table = scoreTable.getScoreTable();

        assertEquals(150, table.firstKey());
        assertEquals(50, table.lastKey());
    }

    @Test
    void testTopTenLimit() {
        for (int i = 1; i <= 10; i++) {
            scoreTable.newScore(i * 10, "P" + i);
        }

        scoreTable.newScore(5, "Looser");
        assertEquals(10, scoreTable.getScoreTable().size());

        scoreTable.newScore(1000, "Pro");
        assertTrue(scoreTable.getScoreTable().containsKey(1000));
        assertFalse(scoreTable.getScoreTable().containsKey(10));
    }
}