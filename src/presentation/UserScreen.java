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
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class UserScreen {
    // creating private properties
    private String userName;
    private int age;
    private String primaryDiagnosis;
    private ArrayList<String> sensoryPref;
    private ArrayList<String> communicationMethods;
    private ArrayList<String> calmingStrategies;
    private ArrayList<String> knownTriggers;
    private ArrayList<String> favoriteActivities;
    private String preferredLearning;
    private boolean isNonVerbal;
    private String extraNotes;

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
        this.age = -1;
        this.primaryDiagnosis = "";
        this.sensoryPref = new ArrayList<String>();
        this.communicationMethods = new ArrayList<String>();
        this.calmingStrategies = new ArrayList<String>();
        this.knownTriggers = new ArrayList<String>();
        this.favoriteActivities = new ArrayList<String>();
        this.preferredLearning = "";
        this.isNonVerbal = false;
        this.extraNotes = "";

        // business logic objects
        this.builder = new MessageBuilderNew();
        this.extractor = new PhraseExtractor(profile, builder);

        // root pane for the screen
        this.bp = new BorderPane();
        this.bp.getStyleClass().add("app-root");
        bp.setTop(buildHeader(profile));
        bp.setCenter(buildMain(profile));
    }

    private HBox buildHeader(UserProfile profile) {
        Label title = new Label("NeuroBridge");
        title.getStyleClass().add("app-title");

        Label subtitle = new Label("User: " + profile.getName() + "   •   Age: " + profile.getAge());
        subtitle.getStyleClass().add("app-subtitle");

        VBox textBlock = new VBox(4, title, subtitle);
        textBlock.setAlignment(Pos.CENTER_LEFT);

        HBox header = new HBox(textBlock);
        header.getStyleClass().add("app-header");
        return header;
    }

    private SplitPane buildMain(UserProfile profile) {
        MessagePanel messagePanel = new MessagePanel(extractor);
        VBox tilesCard = wrapInCard("Phrases", messagePanel.loadTiles());
        tilesCard.setMinWidth(640);

        // Right side "card stack"
        VBox messageCard = buildMessageCard(profile);
        VBox profileCard = buildProfileCard(profile);
        VBox caregiverCard = buildCaregiverToolsCard(profile);

        VBox rightStack = new VBox(14, messageCard, profileCard, caregiverCard);
        rightStack.setPadding(new Insets(14));
        rightStack.setFillWidth(true);

        SplitPane split = new SplitPane(tilesCard, rightStack);
        split.setDividerPositions(0.52);
        split.setPadding(new Insets(14));
        return split;
    }

    // Message card: Message preview area + action buttons
    private VBox buildMessageCard(UserProfile profile) {
        messageArea = new TextArea();
        messageArea.setEditable(false);
        messageArea.setWrapText(true);
        messageArea.setPrefHeight(140);


        Button showButton = new Button("Show Message");
        showButton.getStyleClass().addAll("button", "primary");
        showButton.setOnAction(e -> {
            String message = builder.showCurrentMessage();
            messageArea.setText(message);
            extractor.saveMessageToProfile(message);
        });

        Button clearButton = new Button("Clear Message");
        clearButton.getStyleClass().addAll("button", "danger");
        clearButton.setOnAction(e -> {
            builder.clearCurrentMessage();
            messageArea.clear();
        });

        Button saveButton = new Button("Save Message");
        saveButton.getStyleClass().add("button");
        saveButton.setOnAction(e -> extractor.saveProfileToDisk());

        HBox actions = new HBox(10, showButton, clearButton, saveButton);
        actions.setAlignment(Pos.CENTER_LEFT);

        VBox content = new VBox(10, messageArea, actions);
        return wrapInCard("Message", content);
    }

    private VBox buildProfileCard(UserProfile profile) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);

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

        ColumnConstraints c2 = new ColumnConstraints();
        c2.setHgrow(Priority.ALWAYS);

        grid.getColumnConstraints().addAll(c1, c2);

        return wrapInCard("Profile", grid);
    }

    // buildCaregiverToolsCard() buttons will open secondary windows
    private VBox buildCaregiverToolsCard(UserProfile profile) {
        Button messageHistoryButton = new Button("Message History");
        messageHistoryButton.getStyleClass().addAll("button", "primary");
        messageHistoryButton.setOnAction(e -> showMessageHistory(profile));

        Button caregiverInfoButton = new Button("Caregiver Dashboard");
        caregiverInfoButton.getStyleClass().add("button");
        caregiverInfoButton.setOnAction(e -> showCaregiverDashboard(profile));

        HBox row = new HBox(10, messageHistoryButton, caregiverInfoButton);
        row.setAlignment(Pos.CENTER_LEFT);

        return wrapInCard("Caregiver Tools", row);
    }

    // showMessageHistory() method will take in a UserProfile Object and will display the message history
    private void showMessageHistory(UserProfile profile) {
        Stage messageHistoryStage = new Stage();
        messageHistoryStage.initModality(Modality.APPLICATION_MODAL);
        messageHistoryStage.setTitle("Message History for " + this.userName);

        VBox messageHistoryBox = new VBox(12);
        messageHistoryBox.getStyleClass().add("popup-root");

        Label title = new Label("Message History");
        title.getStyleClass().add("popup-title");

        TextArea historyArea = new TextArea();
        historyArea.setWrapText(true);
        historyArea.setEditable(false);

        if (profile.getMessages().isEmpty()) {
            historyArea.setText("No message history available.");
        } else {
            historyArea.setText(String.join("\n", profile.getMessages()));
        }

        Button close = new Button("Close");
        close.getStyleClass().add("button");
        close.setOnAction(e -> messageHistoryStage.close());

        messageHistoryBox.getChildren().addAll(title, historyArea, close);

        Scene scene = new Scene(messageHistoryBox, 720, 520);

        scene.getStylesheets().add(getClass().getResource("/resources/styles/neuro-theme.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/resources/styles/popups.css").toExternalForm());

        messageHistoryStage.setScene(scene);
        messageHistoryStage.showAndWait();
    }

    // caregiver dashboard window
    private void showCaregiverDashboard(UserProfile profile) {
        Stage caregiverStage = new Stage();
        caregiverStage.initModality(Modality.APPLICATION_MODAL);
        caregiverStage.setTitle("Caregiver Dashboard for " + this.userName);

        CaregiverDashboard dashboard = new CaregiverDashboard(this.userName, profile);

        Scene scene = dashboard.buildScene();

        if (!scene.getStylesheets().contains(getClass().getResource("/resources/styles/neuro-theme.css").toExternalForm())) {
            scene.getStylesheets().add(getClass().getResource("/resources/styles/neuro-theme.css").toExternalForm());
        }
        if (!scene.getStylesheets().contains(getClass().getResource("/resources/styles/popups.css").toExternalForm())) {
            scene.getStylesheets().add(getClass().getResource("/resources/styles/popups.css").toExternalForm());
        }

        caregiverStage.setScene(scene);
        caregiverStage.setWidth(1100);
        caregiverStage.setHeight(780);
        caregiverStage.showAndWait();
    }

    // wrapInCard() wraps content inside a styled "card" container for consistent spacing + hierarchy.
    private VBox wrapInCard(String title, javafx.scene.Node content) {
        Label t = new Label(title);
        t.getStyleClass().add("card-title");

        Separator sep = new Separator();

        VBox card = new VBox(10, t, sep, content);
        card.getStyleClass().add("card");
        VBox.setVgrow(content, Priority.ALWAYS);
        return card;
    }

    // addProfileRow() method adds a "key : value" row to the profile grid.
    private int addProfileRow(GridPane grid, int row, String key, String value) {
        Label k = new Label(key + ":");
        k.getStyleClass().add("muted");

        Label v = new Label(value == null ? "" : value);
        v.setWrapText(true);

        grid.add(k, 0, row);
        grid.add(v, 1, row);
        return row + 1;
    }

    public BorderPane getScreen() {
        return this.bp;
    }
}
