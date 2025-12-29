package presentation;
import java.util.*;

import business.*;
import data.UserProfile;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class UserScreen {
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
    MessageBuilder builder;
    PhraseExtractor extractor;
    BorderPane bp;
    TextArea messageArea;

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
        this.builder = new MessageBuilder();
        this.extractor = new PhraseExtractor(profile, builder);
        this.bp = new BorderPane(); 

        MessagePanel messagePanel = new MessagePanel(extractor);
        bp.setCenter(messagePanel.loadTiles());
        VBox displayBox = createDisplayBox(profile);
        bp.setRight(displayBox);
    }

    public VBox createDisplayBox(UserProfile profile) {
        VBox box = new VBox();
        box.setPadding(new Insets(15));

        Label nameLabel = new Label("Name: " + profile.getName());
        nameLabel.setFont(Font.font(18));

        messageArea = new TextArea();
        messageArea.setEditable(false);
        messageArea.setPrefHeight(150);

        Button showButton = new Button("Show Message");

        showButton.setOnAction(e -> {
            String message = builder.showCurrentMessage();
            messageArea.setText(message);
            extractor.saveMessageToProfile(message);
        });

        Button clearButton = new Button("Clear Message");

        clearButton.setOnAction(e -> {
            builder.clearCurrentMessage();
            messageArea.clear();
        });

        Button saveButton = new Button("Save Message");
        saveButton.setOnAction(e -> extractor.saveProfileToDisk());

        Label userNameLabel = new Label("User: " + profile.getName());
        Label ageLabel = new Label("Age: " + profile.getAge());
        Label diagnosisLabel = new Label("Diagnosis: " + profile.getPrimaryDiagnosis());

        // Button actions can be defined here
        box.getChildren().addAll(messageArea, showButton, clearButton, saveButton, userNameLabel, ageLabel, diagnosisLabel);
        return box;

    }

    public BorderPane getScreen() {
        return this.bp;
    }

    
}
