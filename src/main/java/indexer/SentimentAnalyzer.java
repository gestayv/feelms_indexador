package indexer;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.es.SpanishAnalyzer;

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

    private CharArraySet stopwords;

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
            suffixDictionary = new HashMap<String, Integer>(suffixCap.intValue());

            for (String auxLine: lines) {
                String[] lineSplit = auxLine.split(",");
                String word = StringUtils.stripAccents(lineSplit[0].toLowerCase());
                int pos = Integer.valueOf(lineSplit[1]);
                int neg = Integer.valueOf(lineSplit[2]);
                int value = pos - neg;
                dictionary.putIfAbsent(word, value);
            }

            for (String auxLine: suffixLines) {
                String[] lineSplit = auxLine.split(",");
                String word = StringUtils.stripAccents(lineSplit[0].replace("*", "").toLowerCase());
                int pos = Integer.valueOf(lineSplit[1]);
                int neg = Integer.valueOf(lineSplit[2]);
                int value = pos - neg;
                suffixDictionary.putIfAbsent(word, value);
            }

            stopwords = SpanishAnalyzer.getDefaultStopSet();


        } catch (FileNotFoundException e) {
            System.out.print(e.getMessage());
            throw e;
        }
    }

    public int analyzeTweet(String tweet) {
        String[] wordsAux = tweet.split(" ");

        List<String> words = new ArrayList<String>();

        int n = 0; //Cantidad de palabras a contar

        int pos = 0;
        int neg = 0;
        int neutral = 0;

        for (String word: wordsAux) {
            String auxWord = StringUtils.stripAccents(word.toLowerCase());
            if(!stopwords.contains(auxWord)) {
                if(dictionary.containsKey(auxWord)) {
                    n++;
                    //Operacion si contiene
                    int val = dictionary.get(auxWord);
                    if(val > 0) {
                        pos++;
                    } else if (val < 0) {
                        neg++;
                    } else neutral++;

                } else {
                    //Busca en el dicionario de sufijos. Debe recortar la string para ver si lo compone alguno
                    boolean found = false;
                    String aux = auxWord;
                    while(aux.length() >= 2 && !found) {
                        if(suffixDictionary.containsKey(aux)) {
                            found = true;
                            n++;
                            int val = suffixDictionary.get(aux);
                            if(val > 0) {
                                pos++;
                            } else if (val < 0) {
                                neg++;
                            } else {
                                neutral++;
                            }
                        } else {
                            aux = StringUtils.chop(aux);
                        }
                    }
                }
            }
        }


        if(pos > 0 || neg > 0) {
            if(pos > 0 && neg == 0) {
                return 1;
            } else if (neg > 0 && pos == 0) {
                return - 1;
            }
            float ratio = (pos - neg)/(pos + neg);

            if(ratio >= 0.15) {
                return 1;
            } else if (ratio <= 0.15) {
                return -1;
            }
        }

        return 0;
    }

}
