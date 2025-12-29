package business;
import java.util.*;

import data.FileManager;
import data.PhraseTile;
import data.UserProfile;
import javafx.scene.image.Image;

import java.io.*;

public class PhraseExtractor {
    private UserProfile profile;
    private MessageBuilder builder;
    private Map<String, String> phraseImageMap;
    private static final String ICON_PATH = "resources/iconMap.txt";

    public PhraseExtractor(UserProfile profile, MessageBuilder builder) {
        this.profile = profile;
        this.builder = builder;
        this.phraseImageMap = new HashMap<>();
    }

     public void handleTileClick(PhraseTile tile) {
        builder.addButtonPhrase(tile.getPhrase());
    }

    public List<PhraseTile> loadTiles() {
        List<PhraseTile> phraseImageTiles = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(ICON_PATH))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] partsOfLine = line.split(":", 2);
                    phraseImageMap.put(partsOfLine[0].trim(), partsOfLine[1].trim());
                }
            } catch (Exception e) {
            System.out.println("Error loading phrases from file: " + e.getMessage());
        }

        phraseImageMap.forEach (
            (key,value) ->{
                try{
                    phraseImageTiles.add(new PhraseTile(key, new Image(new FileInputStream(value))));
                } catch(Exception e){
                    System.out.println("Cant load Image");
                }
            }); 
            
       

        return phraseImageTiles;
    }    

    public void saveMessageToProfile(String msg) {
        // String key = java.time.LocalDate.now().toString();
        // profile.addMessage(key, msg);
        profile.addMessage(msg);

    } 
    
    public void saveProfileToDisk() {
        FileManager.saveProfile(profile);
    } 
    
    public void showTileMeaning(PhraseTile tile) {
        String phrase = tile.getPhrase();
        
        // if (linkPhraseToMeaningMap.containsKey(phrase)) {
        //     String meaning = linkPhraseToMeaningMap.get(phrase);
        //     builder.addMeaning(meaning);
        //     builder.addPhrasesAndMeanings(phrase, meaning);
        // }
        // else {
        //     System.out.println("Meaning not found for tile: " + phrase);
        // }
    }
}

        



