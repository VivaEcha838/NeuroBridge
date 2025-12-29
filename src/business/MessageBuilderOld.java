package business;

import java.util.*;

import data.PhraseTile;

import java.io.*;

public class MessageBuilderOld {
    private Map<String, String> categoryMap;
    private String currentMessage;

    public MessageBuilderOld(String userName, String phraseFile, String historyFile) {
        this.categoryMap = new HashMap<>();
        loadPhrasesByCategory(phraseFile);
    }   

    public MessageBuilderOld() {

    }
    
    
    private void loadPhrasesByCategory(String fileName) {
        try {
            Scanner inputFile = new Scanner(new File(fileName));
            while (inputFile.hasNextLine()) {

                String line = inputFile.nextLine();
                String[] partsOfLine = line.split(":");

                if (partsOfLine.length == 2) {
                    //String category = partsOfLine[0].trim();
                    String phrase = partsOfLine[1].trim();
                    String meaning = partsOfLine[2].trim();

                    categoryMap.put(phrase, meaning);
                    
                    /*if (!categoryMap.containsKey(phrase)) {
                        categoryMap.put(category, new ArrayList<PhraseOption>());
                    }*/
                    //categoryMap.get(category).add(new PhraseOption(phrase, meaning));
                }
            }
            inputFile.close();

        } 
        catch (FileNotFoundException e) {
            System.out.println("Error loading the tiles: " + e.getMessage());
        }
    }

    public void showTileMeaning(PhraseTile tile) {
        String phrase = tile.getPhrase();
        if (categoryMap.containsKey(phrase)) {
            currentMessage += categoryMap.get(phrase) + " ";
            //System.out.println("Tile: " + phrase + " Meaning: " + meaning);
            
        } 
        else {
            System.out.println("Meaning not found for tile: " + phrase);
        }
    }

    public void clearCurrentMessage() {
        currentMessage = "";
    }


    /*public void addToMessage(String phrase) {
        currentMessage.add(phrase);
    }*/

    /*public void showPlusLogMessages() {
        String message = "";

        for (int i = 0; i < currentMessage.size(); i++) {
            message += currentMessage.get(i);

            if (i < currentMessage.size() - 1) {
                message += " ";
            }
        }

        System.out.println("\n Final Message for " + userName + ": " + message);
        storeMessageInHistory(message);
        currentMessage.clear();
    }

    private void storeMessageInHistory(String message) {
        try {
            FileWriter writer = new FileWriter("historyLog.txt", true);
        }
    }

    */

    public String showCurrentMessage() {
        return currentMessage;
    }
}


