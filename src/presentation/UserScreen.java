/* 
    Vivaan Echambadi
    1/6/2026

    UserScreen class represents the user interface for displaying user profile information. UserScreen class
    will contain methods to create the display box with user details, show message history, and caregiver dashboard.
    The class will also contain more buttons, such as the Show Message, Clear Message, and Save Message buttons. These
    buttons will interact with the MessageBuilderNew and PhraseExtractor classes to manage message creation and saving.

d
*/

// importing packages
package presentation;
import java.util.*;

import business.*;
import data.UserProfile;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.*;


public class UserScreen {
    // creating private properties
    private String userName;
    private MessageGarden garden;
    private ImageView gardenView;
    private Label gardenLabel;

    // message engine + phrase click handler
    MessageBuilderNew builder;
    PhraseExtractor extractor;

    // root layout container
    BorderPane bp;

    // shared UI pieces that update
    TextArea messageArea;

    // constructor that takes in UserProfile object
    // will intialize all properties and create the user interface
    public UserScreen(UserProfile profile) {
        this.userName = profile.getName();
        this.garden = new MessageGarden(userName);

        // business logic objects
        this.builder = new MessageBuilderNew();
        this.extractor = new PhraseExtractor(profile, builder);

        // root pane for the screen
        this.bp = new BorderPane();
        this.bp.getStyleClass().add("app-root");
        bp.setTop(buildHeader(profile));
        bp.setCenter(buildMain(profile));
    }

    // buildHeader() method will take in a UserProfile object and will return the header of the app in the top left corner with the
    // name of the app "NeuroBridge" as well as the user name.
    private HBox buildHeader(UserProfile profile) {
        // creating new Label object for app title name and adding style to it
        Label title = new Label("NeuroBridge");
        title.getStyleClass().add("app-title");

        // creating new Label object that will display the username
        Label subtitle = new Label("User: " + profile.getName() + "   •   Age: " + profile.getAge());
        subtitle.getStyleClass().add("app-subtitle");
        
        // creating a VBox object that will take in the title and subtitle and will align in the top left of the main screen
        VBox textBlock = new VBox(4, title, subtitle);
        textBlock.setAlignment(Pos.CENTER_LEFT);

        // returning header
        HBox header = new HBox(textBlock);
        header.getStyleClass().add("app-header");
        return header;
    }

    // buildMain() methid will take in a UserProfile object and build the main screen, which has a section for the icons,
    // profile information, message garden, and caregiver section.
    private SplitPane buildMain(UserProfile profile) {
        // creating MessagePanel object that creates the left side of the screen
        MessagePanel messagePanel = new MessagePanel(extractor);
        VBox tilesCard = wrapInCard("Phrases", messagePanel.loadTiles());
        tilesCard.setMinWidth(640);

        // Right side "card stack", creating right side of the screen
        VBox messageCard = buildMessageCard(profile);
        VBox profileCard = buildProfileCard(profile);
        VBox caregiverCard = buildCaregiverToolsCard(profile);
        VBox gardenCard = buildGarden();

        // adding the rightStack to a vbox
        VBox rightStack = new VBox(14, messageCard, profileCard, caregiverCard, gardenCard);
        rightStack.setPadding(new Insets(14));
        rightStack.setFillWidth(true);

        // setting up a divider plane and returning that so there is a clear distinction between sides
        SplitPane split = new SplitPane(tilesCard, rightStack);
        split.setDividerPositions(0.52);
        split.setPadding(new Insets(14));
        return split;
    }

