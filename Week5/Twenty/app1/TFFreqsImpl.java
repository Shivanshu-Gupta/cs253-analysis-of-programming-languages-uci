package Week5.Twenty.app1;

import Week5.Twenty.framework.TFFreqs;
//import Week5.Twenty.framework.Twenty.WordFrequencyPair;

import java.util.HashMap;
import java.util.List;

public class TFFreqsImpl implements TFFreqs {
    public HashMap<String, Integer> top25(List<String> words) {
        HashMap<String, Integer> wordFreqs = new HashMap<>();
        for(String word: words) {
            wordFreqs.put(word, wordFreqs.getOrDefault(word, 0) + 1);
        }
        return wordFreqs;
    }
}
