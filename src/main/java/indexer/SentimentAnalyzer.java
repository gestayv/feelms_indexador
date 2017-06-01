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

    private Map<String, Double> dictionary; //Para palabras enteras
    private Map<String, Double> suffixDictionary; //Aquellas que estan guardadas solo como sufijo

    private SentimentAnalyzer() {

    }

    public SentimentAnalyzer(String filePath) throws IOException {

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {

            List<String> lines = new ArrayList<String>();
            String line;
            while((line = bufferedReader.readLine()) != null) {
                lines.add(line.trim());
            }

            int size = lines.size();
            Double cap = (size / 0.75) + 1;
            dictionary = new HashMap<String, Double>(cap.intValue());

            for (String auxLine: lines) {
                String[] lineSplit = auxLine.split(",");
            }

        } catch (FileNotFoundException e) {
            System.out.print(e.getMessage());
            throw e;
        }
    }

}
