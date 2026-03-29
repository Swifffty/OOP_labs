import csv.WriterCSV;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import csv.Words;
import csv.ReaderFile;

import static org.junit.jupiter.api.Assertions.*;

class TestCsvParser {

    @Test
    void TestReaderFile() {
        String NameFile = "/TestTxt";
        try (ReaderFile TestFile = new ReaderFile(NameFile)) {
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



    @Test
    void EdgeTests() {

        assertThrows(NullPointerException.class, () -> {
           ReaderFile NonExist = new ReaderFile("/NonExistFile");
        });
        PrintStream standard = System.err;
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outputStreamCaptor));
        try (ReaderFile EmptyFile = new ReaderFile("/EmptyFile.txt")){
            String outErr = outputStreamCaptor.toString();
            assertTrue(outErr.contains("Передан пустой файл!"));
            System.setErr(standard);
        } catch (IOException e) {
            System.setErr(standard);
            System.err.println(e.getMessage());
        }

    }

}
