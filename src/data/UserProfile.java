/* 
    Vivaan Echambadi
    1/22/2026

    UserProfile() class will represent a user profile with various attributes such as name, age, sensory preferences,
    communication methods, calming strategies, known triggers, favorite activities, learning preferences, and notes. It will provide getter and setter methods for these attributes,
    as well as methods to add individual preferences and a toString() method to display the full profile details.

*/

// importing packages
package data;

import java.util.ArrayList;
import java.util.List;

// UserProfile class definition
public class UserProfile {
    // private attributes for user profile details
    private String userName;
    private List<String> messages;
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
    
    // constructor to initialize user profile with a name, as well as default values for other attributes
    public UserProfile (String name) {
        this.userName = name;
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
        this.messages = new ArrayList<String>();
    }

    // GETTER METHODS

    // getName() method will get the username
    public String getName() {
        return this.userName;
    }
    
    // getAge() method will get the age of the user
    public int getAge() {
        return this.age;
    }

    // getSensoryPref() method will get the sensory preferences of the user
    public ArrayList<String> getSensoryPref() {
        return this.sensoryPref;
    }

    // getCommunicationMethods() method will get the preferred communication methods of the user
    public ArrayList<String> getCommunicationMethods() {
        return this.communicationMethods;
    }

    // getCalmingStrategies() method will get the best calming strategies for the user
    public ArrayList<String> getCalmingStrategies() {
        return this.calmingStrategies;
    }

    // getKnownTriggers() method will get the known triggers for the user
    public ArrayList<String> getKnownTriggers() {
        return this.knownTriggers;
    }

    // getFavoriteActivities() method will get the favorite activities of the user
    public ArrayList<String> getFavoriteActivities() {
        return this.favoriteActivities;
    }

    // getPreferredLearning() method will get the preferred learning style of the user
    public String getPreferredLearning() {
        return this.preferredLearning;
    }

    // getIsNonVerbal() method will check if the user is non-verbal
    public boolean getIsNonVerbal() {
        return this.isNonVerbal;
    }

    // getNotes() method will get any extra notes about the user
    public String getNotes() {
        return this.extraNotes;
    }

    // getPrimaryDiagnosis() method will get the primary diagnosis of the user
    public String getPrimaryDiagnosis() {
        return this.primaryDiagnosis;
    }

    // SETTER METHODS


    // setName() method will set the username
    public void setName(String name) {
        this.userName = name;
    }

    // setAge() method will set the age of the user
    public void setAge(int age) {
        this.age = age; 
    }

    // seCommunicationStyles() method will set the preferred communication methods of the user
    public void setCommunicationStyles(ArrayList<String> communicationMethods) {
        this.communicationMethods = communicationMethods;
    }

    // setLearningPreference() method will set the preferred learning style of the user
    public void setLearningPreference(String learningPref) {
        this.preferredLearning = learningPref;
    }

    // setNonVerbal() method will set if the user is non-verbal
    public void setNonVerbal(boolean nonVerbal) {
        this.isNonVerbal = nonVerbal;
    }

    // setNotes() method will set any extra notes about the user
    public void setNotes(String notes) {
        this.extraNotes = notes;
    }

    // setPrimaryDiagnosis() method will set the primary diagnosis of the user
    public void setPrimaryDiagnosis(String primaryDiagnosis) {
        this.primaryDiagnosis = primaryDiagnosis;
    }

    // setMessages() method will set the messages of the user
    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    // ADDER METHODS

    // addSensoryPref() method will add a sensory preference to the user's profile
    public void addSensoryPref(String preferences) {
        sensoryPref.add(preferences);
    }

    // addCommunicationMethod() method will add a preferred communication method to the user's profile
    public void addCommunicationMethod(String method) {
        this.communicationMethods.add(method);
    }

    // addCalmingStrategy() method will add a calming strategy to the user's profile
    public void addCalmingStrategy(String calmingStrategy) {
        this.calmingStrategies.add(calmingStrategy);
    }

    // addTrigger() method will add a known trigger to the user's profile
    public void addTrigger(String trigger) {
        this.knownTriggers.add(trigger);
    }

    // addFavoriteActivity() method will add a favorite activity to the user's profile
    public void addFavoriteActivity(String activity) {
        this.favoriteActivities.add(activity);
    }

    // addMessage() method will add a message to the user's profile
    public void addMessage(String message) {
        this.messages.add(message);
    }

    // getMessages() method will get the messages of the user
    public List<String> getMessages() {
        return this.messages;
    }

    // toString method for full profile details
    @Override
    public String toString() {
        // initializing result string
        String result = "";

        // appending user profile details to result string
        result += "User: " + this.userName + "\n";

        // appending age if it is set
        if (age > 0) {
            result += "Age: " + this.age + "\n";
        }

        // appending primary diagnosis if it is set
        if (primaryDiagnosis != null && primaryDiagnosis.length() > 0) {
            result += "Primary Diagnosis: " + this.primaryDiagnosis + "\n";
        }
        
        // appending sensory preferences if they are set
        if (!(sensoryPref.isEmpty())) {
            result += "Sensory Preferences:\n";
            for (int i = 0; i < sensoryPref.size(); i++) {
                result += " - " + sensoryPref.get(i) + "\n";
            }
        }

        // appending preferred communication methods if they are set
        if (!(communicationMethods.isEmpty())) {
            result += "Preferred Communication Methods :\n";
            for (int i = 0; i < communicationMethods.size(); i++) {
                result += " - " + communicationMethods.get(i) + "\n";
            }
        }

        // appending known triggers if they are set
        if (!(knownTriggers.isEmpty())) {
            result += "Known Triggers :\n";
            for (int i = 0; i < knownTriggers.size(); i++) {
                result += " - " + knownTriggers.get(i) + "\n";
            }
        }

        // appending calming strategies if they are set
        if (!(calmingStrategies.isEmpty())) {
            result += "Calming Strategies :\n";
            for (int i = 0; i < calmingStrategies.size(); i++) {
                result += " - " + calmingStrategies.get(i) + "\n";
            }
        }

        // appending favorite activities if they are set
        if (!(favoriteActivities.isEmpty())) {
            result += "Favorite Activities :\n";
            for (int i = 0; i < favoriteActivities.size(); i++) {
                result += " - " + favoriteActivities.get(i) + "\n";
            }
        }

        // appending preferred learning style if it is set
        if (extraNotes != null  && extraNotes.length() > 0) {
            result += "Notes: " + extraNotes + "\n";
        }

        // returning the full profile details as a string
        return result; 
    }
}