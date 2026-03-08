import csv.WriterCSV;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import csv.Words;
import csv.ReaderFile;

class TestCsvParser {

    @Test
    void TestReaderFile() {
        String NameFile = "/TestTxt";
        try (ReaderFile TestFile = new ReaderFile(NameFile)){
            ArrayList<Words> TestList = TestFile.getSortedList();
            WriterCSV csv = new WriterCSV();
            csv.WriteInCsv(TestList);
            Words TestWords = TestList.getFirst();
            assertEquals("im", TestWords.word());
            assertEquals(1, TestWords.count());
            TestWords = TestList.get(5);
            assertEquals("pf", TestWords.word());
            assertEquals(3, TestWords.count());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
