package Week5.Twenty.app1;

import Week5.Twenty.framework.TFWords;

import java.io.*;
import java.util.*;

public class TFWordsImpl implements TFWords {
    Set<String> getStopWords() throws FileNotFoundException {
        Set<String> stopWords = new HashSet<String>();;
        Scanner f = new Scanner(new File("../stop_words.txt"), "UTF-8");
        f.useDelimiter(",");
        while (f.hasNext()) stopWords.add(f.next());
        f.close();

        return stopWords;
    }

    public List<String> extractWords(String pathToFile) throws FileNotFoundException {
        Set<String> stopWords = getStopWords();
        List<String> words = new ArrayList<>();
        Scanner f = new Scanner(new File(pathToFile), "UTF-8");
        f.useDelimiter("[\\W_]+");
        while (f.hasNext()) {
            String word = f.next().toLowerCase();
            if(!stopWords.contains(word) && word.length() >= 2) {
                words.add(word);
            }
        }
        f.close();
        return words;
    }
}
