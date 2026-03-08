package csv;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;



public class WriterCSV {
    public void WriteInCsv(ArrayList<Words> ListWords) {
        try (BufferedWriter csvFile = new BufferedWriter(new FileWriter("Words.csv"))){
            csvFile.write("Word;count_word;frequency\n");
            for (Words wordsCount : ListWords) {
                csvFile.write(wordsCount.word() + ";" + wordsCount.count() + ";" + wordsCount.frequency() + "\n");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