    // buildMessageCard() method will take in a UserProfile object and will return the message card section of the user screen.
    private VBox buildMessageCard(UserProfile profile) {

        // creating the message area text box and setting its properties
        messageArea = new TextArea();
        messageArea.setEditable(false);
        messageArea.setWrapText(true);
        messageArea.setPrefHeight(140);

        // creating the show, clear, and save buttons and setting their properties, as well as their actions
        Button showButton = new Button("Show Message");
        showButton.getStyleClass().addAll("button", "primary");
        showButton.setOnAction(e -> {
            // When Show Message button is clicked, get the current message from the builder,
            // display it in the message area, and save it to the user's profile.
            String message = builder.showCurrentMessage();
            messageArea.setText(message);
            extractor.saveMessageToProfile(message);
            
            // Update the message garden and record the message if it's not blank
            if (message != null && !message.isBlank()) {
                garden.recordMessage();
                updateGarden();
            }
        });

        // Clear Message button
        Button clearButton = new Button("Clear Message");
        clearButton.getStyleClass().addAll("button", "danger");
        clearButton.setOnAction(e -> {
            // When Clear Message button is clicked, clear the current message in the builder and the message area.
            builder.clearCurrentMessage();
            messageArea.clear();
        });

        // Save Message button
        Button saveButton = new Button("Save Message");
        saveButton.getStyleClass().add("button");
        // on action, call extractor's saveProfileToDisk() method
        saveButton.setOnAction(e -> extractor.saveProfileToDisk());

        // arranging buttons in an HBox
        HBox actions = new HBox(10, showButton, clearButton, saveButton);
        actions.setAlignment(Pos.CENTER_LEFT);

        // returning the message card wrapped in a card style
        VBox content = new VBox(10, messageArea, actions);
        return wrapInCard("Message", content);
    }

    // buildProfileCard() method will take in a UserProfile object and will return the profile card section of the user screen.
    private VBox buildProfileCard(UserProfile profile) {
        // creating a grid pane to hold profile information
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);

        // adding all of the profile information to the grid
        int row = 0;
        row = addProfileRow(grid, row, "Name", profile.getName());
        row = addProfileRow(grid, row, "Age", String.valueOf(profile.getAge()));
        row = addProfileRow(grid, row, "Diagnosis", profile.getPrimaryDiagnosis());
        row = addProfileRow(grid, row, "Sensory Preferences", String.join(", ", profile.getSensoryPref()));
        row = addProfileRow(grid, row, "Communication Methods", String.join(", ", profile.getCommunicationMethods()));
        row = addProfileRow(grid, row, "Calming Strategies", String.join(", ", profile.getCalmingStrategies()));
        row = addProfileRow(grid, row, "Known Triggers", String.join(", ", profile.getKnownTriggers()));
        row = addProfileRow(grid, row, "Favorite Activities", String.join(", ", profile.getFavoriteActivities()));
        row = addProfileRow(grid, row, "Preferred Learning Style", profile.getPreferredLearning());
        row = addProfileRow(grid, row, "Non-Verbal", profile.getIsNonVerbal() ? "Yes" : "No");
        row = addProfileRow(grid, row, "Additional Notes", profile.getNotes());

        // Column widths: left column fixed, right column grows
        ColumnConstraints c1 = new ColumnConstraints();
        c1.setMinWidth(170);

        // Column 2 grows to fill space
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setHgrow(Priority.ALWAYS);

        grid.getColumnConstraints().addAll(c1, c2);

