package pl.sii;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PopularWords {

    static final String DICTIONARY_FILE_PATH = "src/main/resources/3esl.txt";
    static final String DATA_SET_PATH = "src/main/resources/eng_news_2016_1M-sentences.txt";
    static final Integer NUMBER_OF_POPULAR_WORDS = 1000;

    public static void main(String[] args) {
        PopularWords popularWords = new PopularWords();

        Map<String, Long> result = popularWords.findOneThousandMostPopularWords();

    }

    /**
     *  The method loads dictionary and data from the data file
     *  and creates a map of 1000 most popular occurences of dictionary phrases in data.
     **/
    public Map<String, Long> findOneThousandMostPopularWords() {

        final ArrayList<String> dictionary = TextReader.loadTextAsListOfLines(DICTIONARY_FILE_PATH);
        HashMap<String, Long> allFoundWords = WordCounter.countWordsInFile(dictionary,DATA_SET_PATH);
        HashMap<String, Long> thousandMostPopularWords = getMostPopularEntries(allFoundWords, NUMBER_OF_POPULAR_WORDS);

        return thousandMostPopularWords;
    }


    /**
     *   The method reduces the map size to the desired number of most popular words.
     **/
    HashMap<String, Long>  getMostPopularEntries (HashMap<String, Long> map, int numberOfEntries){

        //sorting the map by number of appearances of the word
        LinkedHashMap<String, Long> sortedMap = map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));


        int count = 0;
        int max = numberOfEntries;

        //getting the expected number of top entries
        //using the LinkedHashMap ability to maintain the order in the map in the iteration
        HashMap<String, Long> result = new HashMap<String, Long>();
        for (Map.Entry<String, Long> entry : sortedMap.entrySet()) {
            if (count >= max) break;

            result.put(entry.getKey(), entry.getValue());
            count++;
        }

        return result;

    }

    
}
