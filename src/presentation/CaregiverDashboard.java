package presentation;

import data.UserProfile;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;

public class CaregiverDashboard {

    private final String USERNAME;
    private final UserProfile PROFILE;
    private final SupportRecommendation RECOMMENDER;

    public CaregiverDashboard(String username, UserProfile profile) {
        this.USERNAME = username;
        this.PROFILE = profile;
        this.RECOMMENDER = new SupportRecommendation();
    }

    public Scene buildScene() {
        BorderPane root = new BorderPane();
        root.getStyleClass().addAll("app-root", "popup-root");
        root.setPadding(new Insets(14));

        // Header
        Label title = new Label("Caregiver Dashboard");
        title.getStyleClass().add("app-title");

        Label sub = new Label("For: " + USERNAME);
        sub.getStyleClass().add("app-subtitle");

        VBox header = new VBox(4, title, sub);
        header.setPadding(new Insets(10, 10, 14, 10));

        String generalText = loadGeneralGuideText();
        HashMap<String, String> scenarios = loadScenarios();

        // Left controls (scenario + generate)
        Label scenarioLabel = new Label("Select Scenario");
        scenarioLabel.getStyleClass().add("muted");

        ComboBox<String> scenarioCombo = new ComboBox<>();
        scenarioCombo.getItems().addAll(
            "General Recommendations",
            "Morning Routine",
            "After School",
            "Homework/Focus",
            "Transitions",
            "Overload/Meltdown",
            "Sleep/Wind-Down"
        );
        scenarioCombo.setPromptText("Choose a scenario");
        scenarioCombo.getSelectionModel().clearSelection();

        VBox left = new VBox(10, scenarioLabel, scenarioCombo);
        left.setAlignment(Pos.TOP_LEFT);
        left.setFillWidth(true);
        VBox leftCard = card("Controls", left);
        leftCard.setMinWidth(280);


        TextArea outputArea = new TextArea("");
        outputArea.setWrapText(true);
        outputArea.setEditable(false);

        VBox rightCard = card("Recommendations", outputArea);

        // Split layout
        SplitPane split = new SplitPane(leftCard, rightCard);
        split.setDividerPositions(0.28);
        root.setTop(header);
        root.setCenter(split);

        // On selection, show content
        scenarioCombo.setOnAction(e -> {
            String selected = scenarioCombo.getValue();
            if (selected == null) {
                outputArea.setText("");
                return;
            }

            if (selected.equals("General Recommendations")) {
                outputArea.setText(generalText == null || generalText.isBlank()
                        ? "Caregiver Guide unavailable."
                        : generalText);
                return;
            }

            String tipLine = scenarios.get(normalizeKey(selected));
            outputArea.setText(tipLine == null
                    ? "No recommendations available for this scenario."
                    : tipLine);
        });

        Scene scene = new Scene(root, 1100, 780);
        scene.getStylesheets().add(MainApp.class.getResource("/resources/styles/neuro-theme.css").toExternalForm());
        scene.getStylesheets().add(MainApp.class.getResource("/resources/styles/popups.css").toExternalForm());
        return scene;
    }

    private String normalizeKey(String s) {
        if (s == null) {
            return "";
        }
        return s.trim().toUpperCase().replace(" ", "_");
    }

    private String toTitleCase(String s) {
        if (s == null || s.isBlank()) return s;
        String[] parts = s.toLowerCase().split(" ");
        StringBuilder sb = new StringBuilder();
        for (String p : parts) {
            if (p.length() > 0) {
                sb.append(Character.toUpperCase(p.charAt(0)));
                if (p.length() > 1) {
                    sb.append(p.substring(1));
                }
                sb.append(" ");
            }
        }
        return sb.toString().trim();
    }

    private VBox card(String title, javafx.scene.Node content) {
        Label t = new Label(title);
        t.getStyleClass().add("card-title");

        VBox box = new VBox(10, t, new Separator(), content);
        box.getStyleClass().add("card");
        VBox.setVgrow(content, Priority.ALWAYS);
        return box;
    }

    private String readGuideFile() {
        try (InputStream is = getClass().getResourceAsStream("/resources/Caregiver Guide.txt")) {
            if (is != null) {
                return new String(is.readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            System.out.println("Failed to read caregiver guide resource: " + e.getMessage());
        }

        try (InputStream is = new FileInputStream("resources/Caregiver Guide.txt")) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println("Failed to read caregiver guide file: " + e.getMessage());
            return "";
        }
    }

    private HashMap<String, String> loadScenarios() {
        String full = readGuideFile();
        HashMap<String, String> map = new HashMap<>();
        if (full == null || full.isBlank()) {
            return map;
        }

        String marker = "SUPPORT RECOMMENDATIONS BY SITUATION";
        int index = full.indexOf(marker);
        if (index < 0) {
            return map;
        }

        String afterMarker = full.substring(index + marker.length()).trim();
        if (afterMarker.isBlank()) {
            return map;
        }

        String[] lines = afterMarker.split("\\R");
        for (String raw : lines) {
            if (raw == null) {
                continue;
            }
            String line = raw.trim();
            if (line.isEmpty()) {
                continue;
            }

            int colon = line.indexOf(':');
            if (colon < 0) {
                continue;
            }

            String fileKey = line.substring(0, colon).trim(); 
            String value = line.substring(colon + 1).trim();       

            if (!fileKey.isEmpty() && !value.isEmpty()) {
                map.put(normalizeKey(fileKey), value);            
            }
        }
        
        return map;
    }

    private String toDisplayName(String s) {
        if (s == null) {
            return "";
        }
        s = s.trim();
        if (s.isEmpty()) {
            return "";
        }

        s = s.replace("_", " ");
        List<String> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
           if (Character.isLetterOrDigit(c)) {
                currentToken.append(c);
           } else if (c == '/' || c == '-') {
                if (currentToken.length() > 0) {
                    tokens.add(currentToken.toString());
                    currentToken.setLength(0);
                }
                tokens.add(String.valueOf(c));
            } 
            if (c == '/' || c == '-') {
                tokens.add(String.valueOf(c));
            }
            else {
                tokens.add(" ");
            }
        }

        if (currentToken.length() > 0) {
            tokens.add(currentToken.toString());
        }

        StringBuilder result = new StringBuilder();
        boolean lastWasSpace = false;
        for (String token : tokens) {
            if (token.equals(" ")) {
                if (!lastWasSpace && result.length() > 0) {
                    result.append(" ");
                    lastWasSpace = true;
                }
                continue;
            }

            if (token.equals("/") || token.equals("-")) {
                int n = result.length();
                if (n > 0 && result.charAt(n - 1) == ' ') {
                    result.deleteCharAt(n - 1);
                }
                result.append(token);
                lastWasSpace = false;
                continue;
            }

            String word = token.toLowerCase();
            result.append(Character.toUpperCase(word.charAt(0)));
            if (word.length() > 1) {
                result.append(word.substring(1));
            }
            lastWasSpace = false;
        }

        return result.toString();
    }

    private String loadGeneralGuideText() {
        String full = readGuideFile();
        if (full.isBlank()) {
            return "Caregiver Guide unavailable.";
        }

        String marker = "SUPPORT RECOMMENDATIONS BY SITUATION";
        int idx = full.indexOf(marker);

        if (idx < 0) {
            return full.trim();
        }

        return full.substring(0, idx).trim();
    }
}