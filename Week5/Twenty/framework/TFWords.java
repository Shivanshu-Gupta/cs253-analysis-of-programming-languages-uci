package Week5.Twenty.framework;

import java.io.FileNotFoundException;
import java.util.List;

public interface TFWords {
    public List<String> extractWords(String path) throws FileNotFoundException;
}

