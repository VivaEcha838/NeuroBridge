/* 
    Vivaan Echambadi
    1/6/2026

    UserScreen class represents the user interface for displaying user profile information. UserScreen class
    will contain methods to create the display box with user details, show message history, and caregiver dashboard.
    The class will also contain more buttons, such as the Show Message, Clear Message, and Save Message buttons. These
    buttons will interact with the MessageBuilderNew and PhraseExtractor classes to manage message creation and saving

*/

// importing packages
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
    MessageBuilderNew builder;
    PhraseExtractor extractor;
    BorderPane bp;
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
        this.builder = new MessageBuilderNew();
        this.extractor = new PhraseExtractor(profile, builder);
        this.bp = new BorderPane(); 

        // creating the message panel and setting it to the center of the border pane
        MessagePanel messagePanel = new MessagePanel(extractor);
        bp.setCenter(messagePanel.loadTiles());
        VBox displayBox = createDisplayBox(profile);
        bp.setRight(displayBox);
    }

    // showMessageHistory() method will take in a UserProfile Object and will display the message history
    private void showMessageHistory(UserProfile profile) {
            Stage messageHistoryStage = new Stage();
            messageHistoryStage.setTitle("Message History for " + this.userName);
            VBox messageHistoryBox = new VBox();
            //messageHistoryBox.getStyleClass().add("popup");
            if (profile.getMessages().isEmpty()) {
                messageHistoryBox.getChildren().add(new Text("No message history available."));
            } else {
                for (String msg : profile.getMessages()) {
                    messageHistoryBox.getChildren().add(new Text(msg));
                }
            }
            Scene scene = new Scene(messageHistoryBox, 400, 300);
            scene.getStylesheets().add(getClass().getResource("/resources/styles/popups.css").toExternalForm());
            messageHistoryStage.setScene(scene);
            messageHistoryStage.showAndWait();
    }

    private void showCaregiverDashboard() {
            Stage caregiverStage = new Stage();
            caregiverStage.setTitle("Caregiver Dashboard for " + this.userName);
            
            CaregiverDashboard dashboard = new CaregiverDashboard(this.userName, null);
            Scene scene = dashboard.buildScene();
            caregiverStage.setScene(scene);
            caregiverStage.show();
    }

    public VBox createDisplayBox(UserProfile profile) {
        VBox box = new VBox();
        box.setPadding(new Insets(15));

        Label nameLabel = new Label("Name: " + profile.getName());
        //nameLabel.setFont(Font.font(18));
        nameLabel.getStyleClass().add("header-label");

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

        Label abc = new Label("User Name");
        abc.getStyleClass().add("header-label");

        Label userNameLabel = new Label(profile.getName());
        userNameLabel.getStyleClass().add("label");
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
        //messageHistoryButton.getStyleClass().add("button");
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
