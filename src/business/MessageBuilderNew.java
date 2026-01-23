/* 
    Vivaan Echambadi
    1/22/2026

    MessageBuilderNew() class will represent a message builder that constructs messages from selected phrases. 
    It will provide methods to add phrases, show the current message, clear the message, and update the message 
    based on selected phrases. It will also manage the username associated with the message.
    Moreover, it will utilize a phrase-meaning dictionary to convert phrases into their meanings when constructing the message.

*/

// importing package
package business;

import java.util.*;
import java.io.*;
import presentation.MainApp;

// MessageBuilderNew class definition
public class MessageBuilderNew {
    // private attributes for current message, selected phrases, and username
    private String currentMessage;
    private List<String> selectedPhrases;
    private String userName;

    // constant for phrases file path
    private static final String PHRASES_FILE = "resources/phrases.txt";

    // constructor to initialize message builder
    public MessageBuilderNew() {
        // initializing attributes
        this.currentMessage = "";
        this.selectedPhrases = new ArrayList<>();
        this.userName = "defaultUser";
    }

    // overloaded constructor to initialize message builder with a username
    public MessageBuilderNew(String userName) {
        // initializing attributes
        this.userName = userName;
        this.currentMessage = "";
        this.selectedPhrases = new ArrayList<>();
    }

    // addButtonPhrase() method to add a phrase to the selected phrases list
    public void addButtonPhrase(String phrase) {
        this.selectedPhrases.add(phrase);
        // updating the current message after adding a phrase
        updateCurrentMessage();
    }

    // showCurrentMessage() method to display the current message constructed from selected phrases
    public String showCurrentMessage() {
        // constructing the current message from selected phrases
        currentMessage = "";
        // iterating through selected phrases to build the message
        for (int i = 0; i < this.selectedPhrases.size(); i++) {
            // checking if the phrase exists in the phrase-meaning dictionary
            if (MainApp.phraseMeaningDictMap.containsKey(this.selectedPhrases.get(i))) {
                // appending the meaning of the phrase to the current message
                currentMessage += MainApp.phraseMeaningDictMap.get(this.selectedPhrases.get(i)) + " ";
            } else {
                // if phrase not found in dictionary, append the phrase itself
                currentMessage += this.selectedPhrases.get(i) + " ";
            }
        }
        
        // clearing selected phrases after showing the message
        this.selectedPhrases.clear();
        return currentMessage.trim();
    }

    // clearCurrentMessage() method to clear the current message and selected phrases
    public void clearCurrentMessage() {
        // resetting current message and clearing selected phrasesq
        this.currentMessage = "";
        this.selectedPhrases.clear();
    }

    // updateCurrentMessage() method to update the current message based on selected phrases
    public String updateCurrentMessage() {
        // message builder to construct the current message
        StringBuilder messageBuilder = new StringBuilder();

        // iterating through selected phrases to build the message
        for (int i = 0; i < this.selectedPhrases.size(); i++) {
            // checking if the phrase exists in the phrase-meaning dictionary
            if (MainApp.phraseMeaningDictMap.containsKey(this.selectedPhrases.get(i))) {
                // appending the meaning of the phrase to the message builder
                messageBuilder.append(MainApp.phraseMeaningDictMap.get(this.selectedPhrases.get(i))).append(" ");

                // adding space between phrases
                if (i < this.selectedPhrases.size() - 1) {
                    // append space if not the last phrase
                    messageBuilder.append(" ");
                }
            }
        }
        // setting the current message from the message builder
        this.currentMessage = messageBuilder.toString().trim();
        return this.currentMessage;
    }

    // setUserName() method to get the username associated with the message
    public void setUserName(String userName) {
        this.userName = userName;
    }
}



