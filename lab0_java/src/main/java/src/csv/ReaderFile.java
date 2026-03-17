package csv;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReaderFile implements AutoCloseable {
    private Map<String, Integer> MapWords;
    private BufferedReader TxtFile;
    private int count;


    public ReaderFile(String NameFile) throws IOException {
        MapWords = new HashMap<>();
        try {
            InputStream resourcesTxt = getClass().getResourceAsStream(NameFile);
            TxtFile = new BufferedReader(new InputStreamReader(resourcesTxt));
            String line = TxtFile.readLine();
            while (line != null) {
                add_words(line);
                line = TxtFile.readLine();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void add_words(String line) {
        String [] words = line.split("[^a-zA-Z0-9']+");
        for (String i : words) {
            String lower = i.toLowerCase();
            MapWords.merge(lower, 1, (oldValue, newValue) -> oldValue + newValue);
            count++;
        }
    }

    public ArrayList<Words> getSortedList() {
            ArrayList<Words> SortedWords = new ArrayList<>();

            for (Map.Entry<String, Integer> entry : MapWords.entrySet()) {
                double frequency = (count * 100.0) / entry.getValue();
                SortedWords.add(new Words(entry.getKey(), entry.getValue(), frequency));
            }

            SortedWords.sort((a, b) -> a.count() - b.count());
            return SortedWords;
        }

    @Override
    public void close() throws IOException {
        if (TxtFile != null) {
            TxtFile.close();
        }
    }
}

