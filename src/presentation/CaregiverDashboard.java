/* 
    Vivaan Echambadi
    1/22/2026

    CaregiverDashboard() class will be responsible for creating the caregiver dashboard scene. It will include
    methods to build the scene, load caregiver guide text, and display recommendations based on selected scenarios.
    There will also be a method to create styled cards for displaying content. This class' main purpose is to create the base for 

*/

// importing packages
package presentation;

import data.UserProfile;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;

// CaregiverDashboard class definition
public class CaregiverDashboard {

    // initializing final variables for username and user profile
    private final String USERNAME;
    private final UserProfile PROFILE;

    // constructor for CaregiverDashboard class that takes in username and user profile as parameters
    public CaregiverDashboard(String username, UserProfile profile) {
        this.USERNAME = username;
        this.PROFILE = profile;
    }

    // buildScene() method to create and return the caregiver dashboard scene
    public Scene buildScene() {
        // Root layout 
        BorderPane root = new BorderPane();
        root.getStyleClass().addAll("app-root", "popup-root");
        root.setPadding(new Insets(14));

        // Label that displays the title and adds styling
        Label title = new Label("Caregiver Dashboard");
        title.getStyleClass().add("app-title");

        // Subtitle label that displays the username and adds styling
        Label sub = new Label("For: " + USERNAME);
        sub.getStyleClass().add("app-subtitle");

        // Header layout containing title and subtitle
        VBox header = new VBox(4, title, sub);
        header.setPadding(new Insets(10, 10, 14, 10));

        // Load guide text and scenarios
        String generalText = loadGeneralGuideText();
        HashMap<String, String> scenarios = loadScenarios();

        // Left controls (scenario + generate)
        Label scenarioLabel = new Label("Select Scenario");
        scenarioLabel.getStyleClass().add("muted");

        // Scenario selection combo box
        ComboBox<String> scenarioCombo = new ComboBox<>();
        // Adding scenario options to the combo box
        scenarioCombo.getItems().addAll(
            "General Recommendations",
            "Morning Routine",
            "After School",
            "Homework/Focus",
            "Transitions",
            "Overload/Meltdown",
            "Sleep/Wind-Down"
        );

        // Setting prompt text and clearing any previous selection
        scenarioCombo.setPromptText("Choose a scenario");
        scenarioCombo.getSelectionModel().clearSelection();

        // VBox for Left controls, setting alignment and width
        VBox left = new VBox(10, scenarioLabel, scenarioCombo);
        left.setAlignment(Pos.TOP_LEFT);
        left.setFillWidth(true);
        // Vbox card for Left controls with title "Controls"
        VBox leftCard = card("Controls", left);
        leftCard.setMinWidth(280);

        // Right output area
        TextArea outputArea = new TextArea("");
        outputArea.setWrapText(true);
        outputArea.setEditable(false);

        // Vbox card for Right output area with title "Recommendations"
        VBox rightCard = card("Recommendations", outputArea);

        // Split layout for left and right cards
        SplitPane split = new SplitPane(leftCard, rightCard);
        split.setDividerPositions(0.28);
        root.setTop(header);
        root.setCenter(split);

        // On selection, show content
        scenarioCombo.setOnAction(e -> {
            // getting the selected scenario from the combo box
            String selected = scenarioCombo.getValue();

            // if no scenario is selected, clear the output area and return
            if (selected == null) {
                outputArea.setText("");
                return;
            }

            // if "General Recommendations" is selected, display the general guide text
            if (selected.equals("General Recommendations")) {
                // if generalText is null or blank, display "Caregiver Guide unavailable."
                if (generalText == null || generalText.isBlank()) {
                    outputArea.setText("Caregiver Guide unavailable.");
                } 

                // else display the general guide text
                else {
                    outputArea.setText(generalText);
                }
                // return after displaying general recommendations
                return;
            }

            // for other scenarios, get the corresponding tip line from the scenarios map
            String tipLine = scenarios.get(normalizeKey(selected));

            // if no tip line is found, display "No recommendations available for this scenario."
            if (tipLine == null) {
                outputArea.setText("No recommendations available for this scenario.");
            } 
            // else format and display the tip line as bullet points
            else {
                outputArea.setText(formatScenarioRecommendations(tipLine));
            }
        });

        // Creating the scene with the root layout and applying CSS stylesheets
        Scene scene = new Scene(root, 1100, 780);
        scene.getStylesheets().add(MainApp.class.getResource("/resources/styles/neuro-theme.css").toExternalForm());
        scene.getStylesheets().add(MainApp.class.getResource("/resources/styles/popups.css").toExternalForm());

        // returning the created scene
        return scene;
    }

