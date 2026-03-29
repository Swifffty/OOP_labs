package csv;
import java.io.IOException;

import java.util.ArrayList;

public class Main {
    public static void main(String args[]) {
        if (args.length == 0) {
            System.err.println("Не передан путь к файлу");
            return;
        }
        try (ReaderFile Reader = new ReaderFile(args[0])){
            ArrayList<Words> ListWords = Reader.getSortedList();
            WriterCSV CsvFile = new WriterCSV();
            CsvFile.WriteInCsv(ListWords);
            } catch (IOException | NullPointerException e) {
            System.err.println(e.getMessage());
        }
    }
}