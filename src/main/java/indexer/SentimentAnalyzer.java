package indexer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by arturo on 01-06-17.
 */
public class SentimentAnalyzer {

    private Map<String, Integer> dictionary; //Para palabras enteras
    private Map<String, Integer> suffixDictionary; //Aquellas que estan guardadas solo como sufijo

    private SentimentAnalyzer() {

    }

    public SentimentAnalyzer(String filePath) throws IOException {

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {

            List<String> lines = new ArrayList<String>();
            List<String> suffixLines = new ArrayList<String>();

            String line;
            boolean first = true;
            while((line = bufferedReader.readLine()) != null) {
                if(!first) {
                    if(!line.contains("*")) {
                        lines.add(line.trim());
                    } else {
                        suffixLines.add(line.trim());
                    }
                } else {
                    first = false;
                }
            }

            int size = lines.size();
            int suffixSize = suffixLines.size();

            Double cap = (size / 0.75) + 1;
            Double suffixCap = (suffixSize / 0.75) + 1;

            dictionary = new HashMap<String, Integer>(cap.intValue());
            suffixDictionary = new HashMap<String, Integer>(cap.intValue());

            for (String auxLine: lines) {
                String[] lineSplit = auxLine.split(",");
                String word = lineSplit[0]; //NORMALIZAR
                int pos = Integer.valueOf(lineSplit[1]);
                int neg = Integer.valueOf(lineSplit[2]);
                int value = pos - neg;
                dictionary.putIfAbsent(word, value);
            }

            for (String auxLine: suffixLines) {
                String[] lineSplit = auxLine.split(",");
                String word = lineSplit[0].replace("*", ""); //NORMALIZAR
                int pos = Integer.valueOf(lineSplit[1]);
                int neg = Integer.valueOf(lineSplit[2]);
                int value = pos - neg;
                suffixDictionary.putIfAbsent(word, value);
            }

        } catch (FileNotFoundException e) {
            System.out.print(e.getMessage());
            throw e;
        }
    }

    public int analyzeTweet(String tweet) {
        String[] wordsAux = tweet.split(" ");

        List<String> words = new ArrayList<String>();

        for (String word: wordsAux) {

        }

        return 0;
    }

}
