package pl.sii;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


/**
 * Class responsible for loading text from files. It contains a buffered reader and lets load the data line by line.
 */
public class TextReader {

    BufferedReader bufferedReader;

    TextReader(String filePath) {

        try {
            bufferedReader = new BufferedReader(new FileReader(new File(filePath)));

        } catch( IOException e) {
            System.out.println("Failed to read the file!");
        }

    }

    /**
     * Reads the next line of the file and clears it.
     */
    String nextLine() {

        String line = null;
        try {
            line = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return clearData(line);
    }


    /**
     *  The method clears the data from signs that are not present in 3esl.txt file
     **/
    String clearData (String input) {

        if (input == null){
            return input;
        }

        String[] signs = { ",", ";","!","?","#","^","*","+","|","@","<",">","(",")","[","]","/","\"","“","”" };

        for (int i = 0; i < signs.length; i++)
        {
            input = input.replace(signs[i], "");
        }

        return input;
    }


    /**
     *  The method loads every line of the file and adds it to an Arraylist.
     **/
    static ArrayList<String> loadTextAsListOfLines(String filePath) {

        ArrayList<String> dictionary = new ArrayList<>();

        try ( BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(filePath)))) {

            String line = null;

            while ((line = bufferedReader.readLine()) != null) {

                dictionary.add(line);
            }

        } catch ( IOException e ) {
            System.out.println("Failed to read the file!");
        }

        //dictionary must be sorted for binary search
        Collections.sort(dictionary);

        return dictionary;
    }
}