        // wrapping grid in a scroll pane in case profile is long
        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);
        scroll.setPannable(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        // returning the profile card wrapped in a card style
        return wrapInCard("Profile", scroll);
    }

    // buildCaregiverToolsCard() buttons will open secondary windows for message history and caregiver dashboard.
    private VBox buildCaregiverToolsCard(UserProfile profile) {
        // creating the message history, caregiver dashboard, and clear history buttons, and setting their actions
        Button messageHistoryButton = new Button("Message History");
        messageHistoryButton.getStyleClass().addAll("button", "primary");
        messageHistoryButton.setOnAction(e -> showMessageHistory(profile));

        // Caregiver Dashboard button
        Button caregiverInfoButton = new Button("Caregiver Dashboard");
        caregiverInfoButton.getStyleClass().add("button");
        caregiverInfoButton.setOnAction(e -> showCaregiverDashboard(profile));

        // Clear Message History button
        Button clearHistoryButton = new Button("Clear Message History");
        clearHistoryButton.getStyleClass().addAll("button", "danger");
        clearHistoryButton.setOnAction(e -> {
            handleClearHistory(profile);
        });

        // arranging buttons in an HBox
        HBox row = new HBox(10, messageHistoryButton, caregiverInfoButton, clearHistoryButton);
        row.setAlignment(Pos.CENTER_LEFT);

        // returning the caregiver tools card wrapped in a card style
        return wrapInCard("Caregiver Tools", row);
    }

    // buildGarden() method will build the message garden section of the user screen.
    private VBox buildGarden() {
        // setting up the image view and label for the garden, as well as updating it
        gardenView = new ImageView();
        gardenView.setFitWidth(120);
        gardenView.setPreserveRatio(true);
        gardenView.setFitHeight(120);

        // setting up the label for the garden and calling updateGarden()
        gardenLabel = new Label();
        gardenLabel.getStyleClass().add("muted");
        updateGarden();

        // arranging garden image and label in a vbox
        VBox gardenBox = new VBox(8, gardenView, gardenLabel);
        gardenBox.setAlignment(Pos.CENTER);

        // returning the garden box wrapped in a card style
        return wrapInCard("Message Garden", gardenBox);
    }

    // updateGarden() method will update the message garden image and label based on the current state of the garden.
    private void updateGarden() {
        // loading the garden image based on the current stage
        String path = garden.getImagePath();
        InputStream input = getClass().getResourceAsStream(path);

        // if image not found in resources, try loading from file system
        if (input == null) {
            String fileName = path;
            // remove leading slash if present
            if (fileName.startsWith("/")) {
                fileName = fileName.substring(1);
            }

            // trying two locations: "resources/" + fileName and fileName directly
            File file1 = new File("resources/" + fileName);
            File file2 = new File(fileName);

            // if the file is found, then load it
            try {
                if (file1.exists()) {
                    input = new FileInputStream(file1);
                } else if (file2.exists()) {
                    input = new FileInputStream(file2);
                } else {
                    System.out.println("Garden image not found: " + path);
                    return;
                }
            } catch (FileNotFoundException e) {
                // If the file is not found, log the error and return
                System.out.println("Garden image file not found: " + e.getMessage());
                return;
            }
        }

        // setting the image and label text
        Image image = new Image(input);
        gardenView.setImage(image);
        gardenLabel.setText("Stage: " + garden.getCurrentStageName() + " | Messages Today: " + garden.getMessagesToday());
    }

    // handleClearHistory() method will take in a UserProfile object and will clear the message history after confirmation
    private void handleClearHistory(UserProfile profile) {
        // confirming that profile is not null
        if (profile == null) {
            return;
        }

        // creating confirmation alert
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Clear History");
        confirm.setHeaderText("Are you sure you want to clear the message history?");
        confirm.setContentText("This action cannot be undone.");

        // setting up custom buttons: Cancel and Clear History
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType clear = new ButtonType("Clear History", ButtonBar.ButtonData.OK_DONE);
        confirm.getButtonTypes().setAll(clear, cancel);

        // applying styles from CSS file to the dialog
        DialogPane pane = confirm.getDialogPane();
        pane.getStylesheets().add(
            getClass().getResource("/resources/styles/neuro-theme.css").toExternalForm()
        );
        pane.getStylesheets().add(
            getClass().getResource("/resources/styles/popups.css").toExternalForm()
        );
        pane.getStyleClass().addAll("app-root", "popup-root");

        // showing the confirmation dialog and handling the result
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != clear) {
            return;
        }

        // clearing the message history from the profile and builder, then saving to disk
        profile.getMessages().clear();
        builder.clearCurrentMessage();
        extractor.saveProfileToDisk();
    }

    // showMessageHistory() method will take in a UserProfile Object and will display the message history
    private void showMessageHistory(UserProfile profile) {
        Stage messageHistoryStage = new Stage();
        messageHistoryStage.initModality(Modality.APPLICATION_MODAL);
        messageHistoryStage.setTitle("Message History for " + this.userName);

        // Creating the message history box
        VBox messageHistoryBox = new VBox(12);
        messageHistoryBox.getStyleClass().add("popup-root");

        // creating title label
        Label title = new Label("Message History");
        title.getStyleClass().add("popup-title");

        // creating the text area to display message history
        TextArea historyArea = new TextArea();
        historyArea.setWrapText(true);
        historyArea.setEditable(false);

        // if-else statement that populates the history area with messages from the profile
        if (profile.getMessages().isEmpty()) {
            historyArea.setText("No message history available.");
        } 
        // if there are messages, format them with visual separation between timestamp and message
        else {
            historyArea.setText(formatMessageHistory(profile.getMessages()));
        }

        // creating the close button, adding its style, and setting its action
        Button close = new Button("Close");
        close.getStyleClass().add("button");
        close.setOnAction(e -> messageHistoryStage.close());

        // adding all components to the message history box
        messageHistoryBox.getChildren().addAll(title, historyArea, close);

        // Creating the scene and applying stylesheets
        Scene scene = new Scene(messageHistoryBox, 720, 520);
        scene.getStylesheets().add(getClass().getResource("/resources/styles/neuro-theme.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/resources/styles/popups.css").toExternalForm());

        // setting the scene and showing the message history stage
        messageHistoryStage.setScene(scene);
        messageHistoryStage.showAndWait();
    }

    // formatMessageHistory() method will take a List as input and format message history with visual separation between timestamp and content
    private String formatMessageHistory(List<String> messages) {
        // using StringBuilder to construct formatted history
        StringBuilder formatted = new StringBuilder();

        // iterating through each message
        for (int i = 0; i < messages.size(); i++) {
            String message = messages.get(i);

            // splitting message at the pipe character (|) to separate timestamp from content
            if (message.contains(" | ")) {
                String[] parts = message.split(" \\| ", 2);
                
                // if split was successful, format with visual separator
                if (parts.length == 2) {
                    // adding timestamp in brackets
                    formatted.append("[").append(parts[0].trim()).append("]");
                    formatted.append("\n");
                    
                    // adding message content with indentation
                    formatted.append("  → ").append(parts[1].trim());
                } else {
                    // if split failed, just add the original message
                    formatted.append(message);
                }
            } else {
                // if no pipe character, add message as-is
                formatted.append(message);
            }

            // adding separator line between messages (except after the last one)
            if (i < messages.size() - 1) {
                formatted.append("\n");
                formatted.append("─────────────────────────────────────────────────────");
                formatted.append("\n");
            }
        }

        // returning the formatted message history
        return formatted.toString();
    }

    // showCaregiverDashboard() method will take in a UserProfile object and will display the caregiver dashboard to the caregiver
    private void showCaregiverDashboard(UserProfile profile) {
        // creating new stage for caregiver dashboard, setting its modality and title
        Stage caregiverStage = new Stage();
        caregiverStage.initModality(Modality.APPLICATION_MODAL);
        caregiverStage.setTitle("Caregiver Dashboard for " + this.userName);
        // creating CaregiverDashboard object and building the scene
        CaregiverDashboard dashboard = new CaregiverDashboard(this.userName, profile);
        Scene scene = dashboard.buildScene();

        // using if-statements to apply stylesheets to the scene
        if (!scene.getStylesheets().contains(getClass().getResource("/resources/styles/neuro-theme.css").toExternalForm())) {
            scene.getStylesheets().add(getClass().getResource("/resources/styles/neuro-theme.css").toExternalForm());
        }
        if (!scene.getStylesheets().contains(getClass().getResource("/resources/styles/popups.css").toExternalForm())) {
            scene.getStylesheets().add(getClass().getResource("/resources/styles/popups.css").toExternalForm());
        }

        // calling caregiverStage's setScene() method, setting width and height, and showing the stage
        caregiverStage.setScene(scene);
        caregiverStage.setWidth(1100);
        caregiverStage.setHeight(780);
        caregiverStage.showAndWait();
    }

    // wrapInCard() method wraps content inside a styled "card" container for consistent spacing + hierarchy.
    private VBox wrapInCard(String title, javafx.scene.Node content) {
        // creating title label and getting its style
        Label t = new Label(title);
        t.getStyleClass().add("card-title");
        // creating separator
        Separator sep = new Separator();

        // creating the card vbox and setting its properties,
        VBox card = new VBox(10, t, sep, content);
        card.getStyleClass().add("card");
        VBox.setVgrow(content, Priority.ALWAYS);
        // returning the card
        return card;
    }

    // addProfileRow() method adds a "key : value" row to the profile grid.
    private int addProfileRow(GridPane grid, int row, String key, String value) {
        // creating key label as well as its styles
        Label k = new Label(key + ":");
        k.getStyleClass().add("muted");

        // creating value label and setting its properties
        Label v = new Label(value == null ? "" : value);
        v.setWrapText(true);

        // adding key and value to the grid and returning the next row index
        grid.add(k, 0, row);
        grid.add(v, 1, row);
        return row + 1;
    }

    // getScreen() method will return the BorderPane object that represents the user screen.
    public BorderPane getScreen() {
        return this.bp;
    }
}