package Week5;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.Scanner;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class Seventeen {
    /*
     * The main function
     */
    public static void main(String[] args) throws IOException {
        new WordFrequencyController(args[0]).run();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the name of a class: ");
        try {
            Class<?> c = Class.forName(String.format("Week5.%s", scanner.nextLine()));
            // prints out all of the class's fields (their names and types),
            // all of its method names, and all of its superclasses and
            // implemented interfaces
            System.out.println();
            System.out.println("Fields and Types:");
            for(Field f: c.getDeclaredFields()) {
                System.out.printf("Name: %s, Type: %s%n", f.getName(), f.getGenericType());
            }

            System.out.println();
            System.out.println("Methods:");
            for(Method m: c.getDeclaredMethods()) {
                System.out.println(m.toGenericString());
            }

            System.out.println();
            System.out.printf("Superclasses: %s%n", c.getGenericSuperclass().getTypeName());

            System.out.println();
            System.out.println("Implemented Interfaces:");
            for(Type t: c.getGenericInterfaces()) {
                System.out.println(t.getTypeName());
            }

        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
    }
}

/*
 * The classes
 */

abstract class TFExercise {
    public String getInfo() {
        return this.getClass().getName();
    }
}

class WordFrequencyController extends TFExercise {
    private DataStorageManager storageManager;
    private StopWordManager stopWordManager;
    private WordFrequencyManager wordFreqManager;

    public WordFrequencyController(String pathToFile) throws IOException {
        this.storageManager = new DataStorageManager(pathToFile);
        this.stopWordManager = new StopWordManager();
        this.wordFreqManager = new WordFrequencyManager();
    }

    public void run() {
        Class<?> storageManagerClass = this.storageManager.getClass();
        Class<?> stopWordManagerClass = this.stopWordManager.getClass();
        Class<?> wordFreqManagerClass = this.wordFreqManager.getClass();
        try {
            for (String word : (List<String>)storageManagerClass.getMethod("getWords").
                    invoke(this.storageManager)) {
                if (!(Boolean) stopWordManagerClass.getDeclaredMethod("isStopWord", String.class).
                        invoke(this.stopWordManager, word)) {
                    wordFreqManagerClass.getDeclaredMethod("incrementCount", String.class).
                            invoke(this.wordFreqManager, word);
                }
            }
            int numWordsPrinted = 0;
            for (WordFrequencyPair pair : (List<WordFrequencyPair>) wordFreqManagerClass.getMethod("sorted").
                    invoke(this.wordFreqManager)) {
                System.out.println(pair.getWord() + " - " + pair.getFrequency());

                numWordsPrinted++;
                if (numWordsPrinted >= 25) {
                    break;
                }
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}

/** Models the contents of the file. */
class DataStorageManager extends TFExercise {
    private List<String> words;

    public DataStorageManager(String pathToFile) throws IOException {
        this.words = new ArrayList<String>();

        Scanner f = new Scanner(new File(pathToFile), "UTF-8");
        try {
            f.useDelimiter("[\\W_]+");
            while (f.hasNext()) {
                this.words.add(f.next().toLowerCase());
            }
        } finally {
            f.close();
        }
    }

    public List<String> getWords() {
        return this.words;
    }

    public String getInfo() {
        return super.getInfo() + ": My major data structure is a " + this.words.getClass().getName();
    }
}

/** Models the stop word filter. */
class StopWordManager extends TFExercise {
    private Set<String> stopWords;

    public StopWordManager() throws IOException {
        this.stopWords = new HashSet<String>();

        Scanner f = new Scanner(new File("../stop_words.txt"), "UTF-8");
        try {
            f.useDelimiter(",");
            while (f.hasNext()) {
                this.stopWords.add(f.next());
            }
        } finally {
            f.close();
        }

        // Add single-letter words
        for (char c = 'a'; c <= 'z'; c++) {
            this.stopWords.add("" + c);
        }
    }

    public boolean isStopWord(String word) {
        return this.stopWords.contains(word);
    }

    public String getInfo() {
        return super.getInfo() + ": My major data structure is a " + this.stopWords.getClass().getName();
    }
}

/** Keeps the word frequency data. */
class WordFrequencyManager extends TFExercise {
    private Map<String, MutableInteger> wordFreqs;

    public WordFrequencyManager() {
        this.wordFreqs = new HashMap<String, MutableInteger>();
    }

    public void incrementCount(String word) {
        MutableInteger count = this.wordFreqs.get(word);
        if (count == null) {
            this.wordFreqs.put(word, new MutableInteger(1));
        } else {
            count.setValue(count.getValue() + 1);
        }
    }

    public List<WordFrequencyPair> sorted() {
        List<WordFrequencyPair> pairs = new ArrayList<WordFrequencyPair>();
        for (Map.Entry<String, MutableInteger> entry : wordFreqs.entrySet()) {
            pairs.add(new WordFrequencyPair(entry.getKey(), entry.getValue().getValue()));
        }
        Collections.sort(pairs);
        Collections.reverse(pairs);
        return pairs;
    }

    public String getInfo() {
        return super.getInfo() + ": My major data structure is a " + this.wordFreqs.getClass().getName();
    }
}

class MutableInteger {
    private int value;

    public MutableInteger(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

class WordFrequencyPair implements Comparable<WordFrequencyPair> {
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