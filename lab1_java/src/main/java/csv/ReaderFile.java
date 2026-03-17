package csv;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.lang.StringBuilder;

public class ReaderFile implements AutoCloseable {
    private Map<String, Integer> MapWords;
    private BufferedReader TxtFile;
    private int count;


    public ReaderFile(String NameFile) throws  NullPointerException, IOException {
        MapWords = new HashMap<>();
        InputStream resourcesTxt = getClass().getResourceAsStream(NameFile);
        TxtFile = new BufferedReader(new InputStreamReader(resourcesTxt));
        String line = TxtFile.readLine();
        if (line == null) {
            System.err.println("Передан пустой файл!");
        }
        while (line != null) {
            add_words(line);
            line = TxtFile.readLine();
        }
    }

    private void add_words(String line) {
        StringBuilder sb = new StringBuilder();
        for (Character i : line.toCharArray()) {
            if (Character.isLetterOrDigit(i)) {
                sb.append(i);
            } else if (!sb.isEmpty()) {
                String word = sb.toString();
                String lower = word.toLowerCase();
                MapWords.merge(lower, 1, (oldValue, newValue) -> oldValue + newValue);
                count++;
                sb.setLength(0);
            }
        }
        if (!sb.isEmpty()) {
            String word = sb.toString();
            String lower = word.toLowerCase();
            MapWords.merge(lower, 1, (oldValue, newValue) -> oldValue + newValue);
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

