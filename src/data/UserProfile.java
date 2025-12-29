package data;

import java.util.ArrayList;
import java.util.List;

public class UserProfile {
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

    // getter methods

    public String getName() {
        return this.userName;
    }

    public int getAge() {
        return this.age;
    }

    public ArrayList<String> getSensoryPref() {
        return this.sensoryPref;
    }

    public ArrayList<String> getCommunicationMethods() {
        return this.communicationMethods;
    }

    public ArrayList<String> getCalmingStrategies() {
        return this.calmingStrategies;
    }

    public ArrayList<String> getKnownTriggers() {
        return this.knownTriggers;
    }

    public ArrayList<String> getFavoriteActivities() {
        return this.favoriteActivities;
    }

    public String getPreferredLearning() {
        return this.preferredLearning;
    }

    public boolean getIsNonVerbal() {
        return this.isNonVerbal;
    }

    public String getNotes() {
        return this.extraNotes;
    }

    public String getPrimaryDiagnosis() {
        return this.primaryDiagnosis;
    }

    public String getProfileSummary() {
        String summary = "";

        summary += "Name: " + this.userName + "\n";
        summary += "Age: " + this.age + "\n";

        if (!communicationMethods.isEmpty()) {
            summary += "Communication Methods: " + String.join(", ", communicationMethods) + "\n";
        }

        if (!sensoryPref.isEmpty()) {
            summary += "Sensory Preferences: " + String.join(", ", sensoryPref) + "\n";
        }  

        return summary;
    }

    // setter methods

    public void setAge(int age) {
        this.age = age; 
    }

    public void setPrimaryDiagnosis(String primaryDiagnosis) {
        this.primaryDiagnosis = primaryDiagnosis;
    }

    public void setNotes(String notes) {
        this.extraNotes = notes;
    }

    public void setLearningPreference(String learningPref) {
        this.preferredLearning = learningPref;
    }

    public void setNonVerbal(boolean nonVerbal) {
        this.isNonVerbal = nonVerbal;
    }

    public void setCommunicationStyles(ArrayList<String> communicationMethods) {
        this.communicationMethods = communicationMethods;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    // adder methods
    
    public void addSensoryPref(String preferences) {
        sensoryPref.add(preferences);
    }

    public void addCommunicationMethod(String method) {
        this.communicationMethods.add(method);
    }

    public void addCalmingStrategy(String calmingStrategy) {
        this.calmingStrategies.add(calmingStrategy);
    }

    public void addTrigger(String trigger) {
        this.knownTriggers.add(trigger);
    }

    public void addFavoriteActivity(String activity) {
        this.favoriteActivities.add(activity);
    }

    public void addMessage(String message) {
        this.messages.add(message);
    }

    public List<String> getMessages() {
        return this.messages;
    }



    // calling viewMoreDetails will return the full profile details
    public String viewMoreDetails() {
        return this.toString();
    }

    // toSting method for full profile details
    @Override
    public String toString() {
        String result = "";
        
        result += "User: " + this.userName + "\n";
        
        if (age > 0) {
            result += "Age: " + this.age + "\n";
        }

        if (primaryDiagnosis != null && primaryDiagnosis.length() > 0) {
            result += "Primary Diagnosis: " + this.primaryDiagnosis + "\n";
        }
        
        if (!(sensoryPref.isEmpty())) {
            result += "Sensory Preferences:\n";
            for (int i = 0; i < sensoryPref.size(); i++) {
                result += " - " + sensoryPref.get(i) + "\n";
            }
        }

        if (!(communicationMethods.isEmpty())) {
            result += "Preferred Communication Methods :\n";
            for (int i = 0; i < communicationMethods.size(); i++) {
                result += " - " + communicationMethods.get(i) + "\n";
            }
        }

        if (!(knownTriggers.isEmpty())) {
            result += "Known Triggers :\n";
            for (int i = 0; i < knownTriggers.size(); i++) {
                result += " - " + knownTriggers.get(i) + "\n";
            }
        }

        if (!(calmingStrategies.isEmpty())) {
            result += "Calming Strategies :\n";
            for (int i = 0; i < calmingStrategies.size(); i++) {
                result += " - " + calmingStrategies.get(i) + "\n";
            }
        }

        if (!(favoriteActivities.isEmpty())) {
            result += "Favorite Activities :\n";
            for (int i = 0; i < favoriteActivities.size(); i++) {
                result += " - " + favoriteActivities.get(i) + "\n";
            }
        }

        if (extraNotes != null  && extraNotes.length() > 0) {
            result += "Notes: " + extraNotes + "\n";
        }

        return result; 
    }
}