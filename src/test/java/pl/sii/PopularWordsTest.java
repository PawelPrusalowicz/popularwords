package pl.sii;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class PopularWordsTest {
    private static final PopularWords testee = new PopularWords();
    static final String ADAMKILGARRIFF_SOLUTION_PATH = "src/test/resources/all.num";

    @Test
    public void shouldReturnOneThousandMostPopularWords() {

        //given
        Map<String, Long> wordsFrequencyListCreatedByAdamKilgarriff = getWordsFrequencyListCreatedByAdamKilgarriff();

        //when
        Map<String, Long> result = testee.findOneThousandMostPopularWords();

        //then
        assertFalse(result.isEmpty());
        assertEquals(1000, result.size());
        compareWordListsFrequency(wordsFrequencyListCreatedByAdamKilgarriff, result);

    }

    private void compareWordListsFrequency(Map<String, Long> wordsFrequencyListCreatedByAdamKilgarriff, Map<String, Long> result) {
        long totalFrequencyByKilgarriff = wordsFrequencyListCreatedByAdamKilgarriff.values().stream().reduce(0L, Long::sum);
        long totalFrequencyInAResult = result.values().stream().reduce(0L, Long::sum);
        System.out.println("totalFrequencyByKilgarriff = " + totalFrequencyByKilgarriff);
        System.out.println("totalFrequencyInAResult = " + totalFrequencyInAResult);


        result.forEach((key, value) -> {

        	BigDecimal kilgarriffUsagePercentage = null;
        	if (wordsFrequencyListCreatedByAdamKilgarriff.containsKey(key)) {
                kilgarriffUsagePercentage = calculatePercentage(wordsFrequencyListCreatedByAdamKilgarriff.get(key), totalFrequencyByKilgarriff);
            } else {
        	    //in case the word appears in result, but not in wordsFrequencyListCreatedByAdamKilgarriff, it's given a value of 0
                kilgarriffUsagePercentage = BigDecimal.ZERO;
            }

                BigDecimal valueUsagePercentage = calculatePercentage(value, totalFrequencyInAResult);
        		BigDecimal diff = kilgarriffUsagePercentage.subtract(valueUsagePercentage);
        		System.out.println(key + "," + valueUsagePercentage + "%," + kilgarriffUsagePercentage + "%," + (new BigDecimal(0.5).compareTo(diff.abs()) > 0) + " " + diff);

        });
    }

    private BigDecimal calculatePercentage(double obtained, double total) {
        return BigDecimal.valueOf(obtained * 100 / total).setScale(4, RoundingMode.HALF_UP);
    }

    private Map<String, Long> getWordsFrequencyListCreatedByAdamKilgarriff() {
    	
    	Map<String, Long> AdamKilgarriffResult = new HashMap <String, Long>();
    	
    	try (BufferedReader bufferedReader = new BufferedReader(new FileReader(ADAMKILGARRIFF_SOLUTION_PATH))) {

            String line = null;

            //first line of the file contains only information about the file
            bufferedReader.readLine();

            while ((line = bufferedReader.readLine()) != null) {
            	            	
            	StringTokenizer st = new StringTokenizer(line, " \t");

            	//first token represents the number of appearances of the word in data
            	Long value = Long.valueOf(st.nextToken());

            	//second token represents the given word
            	String key = st.nextToken();

            	//in case of repetitions in the file
            	if (AdamKilgarriffResult.containsKey(key)) {
            		
            		Long valueBefore = AdamKilgarriffResult.get(key);
                    AdamKilgarriffResult.put(key, valueBefore + value);
            		
            	} else {
                    AdamKilgarriffResult.put(key, value);
            	}

            }

        } catch (IOException e) {

            e.printStackTrace();
        }

    	
    	return AdamKilgarriffResult;
    }


}
