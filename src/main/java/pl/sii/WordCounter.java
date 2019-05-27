package pl.sii;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 *  Class responsible for counting word occurrences in given text.
 * **/

 public class WordCounter {

    /**
     *  The method loads a phrase from the data file and looks for it (binary search) in the dictionary (3esl.txt)
     *  However, some phrases in dictionary consist of multiple words. As the number of them is much smaller than number of
     *  phrases that consist of one word only, it would be inefficient to analyse the following words together with current one (join them)
     *  and binary search the result in every case.
     *
     *  Consequently, all the words were subdivided in two categories:
     *  words, that may start a phrase consisting of many words and words that can become single word phrases only.
     *  Only if the current word belongs to the first group, the consecutive words in data file are taken into consideration.
     *  This leads to speeding up the search process.
     *  In this case new possible dictionary phrases are created (by adding next string in file to previous one) and binary searched in dictionary.
     *
     *  EXAMPLES:
     *  1) word "social"
     *  the word may begin some dictionary phrases like "social studies"
     *
     *  so whenever a word "social" occurs in text, the following words in text in combination with "social" are checked in dictionary to find phrases.
     *  if longer phrases are not found, word "social" is added to map
     *
     *  2) word "son"
     *
     *  no dictionary phrase starts with "son", so the word will be binary searched in the dictionary directly
     *
     *
     */
    static HashMap<String, Long> countWordsInFile (ArrayList<String> dictionary, String filePath) {


        // The Map stores all the loaded words, that were found in dictionary and the number of their occurrences in data.
        HashMap<String, Long> allFoundWords = new HashMap<>();


        // The Map stores the words, that may start a phrase consisting of many words and maximum number of words in such phrase for a word.
        HashMap<String, Integer> wordsThatStartPhrases = findWordsThatStartPhrases (dictionary);


        TextReader textReader = new TextReader(filePath);

        String line = null;
        while ((line = textReader.nextLine() ) != null) {

            String[] words = line.split(" ");

            for (int wordIndex = 0; wordIndex < words.length; wordIndex++ ){

                //avoiding empty data strings
                if (words[wordIndex].isEmpty()) {
                    continue;
                }

                //checking if the current word may begin a dictionary phrase consisting of multiple words
                if (wordsThatStartPhrases.get(words[wordIndex]) != null) {

                    //possible phrases are generated
                    ArrayList<String> possibleDictionarySlogans = generatePossibleSlogans (words, wordIndex, wordsThatStartPhrases.get(words[wordIndex]));

                    //longer phrases should be added to the dictionary rather than short ones
                    //long phrases are searched first, then shorter and shorted ones if the longer don't match
                    for (String wordSequence : possibleDictionarySlogans) {

                        if (wordSequence != null) {
                            if (Collections.binarySearch(dictionary, wordSequence) >= 0) {

                                addWordToMap(allFoundWords, wordSequence);
                                break;
                            }
                        }
                    }

                }

                //if the word may start single word phrases only, it is binary searched directly
                else {

                    if (Collections.binarySearch(dictionary, words[wordIndex]) >= 0) {

                        addWordToMap(allFoundWords, words[wordIndex]);
                    }

                }

            }

        }

        return allFoundWords;

    }

    /**
     *  Method adds a new word with value (number of appearances in text) = 1 or changes the map value, when the word(key) is already present in the map.
     **/
    static void addWordToMap (HashMap<String, Long> map, String word){

        Long count = map.get(word);
        if (count == null) {
            map.put(word, Long.valueOf(1));
        } else {
            map.put(word, count + 1);
        }
    }

    /**
     * The method generates possible phrases - strings, merging the current word with the following ones.
     * The number of possible phrases is the maximal number of words that may be in the phrase, that the given word begins.
     *
     **/
    static ArrayList<String> generatePossibleSlogans (String[] words, int wordIndex, int maximalNumberOfWords ) {

        ArrayList<String> phrases = new ArrayList<String>();

        for (int numberOfWordsInDictionaryInput = maximalNumberOfWords; numberOfWordsInDictionaryInput > 0; numberOfWordsInDictionaryInput--) {

            String wordSequence = null;

            //creates a phrase out of numberOfWordsInDictionarySlogan consecutive words
            for (int tempWordIndex = wordIndex; tempWordIndex < numberOfWordsInDictionaryInput + wordIndex; tempWordIndex++) {

                //in case the end of data array is reached
                if (tempWordIndex >= words.length) {
                    break;
                }

                if (tempWordIndex == wordIndex) {
                    wordSequence = words[wordIndex];

                    // creating phrases longer than one word
                } else {
                    wordSequence += " " + words[tempWordIndex];
                }
            }

            phrases.add(wordSequence);
        }

        return phrases;
    }

    /**
     *  Method finds words, that may be the first word in phrases consisting of multiple words.
     *  The map value is the maximal number of words in phrase, that the given word begins.
     *  Example: for a word "track", the value number becomes 3, because longest phrase that starts with
     *  the word "track" is "track and field"
     **/
    static HashMap<String, Integer> findWordsThatStartPhrases (ArrayList<String> dictionary) {

        HashMap<String, Integer> wordsThatStartWordSequenceInDictionary = new HashMap<>();

        for (String word : dictionary) {

            if (word.contains(" ")) {
                String[] words = word.split(" ");

                Integer maximumNumberOfWordsInSequence = wordsThatStartWordSequenceInDictionary.get(words[0]);

                if (maximumNumberOfWordsInSequence == null || words.length > maximumNumberOfWordsInSequence){
                    wordsThatStartWordSequenceInDictionary.put(words[0], words.length);
                }
            }
        }

        return wordsThatStartWordSequenceInDictionary;
    }
}