    // formatScenarioRecommendations() method to format scenario recommendations as bullet points
    private String formatScenarioRecommendations(String recommendations) {
        // if recommendations are null or blank, return empty string
        if (recommendations == null || recommendations.isBlank()) {
            return "";
        }

        // using StringBuilder to construct formatted text
        StringBuilder formatted = new StringBuilder();

        // splitting recommendations by comma to get individual items
        String[] items = recommendations.split(",");

        // iterating through each item and adding as a bullet point
        for (String item : items) {
            // trimming whitespace from the item
            String trimmed = item.trim();

            // if item is not empty, add it as a bullet point
            if (!trimmed.isEmpty()) {
                formatted.append("• ").append(trimmed).append("\n\n");
            }
        }

        // returning the formatted recommendations
        return formatted.toString().trim();
    }

    // normalizeKey() method will take in a String input and standardize scenario keys for consistent lookup
    private String normalizeKey(String s) {
        // if the input string is null, return an empty string
        if (s == null) {
            return "";
        }
        // return a trim of the whitespaces, converted to uppercase, with spaces replaced by underscores
        return s.trim().toUpperCase().replace(" ", "_");
    }

    // card() method that will take a String title and a Node content as input and will create a styled VBox card with a title and content
    private VBox card(String title, javafx.scene.Node content) {
        // creating a Label for the card title and adding styling
        Label t = new Label(title);
        t.getStyleClass().add("card-title");

        // creating a VBox to hold the title and content, adding styling and setting growth priority
        VBox box = new VBox(10, t, new Separator(), content);
        box.getStyleClass().add("card");
        VBox.setVgrow(content, Priority.ALWAYS);

        // returning the created VBox card
        return box;
    }

    // readGuideFile() method to read the caregiver guide text from resources or file system
    private String readGuideFile() {
        // Attempt to read from resources first
        try (InputStream is = getClass().getResourceAsStream("/resources/Caregiver Guide.txt")) {
            // if input stream is not null, read all bytes and return as a String
            if (is != null) {
                return new String(is.readAllBytes(), StandardCharsets.UTF_8);
            }
        } 
        // if an exception occurs, print an error message
        catch (Exception e) {
            System.out.println("Failed to read caregiver guide resource: " + e.getMessage());
        }

        // Fallback to attempting to read from file system
        try (InputStream is = new FileInputStream("resources/Caregiver Guide.txt")) {
            // reading all bytes from the file and returning as a String
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        
        }
        // if an exception occurs, print an error message and return an empty string 
        catch (Exception e) {
            System.out.println("Failed to read caregiver guide file: " + e.getMessage());
            return "";
        }
    }

