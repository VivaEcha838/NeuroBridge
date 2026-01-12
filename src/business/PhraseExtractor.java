package business;

import data.FileManager;
import data.PhraseTile;
import data.UserProfile;
import javafx.scene.image.Image;

import java.io.*;
import java.util.*;


public class PhraseExtractor {

    private final UserProfile profile;
    private final MessageBuilderNew builder;
    private final Map<String, String> phraseImageMap;

    private static final String ICON_PATH = "resources/iconMap.txt";
    private static final String PHRASES_PATH = "resources/phrases.txt";

    public PhraseExtractor(UserProfile profile, MessageBuilderNew builder) {
        this.profile = profile;
        this.builder = builder;

        if (this.profile != null && this.profile.getName() != null) {
            this.builder.setUserName(this.profile.getName());
        }

        this.phraseImageMap = loadIconMapSafe(ICON_PATH);
    }

    public List<PhraseTile> loadTiles() {
        List<PhraseTile> tiles = new ArrayList<>();

        List<String> phrases = loadPhrasesSafe(PHRASES_PATH);
        for (String raw : phrases) {
            String phrase = extractPhrase(raw);
            if (phrase.isEmpty()) continue;

            Image icon = null;
            String iconPath = phraseImageMap.get(phrase);
            if (iconPath != null && !iconPath.isBlank()) {
                icon = loadImageSafe(iconPath.trim());
            }

            tiles.add(new PhraseTile(phrase, icon));
        }

        return tiles;
    }

    public void handleTileSelection(PhraseTile tile) {
        if (tile == null) {
            return;
        }
        String phrase = tile.getPhrase();
        if (phrase == null || phrase.isBlank()) {
            return;
        }

        builder.addButtonPhrase(phrase);
    }

    public void saveMessageToProfile(String msg) {
        if (msg == null) {
            return;
        }
        String trimmed = msg.trim();
        if (trimmed.isEmpty()) {
            return;
        }
        profile.addMessage(trimmed);
    }

    public void saveProfileToDisk() {
        FileManager.saveProfileToTxt(profile, "profiles/Jane.txt");
    }

    public void handleTileClick(PhraseTile tile) {
        handleTileSelection(tile);
    }

    // Helper Methods

    private static String extractPhrase(String line) {
        if (line == null) {
            return "";
        }
        String t = line.trim();
        if (t.isEmpty()) {
            return "";
        }

        int idx = t.indexOf(':');
        if (idx >= 0) {
            return t.substring(0, idx).trim();
        }
        return t;
    }

    private static List<String> loadPhrasesSafe(String phrasesPath) {
        List<String> out = new ArrayList<>();
        File f = new File(phrasesPath);

        if (!f.exists()) {
            System.out.println("phrases file not found: " + phrasesPath);
            return out;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String t = line.trim();
                if (!t.isEmpty()) {
                    out.add(t);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading phrases: " + e.getMessage());
        }

        return out;
    }

    private static Map<String, String> loadIconMapSafe(String iconMapPath) {
        Map<String, String> map = new HashMap<>();
        File f = new File(iconMapPath);

        if (!f.exists()) {
            System.out.println("icon map file not found: " + iconMapPath);
            return map;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String t = line.trim();
                if (t.isEmpty()) {
                    continue;
                }
                if (t.startsWith("#")) {
                    continue;
                }

                String[] parts;
                
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

                String phrase = parts[0].trim();
                String path = parts[1].trim();

                if (!phrase.isEmpty() && !path.isEmpty()) {
                    map.put(phrase, path);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading icon map: " + e.getMessage());
        }

        return map;
    }

    private static Image loadImageSafe(String path) {
        try {
            if (path.startsWith("/")) {
                InputStream is = PhraseExtractor.class.getResourceAsStream(path);
                if (is == null) return null;
                return new Image(is);
            } else {
                File f = new File(path);
                if (!f.exists()) {
                    return null;
                }
                try (InputStream is = new FileInputStream(f)) {
                    return new Image(is);
                }
            }
        } catch (Exception e) {
            return null;
        }
    }
}
