package Week5.Twenty.app2;

import Week5.Twenty.framework.TFWords;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TFWordsImpl implements TFWords {
    Set<String> getStopWords() throws FileNotFoundException {
        Set<String> stopWords = new HashSet<String>();;
        Scanner f = new Scanner(new File("../stop_words.txt"), "UTF-8");
        try {
            f.useDelimiter(",");
            while (f.hasNext()) {
                stopWords.add(f.next());
            }
        } finally {
            f.close();
        }
        return stopWords;
    }

    public List<String> extractWords(String pathToFile) throws FileNotFoundException {
        Set<String> stopWords = getStopWords();
        List<String> words = new ArrayList<>();
        Scanner f = new Scanner(new File(pathToFile), "UTF-8");
        Pattern wordPattern = Pattern.compile("[a-z]{2,}");
        while (f.hasNextLine()) {
            String line = f.nextLine().toLowerCase();
            Matcher matcher = wordPattern.matcher(line);
            while(matcher.find()) {
                String word = matcher.group();
                if(!stopWords.contains(word)) {
                    words.add(word);
                }
            }
        }
        f.close();
        return words;
    }
}
