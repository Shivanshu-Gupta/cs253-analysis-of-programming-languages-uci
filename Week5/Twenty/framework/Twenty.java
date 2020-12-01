package Week5.Twenty.framework;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

public class Twenty {
    public static void main(String[] args) throws ClassNotFoundException,
            NoSuchMethodException, IOException, IllegalAccessException,
            InvocationTargetException, InstantiationException, ParseException {

        JSONObject json = (JSONObject) new JSONParser().parse(new FileReader("config.json"));

        URL classURL = new URL((String) json.get("appPath"));
        URL[] classURLs = {classURL};
        URLClassLoader cloader = new URLClassLoader(classURLs);
        TFWords tfWords = (TFWords) cloader.loadClass("TFWordsImpl").newInstance();
        TFFreqs tfFreqs = (TFFreqs) cloader.loadClass("TFFreqsImpl").newInstance();
        HashMap<String, Integer> wordFreqs = tfFreqs.top25(tfWords.extractWords("../../../pride-and-prejudice.txt"));
        List<WordFrequencyPair> pairs = new ArrayList<WordFrequencyPair>();
        for (Map.Entry<String, Integer> entry : wordFreqs.entrySet()) {
            pairs.add(new WordFrequencyPair(entry.getKey(), entry.getValue()));
        }
        Collections.sort(pairs);
        Collections.reverse(pairs);

        int numWordsPrinted = 0;
        for (WordFrequencyPair pair : pairs) {
            System.out.println(pair.getWord() + " - " + pair.getFrequency());

            numWordsPrinted++;
            if (numWordsPrinted >= 25) {
                break;
            }
        }
    }

    public static class WordFrequencyPair implements Comparable<WordFrequencyPair> {
        private String word;
        private int frequency;

        public WordFrequencyPair(String word, int frequency) {
            this.word = word;
            this.frequency = frequency;
        }

        public String getWord() {
            return word;
        }

        public int getFrequency() {
            return frequency;
        }

        public int compareTo(WordFrequencyPair other) {
            return this.frequency - other.frequency;
        }
    }
}