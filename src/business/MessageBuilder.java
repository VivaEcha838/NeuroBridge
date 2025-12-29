package business;

import java.util.*;
import java.io.*;
import presentation.MainApp;

public class MessageBuilder {
    
    private String currentMessage;
    private List<String> selectedPhrases;

    private static final String PHRASE_PATH = "resources/phrases.txt";

    public MessageBuilder() {
        this.currentMessage = "";
        this.selectedPhrases = new ArrayList<>();
    }

    public void addButtonPhrase(String phrase) {
        this.selectedPhrases.add(phrase);
    }

    public static void loadPhraseDict() {
        try (BufferedReader reader = new BufferedReader(new FileReader(PHRASE_PATH))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] partsOfLine = line.split(":", 2);
                MainApp.phraseMeaningDictMap.put(partsOfLine[0].trim(), partsOfLine[1].trim());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    

    public String showCurrentMessage() {
        currentMessage = "";
        for(int i = 0; i <this.selectedPhrases.size();i++) {
             if(MainApp.phraseMeaningDictMap.containsKey(this.selectedPhrases.get(i))) {
                this.currentMessage = this.currentMessage + MainApp.phraseMeaningDictMap.get(this.selectedPhrases.get(i)) + "\n";
            }
        }  
        this.selectedPhrases.clear(); 
        return this.currentMessage;
    }

    public void clearCurrentMessage() {
        currentMessage = "";
    }

    // public void addMeaning(String meaning) {
    //     if (meaning != null && !meaning.isEmpty()) {
    //         currentMessage.add(meaning);
    //     }
    // }

    // public void showFinalMessage() {
    //     String finalMessage = "";

    //     for (int i = 0; i < currentMessage.size(); i++) {
    //         finalMessage += currentMessage.get(i) + " ";
    //         if (i < currentMessage.size() - 1) {
    //             finalMessage += " ";
    //         }
    //     }

    //     if (!finalMessage.isEmpty()) {
    //         System.out.println("Final Message for " + userName + ": " + finalMessage);
    //         saveMessageToHistory(finalMessage);
    //     }
    //     else {
    //         System.out.println("No message to be shown for " + userName);
    //     }

    //     currentMessage.clear();
    // }

    // private void saveMessageToHistory(String message) {
    //     try {
    //         FileWriter writer1 = new FileWriter(historyFile, true);
    //         writer1.write(userName + ": " + message + "\n");
    //         writer1.close();

    //         FileWriter writer2 = new FileWriter("data/messageHistory.txt", true);
    //         writer2.write(userName + ": " + message + "\n");
    //         writer2.close();
    //     }
    //     catch (IOException e) {
    //         System.out.println("Error saving message to history: " + e.getMessage());
    //     }
    // }

    // public void viewHistory() {
    //     System.out.println("\nMessage History for " + userName + ":");

    //     try {
    //         Scanner scanner = new Scanner(new File(historyFile));
    //         while (scanner.hasNextLine()) {
    //             String line = scanner.nextLine();
    //             if (line.startsWith(userName + ":")) {
    //                 System.out.println("- " + line.substring(userName.length() + 2));
    //             }
    //         }
    //         scanner.close();
    //     }
    //     catch (FileNotFoundException e) {
    //         System.out.println("No history found for " + userName);
    //     }
    // }

    // public void addPhrasesAndMeanings(String phrase, String meaning) {
    //     if (phrase != null && !phrase.isEmpty() && meaning != null && !meaning.isEmpty()) {
    //         selectedPhrases.add(new PhrasePair(phrase, meaning));
    //     }
    // }

    // public void undoLastSelection() {
    //     if (!selectedPhrases.isEmpty()) {
    //         PhrasePair removedPair = selectedPhrases.removeLast();
    //         System.out.println("Removed: " + removedPair.toString());
    //     }

    //     else {
    //         System.out.println("No phrases to undo.");
    //     }

    //     if (!currentMessage.isEmpty()) {
    //         currentMessage.remove(currentMessage.size() - 1);
    //     }
    // }

    // public void viewSelectedPhrases() {
    //     System.out.println("\nSelected Phrases and Meanings:");
    //     for (PhrasePair pair : selectedPhrases) {
    //         System.out.println("- " + pair.toString());
    //     }
    // }

    

}
