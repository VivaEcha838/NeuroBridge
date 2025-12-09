import java.util.*;
import main.java.data.UserProfile;
import main.java.business.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
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
    PhaseExtractor extractor;
    BorderPane bp;
    TextArea messageArea;

    public UserScreen(UserProfile profile) {
        this.userName = profile.userName;
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
        this.extractor = new PhaseExtractor(builder);
        this.bp = new BorderPane(); 

        MessagePanel messagePanel = new MessagePanel(extractor);
        bp.setCenter(messagePanel.loadTiles());
        VBox displayBox = createDisplayBox(profile);
    }

    public VBox createDisplayBox(UserProfile profile) {
        VBox box = new VBox();
        Label nameLabel = new Label("Name: " + profile.getName());
        messageArea = new TextArea();
        messageArea.setPrefHeight(100);
        Button showButton = new Button("Show Message");
        Button clearButton = new Button("Clear Message");
        Button saveButton = new Button("Save Message");

        // Button actions can be defined here
        box.getChildren().addAll(nameLabel, messageArea, showButton, clearButton, saveButton);
        return box;

        
    }

     public BorderPane getScreen() {
        return this.bp;
    }

    
}

