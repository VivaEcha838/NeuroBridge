/* 
    Vivaan Echambadi
    1/22/2026

    MessageBuilder() class will represent a message builder that constructs messages from selected phrases. 
    It will provide methods to add phrases, show the current message, clear the message, and load a phrase-meaning dictionary 
    from a file to convert phrases into their meanings when constructing the message. 

*/

// importing package
package business;

import java.util.*;
import java.io.*;
import presentation.MainApp;

// MessageBuilder class definition
public class MessageBuilder {

    // private attributes for current message and selected phrases
    private String currentMessage;
    private List<String> selectedPhrases;

    // constant for phrases file path
    private static final String PHRASE_PATH = "resources/phrases.txt";

    // constructor to initialize message builder
    public MessageBuilder() {
        // initializing attributes
        this.currentMessage = "";
        this.selectedPhrases = new ArrayList<>();
    }

    // addButtonPhrase() method to add a phrase to the selected phrases list
    public void addButtonPhrase(String phrase) {
        this.selectedPhrases.add(phrase);
    }

    // loadPhraseDict() method to load the phrase-meaning dictionary from a file
    public static void loadPhraseDict() {
        // reading the phrases file and populating the phrase-meaning dictionary map
        try (BufferedReader reader = new BufferedReader(new FileReader(PHRASE_PATH))) {

            // reading each line from the file
            String line;
            while ((line = reader.readLine()) != null) {
                // splitting the line into phrase and meaning and adding to the dictionary map
                String[] partsOfLine = line.split(":", 2);
                MainApp.phraseMeaningDictMap.put(partsOfLine[0].trim(), partsOfLine[1].trim());
            }
        } 
        // handling exceptions during file reading
        catch (Exception e) {
            // printing the exception message
            System.out.println(e);
        }
    }
    
    // showCurrentMessage() method to display the current message constructed from selected phrases
    public String showCurrentMessage() {
        // constructing the current message from selected phrases
        currentMessage = "";

        // iterating through selected phrases to build the message
        for (int i = 0; i <this.selectedPhrases.size();i++) {
            // checking if the phrase exists in the phrase-meaning dictionary
             if (MainApp.phraseMeaningDictMap.containsKey(this.selectedPhrases.get(i))) {
                // appending the meaning of the phrase to the current message
                this.currentMessage = this.currentMessage + MainApp.phraseMeaningDictMap.get(this.selectedPhrases.get(i)) + "\n";
            }
        }  

        // clearing selected phrases after showing the message
        this.selectedPhrases.clear(); 
        return this.currentMessage;
    }

    // clearCurrentMessage() method to clear the current message
    public void clearCurrentMessage() {
        // resetting current message
         this.currentMessage = "";
    }
}
