package presentation;
import java.util.*;

import business.*;
import data.UserProfile;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;


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
    MessageBuilderNew builder;
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
        this.builder = new MessageBuilderNew();
        this.extractor = new PhraseExtractor(profile, builder);
        this.bp = new BorderPane(); 

        MessagePanel messagePanel = new MessagePanel(extractor);
        bp.setCenter(messagePanel.loadTiles());
        VBox displayBox = createDisplayBox(profile);
        bp.setRight(displayBox);
    }

    private void showMessageHistory(UserProfile profile) {
            Stage messageHistoryStage = new Stage();
            messageHistoryStage.setTitle("Message History for " + this.userName);
            VBox messageHistoryBox = new VBox();
            if (profile.getMessages().isEmpty()) {
                messageHistoryBox.getChildren().add(new Text("No message history available."));
            } else {
                for (String msg : profile.getMessages()) {
                    messageHistoryBox.getChildren().add(new Text(msg));
                }
            }
            Scene scene = new Scene(messageHistoryBox, 400, 300);
            messageHistoryStage.setScene(scene);
            messageHistoryStage.showAndWait();
    }

    private void showCaregiverDashboard() {
            Stage caregiverStage = new Stage();
            caregiverStage.setTitle("Caregiver Dashboard for " + this.userName);
            caregiverStage.showAndWait();
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
        Label sensoryLabel = new Label("Sensory Preferences: " + String.join(", ", profile.getSensoryPref()));
        Label communicationLabel = new Label("Communication Methods: " + String.join(", ", profile.getCommunicationMethods()));
        Label calmingLabel = new Label("Calming Strategies: " + String.join(", ", profile.getCalmingStrategies()));
        Label triggersLabel = new Label("Known Triggers: " + String.join(", ", profile.getKnownTriggers()));
        Label activitiesLabel = new Label("Favorite Activities: " + String.join(", ", profile.getFavoriteActivities()));
        Label learningLabel = new Label("Preferred Learning Style: " + profile.getPreferredLearning());
        Label nonVerbalLabel = new Label("Non-Verbal: " + (profile.getIsNonVerbal() ? "Yes" : "No"));
        Label notesLabel = new Label("Additional Notes: " + profile.getNotes());

        Button messageHistoryButton = new Button("Message History");
        messageHistoryButton.setOnAction(e -> 
            showMessageHistory(profile)
        );    

        Button caregiverInfoButton = new Button("Caregiver Dashboard");
        caregiverInfoButton.setOnAction(e -> {
            showCaregiverDashboard();
        });

        // Button actions can be defined here
        box.getChildren().addAll(
            messageArea, showButton, clearButton, saveButton, 
            userNameLabel, ageLabel, diagnosisLabel, sensoryLabel, 
            communicationLabel, calmingLabel, triggersLabel, activitiesLabel, 
            learningLabel, nonVerbalLabel, notesLabel, messageHistoryButton, caregiverInfoButton
        );

        return box;

    }

    public BorderPane getScreen() {
        return this.bp;
    }

    
}
