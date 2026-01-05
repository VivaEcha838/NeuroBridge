package presentation;

import java.util.*;
import business.*;
import data.UserProfile;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class UserScreenNew {
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
    MessageBuilderNew builder;
    PhraseExtractor extractor;
    BorderPane bp;
    TextArea messageArea;

    public UserScreenNew(UserProfile profile) {
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
        this.builder = new MessageBuilderNew(profile.getName());
        this.extractor = new PhraseExtractor(profile, builder);
        this.bp = new BorderPane(); 

        MessagePanel messagePanel = new MessagePanel(extractor);
        bp.setCenter(messagePanel.loadTiles());
        VBox displayBox = createDisplayBox(profile);
        bp.setRight(displayBox);
    }

    public VBox createDisplayBox(UserProfile profile) {
        VBox box = new VBox(10);
        box.setPadding(new Insets(15));

        Label nameLabel = new Label("User: " + profile.getName());
        nameLabel.setFont(Font.font(18));

        messageArea = new TextArea();
        messageArea.setEditable(false);
        messageArea.setPrefHeight(200);
        messageArea.setWrapText(true);
        messageArea.setPromptText("Your message will appear here: ");

        VBox buttonBox = new VBox(10);
        Button showMessageBtn = new Button("Show Message");
        showMessageBtn.setPrefWidth(200);
        showMessageBtn.setOnAction(e -> {
            String message = builder.showCurrentMessage();
            messageArea.setText(message);
        });

        Button clearButton = new Button("Clear Message");
        clearButton.setPrefWidth(200);
        clearButton.setOnAction(e -> {
            builder.clearCurrentMessage();
            messageArea.clear();
        });

        Button undoButton = new Button("Undo Last Phrase");
        undoButton.setPrefWidth(200);
        undoButton.setOnAction(e -> {
            builder.removeLastPhrase();
            String updatedMessage = builder.updateCurrentMessage();
            messageArea.setText(updatedMessage);
        });

        Button historyButton = new Button("View Message History");
        historyButton.setPrefWidth(200);
        historyButton.setStyle(" -fx-background-color: #4CAF50; -fx-text-fill: white; ");
        historyButton.setOnAction(e -> {
            MessageHistoryWindow historyViewer = new MessageHistoryWindow(userName);
            historyViewer.show();
        });

        Button saveButton = new Button("Save Profile");
        saveButton.setPrefWidth(200);
        saveButton.setOnAction(e -> {
            extractor.saveProfiletoDisk();
            showInfo("Saved", "Profile saved successfully.");
        });

        buttonBox.getChildren().addAll(showMessageBtn, clearButton, undoButton, historyButton, saveButton);

        Separator separator = new Separator();

        Label userInfoLabel = new Label("Profile Information");
        userInfoLabel.setFont(Font.font(16));
        userInfoLabel.setStyle("-fx-font-weight: bold;");

        Label userNameLabel = new Label("User: " + profile.getName());
        Label ageLabel = new Label("Age: " + profile.getAge());
        Label diagnosisLabel = new Label("Diagnosis: " + profile.getPrimaryDiagnosis());

        box.getChildren().addAll(messageArea, buttonBox, separator, userInfoLabel, userNameLabel, ageLabel, diagnosisLabel);
        return box;
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public BorderPane getScreen() {
        return this.bp;
    }
}
