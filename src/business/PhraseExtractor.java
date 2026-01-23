/* 
    Vivaan Echambadi
    1/22/2026

    PhraseExtractor() class will extract phrases from a predefined list, associate them with icons, and handle user interactions
    such as selecting phrases and saving messages to a user profile. It will also manage loading phrases and icon mappings from files.
    It will provide methods to load phrase tiles, handle tile selections, save messages, and clear message history.

*/

// importing packages
package business;

import data.FileManager;
import data.PhraseTile;
import data.UserProfile;
import javafx.scene.image.Image;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

// PhraseExtractor class definition
public class PhraseExtractor {

    // private attributes for user profile, message builder, and phrase-image mapping
    private final UserProfile profile;
    private final MessageBuilderNew builder;
    private final Map<String, String> phraseImageMap;

    private static final String ICON_PATH = "resources/iconMap.txt";
    private static final String PHRASES_PATH = "resources/phrases.txt";
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    // constructor to initialize phrase extractor with a user profile and message builder
    public PhraseExtractor(UserProfile profile, MessageBuilderNew builder) {
        // setting the profile and builder attributes
        this.profile = profile;
        this.builder = builder;

        // setting the user name in the message builder if the profile and name are not null
        if (this.profile != null && this.profile.getName() != null) {
            // setting the user name in the message builder
            this.builder.setUserName(this.profile.getName());
        }

        // loading the phrase-image mapping from the icon map file
        this.phraseImageMap = loadIconMapSafe(ICON_PATH);
    }

    // loadTiles() method to load phrase tiles from the phrases file and associate them with icons
    public List<PhraseTile> loadTiles() {
        // list to hold the loaded phrase tiles
        List<PhraseTile> tiles = new ArrayList<>();

        // loading phrases from the phrases file
        List<String> phrases = loadPhrasesSafe(PHRASES_PATH);

        // looping through each loaded phrase and creating a PhraseTile for it
        for (String raw : phrases) {
            // extracting the phrase from the raw line
            String phrase = extractPhrase(raw);

            // skipping empty phrases
            if (phrase.isEmpty()) {
                continue;
            }

            // loading the associated icon for the phrase from the phrase-image mapping
            Image icon = null;
            String iconPath = phraseImageMap.get(phrase);

            // loading the icon image if the icon path is valid
            if (iconPath != null && !iconPath.isBlank()) {
                icon = loadImageSafe(iconPath.trim());
            }

            // creating a PhraseTile with the phrase and its associated icon
            tiles.add(new PhraseTile(phrase, icon));
        }

        // returning the list of loaded phrase tiles
        return tiles;
    }

    // handleTileSelection() method will take in a PhraseTile object and handle the selection of a phrase tile
    public void handleTileSelection(PhraseTile tile) {
        // checking if the tile or its phrase is null or blank
        if (tile == null) {
            return;
        }

        // getting the phrase from the tile
        String phrase = tile.getPhrase();

        // checking if the phrase is null or blank
        if (phrase == null || phrase.isBlank()) {
            return;
        }

        // adding the phrase to the message builder
        builder.addButtonPhrase(phrase);
    }

    // saveMessageToProfile() method will take in a String as input and save a message to the user's profile with a timestamp
    public void saveMessageToProfile(String msg) {
        // checking if the message is null or empty after trimming
        if (msg == null) {
            return;
        }

        // trimming the message and checking if it is empty
        String trimmed = msg.trim();
        if (trimmed.isEmpty()) {
            return;
        }

        // getting the current date and time and formatting it
        LocalDateTime now = LocalDateTime.now();
        String time = now.format(FORMAT);
        String message = time + " | " + trimmed;

        // adding the message to the user's profile
        profile.addMessage(message);
    }

    // saveProfileToDisk() method will save the user's profile to disk
    public void saveProfileToDisk() {
        // saving the user's profile to a text file
        FileManager.saveProfileToTxt(profile, "profiles/Jane.txt");
    }

    // handleTileClick() method will handle the click event on a phrase tile
    public void handleTileClick(PhraseTile tile) {
        handleTileSelection(tile);
    }

    // getUserName() method will return the user's name from the profile
    public String getUserName() {
        // checking if the profile and name are not null
        if (profile != null && profile.getName() != null) {
            // returning the user's name from the profile
            return profile.getName();
        }

        // returning "Unknown" if the profile or name is null
        return "Unknown";
    }