    // loadScenarios() method to extract scenario recommendations from the caregiver guide text
    private HashMap<String, String> loadScenarios() {
        // reading the full caregiver guide text
        String full = readGuideFile();
         // initializing a HashMap to hold scenario recommendations
        HashMap<String, String> map = new HashMap<>();

        // if the full text is null or blank, return the empty map
        if (full == null || full.isBlank()) {
            return map;
        }

        // finding the index of the marker "SUPPORT RECOMMENDATIONS BY SITUATION"
        String marker = "SUPPORT RECOMMENDATIONS BY SITUATION";
        int index = full.indexOf(marker);

        // if the marker is not found, return the empty map
        if (index < 0) {
            return map;
        }

        // extracting the text after the marker
        String afterMarker = full.substring(index + marker.length()).trim();
        if (afterMarker.isBlank()) {
            //  if no text is found after the marker, return the empty map
            return map;
        }

        // splitting the text into lines and processing each line
        String[] lines = afterMarker.split("\\R");

        // for each loop to process each line
        for (String raw : lines) {
            // skipping null or empty lines
            if (raw == null) {
                continue;
            }

            // trimming the line and skipping if it's empty
            String line = raw.trim();
            
            // skipping empty lines
            if (line.isEmpty()) {
                continue;
            }

            // finding the index of the colon to separate key and value
            int colon = line.indexOf(':');
            if (colon < 0) {
                continue;
            }

            // extracting the key and value, trimming whitespaces
            String fileKey = line.substring(0, colon).trim(); 
            String value = line.substring(colon + 1).trim();       

            // if both key and value are non-empty, add them to the map
            if (!fileKey.isEmpty() && !value.isEmpty()) {
                // adding the normalized key and corresponding value to the map
                map.put(normalizeKey(fileKey), value);            
            }
        }
        
        // returning the populated map of scenario recommendations
        return map;
    }

    // loadGeneralGuideText() method to extract and format the general guide text from the caregiver guide
    private String loadGeneralGuideText() {
        // reading the full caregiver guide text
        String full = readGuideFile();

        // if the full text is null or blank, return "Caregiver Guide unavailable."
        if (full.isBlank()) {
            return "Caregiver Guide unavailable.";
        }

        // finding the index of the marker "SUPPORT RECOMMENDATIONS BY SITUATION"
        String marker = "SUPPORT RECOMMENDATIONS BY SITUATION";
        int idx = full.indexOf(marker);

        // extracting text before the scenario-specific recommendations
        String generalText;
        if (idx < 0) {
            generalText = full.trim();
        } else {
            generalText = full.substring(0, idx).trim();
        }
        
        // formatting the text for better readability with proper line breaks and structure
        return formatGeneralGuideText(generalText);
    }

    // formatGeneralGuideText() method to format the general guide text with proper structure and spacing
    private String formatGeneralGuideText(String rawText) {
        // if raw text is null or blank, return unavailable message
        if (rawText == null || rawText.isBlank()) {
            return "Caregiver Guide unavailable.";
        }

        // using StringBuilder to construct formatted text
        StringBuilder formatted = new StringBuilder();

        // splitting text into lines for processing
        String[] lines = rawText.split("\\R");

        // tracking if previous line was a section header
        boolean lastWasHeader = false;

        // iterating through each line to format appropriately
        for (String line : lines) {
            // trimming the line
            String trimmed = line.trim();

            // skipping empty lines
            if (trimmed.isEmpty()) {
                continue;
            }

            // checking if line is a section header (all caps or ends with colon)
            boolean isHeader = trimmed.equals(trimmed.toUpperCase()) && trimmed.length() > 3 && !trimmed.contains(":");

            // adding extra line break before headers (except the first one)
            if (isHeader && formatted.length() > 0) {
                formatted.append("\n\n");
                formatted.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
                formatted.append(trimmed);
                formatted.append("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
                lastWasHeader = true;
            }
            // handling numbered items (principles, core items)
            else if (trimmed.matches("^\\d+:.*")) {
                // adding line break before numbered items if not after header
                if (!lastWasHeader && formatted.length() > 0) {
                    formatted.append("\n");
                }
                formatted.append("\n  • ").append(trimmed.substring(trimmed.indexOf(':') + 1).trim());
                lastWasHeader = false;
            }
            // handling regular content lines
            else {
                // adding appropriate spacing
                if (!lastWasHeader && formatted.length() > 0) {
                    formatted.append("\n");
                }
                formatted.append(trimmed);
                lastWasHeader = false;
            }
        }

        // returning the formatted text
        return formatted.toString();
    }
}