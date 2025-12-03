import java.util.*;

public class UserScreen {
    private String userName;
    private int age;
    private String primaryDiagnosis;
    private ArrayList<String> sensoryPref;
    private String extraNotes;

    public UserScreen(String userName) {
        this.userName = userName;
        this.age = -1;
        this.primaryDiagnosis = "";
        this.sensoryPref = new ArrayList<String>();
        this.extraNotes = "";
    }

    public String getName() {
        return this.userName;
    }

    public int getAge() {
        return this.age;
    }

    public void setPrimaryDiagnosis(String primaryDiagnosis) {
        this.primaryDiagnosis = primaryDiagnosis;
    }

    public void addSensoryPref(String preferences) {
        sensoryPref.add(preferences);
    }

    public ArrayList<String> getSensoryPref() {
        return this.sensoryPref;
    }

    public void setNotes(String notes) {
        this.extraNotes = notes;
    }

    public String getNotes() {
        return this.extraNotes;
    }

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

        if (extraNotes != null  && extraNotes.length() > 0) {
            result += "Notes: " + extraNotes + "\n";
        }

        return result; 
    }
}