    // clearHistory() method will clear the message history from the user's profile
    public void clearHistory() {
        profile.setMessages(null);
    }

    // HELPER METHODS

    // extractPhrase() method to extract the phrase from a raw line
    private static String extractPhrase(String line) {
        // checking if the line is null or empty after trimming
        if (line == null) {
            return "";
        }
        String t = line.trim();
        if (t.isEmpty()) {
            return "";
        }

        // finding the index of the first colon in the line
        int idx = t.indexOf(':');

        // returning the substring before the colon, or the entire line if no colon is found
        if (idx >= 0) {
            // returning the substring before the colon
            return t.substring(0, idx).trim();
        }

        // returning the entire line if no colon is found
        return t;
    }

    // loadPhrasesSafe() method will accept a String phrasesPath and will load phrases from a file safely
    private static List<String> loadPhrasesSafe(String phrasesPath) {
        // list to hold the loaded phrases
        List<String> out = new ArrayList<>();
        // creating a File object for the phrases file
        File f = new File(phrasesPath);

        // checking if the file exists
        if (!f.exists()) {
            // printing an error message if the file does not exist
            System.out.println("phrases file not found: " + phrasesPath);
            return out;
        }

        // reading the phrases from the file
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            // reading each line from the file
            String line;

            // looping through each line in the file
            while ((line = br.readLine()) != null) {
                String t = line.trim();

                // adding the trimmed line to the list if it is not empty
                if (!t.isEmpty()) {
                    out.add(t);
                }
            }
        } 
        // handling IOException during file reading
        catch (IOException e) {
            // printing an error message if an exception occurs
            System.out.println("Error reading phrases: " + e.getMessage());
        }

        // returning the list of loaded phrases
        return out;
    }

    // loadIconMapSafe() method will accept a String iconMapPath and will load the icon map from a file safely
    private static Map<String, String> loadIconMapSafe(String iconMapPath) {
        // map to hold the loaded phrase-icon mappings and creating a File object for the icon map file
        Map<String, String> map = new HashMap<>();
        File f = new File(iconMapPath);

        // checking if the file exists, then printing an error message and returning an empty map if it does not exist
        if (!f.exists()) {
            System.out.println("icon map file not found: " + iconMapPath);
            return map;
        }

        // reading the icon mappings from the file
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            // reading each line from the file
            String line;

            // looping through each line in the file
            while ((line = br.readLine()) != null) {
                // trimming the line and skipping empty lines or comments
                String t = line.trim();

                // skipping empty lines and comments
                if (t.isEmpty()) {
                    continue;
                }

                // skipping comment lines
                if (t.startsWith("#")) {
                    continue;
                }

                // splitting the line into phrase and icon path using '=', ':', or ',' as delimiters
                String[] parts;
                
                // determining the delimiter and splitting the line accordingly
                if (t.contains("=")) {
                    parts = t.split("=", 2);
                } 
                else if (t.contains(":")) {
                    parts = t.split(":", 2);
                }
                else if (t.contains(",")) {
                    parts = t.split(",", 2);
                }
                else {
                    continue;
                }
                
                // checking if the split resulted in exactly two parts
                String phrase = parts[0].trim();
                String path = parts[1].trim();

                // adding the phrase and icon path to the map if both are not empty
                if (!phrase.isEmpty() && !path.isEmpty()) {
                    map.put(phrase, path);
                }
            }
        } 
        // handling IOException during file reading
        catch (IOException e) {
            // printing an error message if an exception occurs
            System.out.println("Error reading icon map: " + e.getMessage());
        }

        // returning the map of loaded phrase-icon mappings
        return map;
    }

    // loadImageSafe() method will accept a String path and will load an image safely
    private static Image loadImageSafe(String path) {
        // trying to load the image from the specified path
        try {
            // checking if the path is a resource path
            if (path.startsWith("/")) {
                InputStream is = PhraseExtractor.class.getResourceAsStream(path);

                // if the resource is not found, return null
                if (is == null) {
                    return null;
                }
                // returning the loaded image
                return new Image(is);
            } else {
                // loading the image from a file path
                File f = new File(path);

                // if the file does not exist, return null
                if (!f.exists()) {
                    return null;
                }

                // returning the loaded image
                try (InputStream is = new FileInputStream(f)) {
                    return new Image(is);
                }
            }
        }
        // handling any exceptions that occur during image loading
        catch (Exception e) {
            return null;
        }
    }
}
