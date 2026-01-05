package data;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private static final String PROFILE_DIR = "profiles/";

    // Load user profile from TXT file
    public static UserProfile loadProfile(String username) {
        //System.out.println(username);
        File file = new File(PROFILE_DIR + username + ".txt");
        //System.out.println(file.getName());
        if (!file.exists()) {
            return null; // profile does not exist
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;
            List<String> messages = new ArrayList<>();
            UserProfile profile = new UserProfile(username);
            
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Name: ")) {
                    profile.setName(line.substring("Name: ".length()));
                } else if (line.startsWith("Age: ")) {
                    profile.setAge(Integer.parseInt(line.substring("Age: ".length())));
                } else if (line.startsWith("Primary Diagnosis: ")) {
                    profile.setPrimaryDiagnosis(line.substring("Primary Diagnosis: ".length()));
                } else if (line.startsWith("Sensory Preferences: ")) {
                    profile.addSensoryPref(line.substring("Sensory Preferences: ".length()));
                } else if (line.startsWith("Communication Methods: ")) {
                    profile.addCommunicationMethod(line.substring("Communication Methods: ".length()));
                } else if (line.startsWith("Known Triggers: ")) {
                    profile.addTrigger(line.substring("Known Triggers: ".length()));
                } else if (line.startsWith("Calming Strategies: ")) {
                    profile.addCalmingStrategy(line.substring("Calming Strategies: ".length()));
                } else if (line.startsWith("Favorite Activities: ")) {
                    profile.addFavoriteActivity(line.substring("Favorite Activities: ".length()));
                } else if (line.startsWith("Preferred Learning Style: ")) {
                    profile.setLearningPreference(line.substring("Preferred Learning Style: ".length()));
                } else if (line.startsWith("Nonverbal: ")) {
                    profile.setNonVerbal(Boolean.parseBoolean(line.substring("Nonverbal: ".length())));
                } else if (line.startsWith("Notes: ")) {
                    profile.setNotes(line.substring("Notes: ".length()));
                } else if (line.startsWith("Message: ")) {
                    messages.add(line.substring("Message: ".length()));
                }    
            }
            //UserProfile profile = new UserProfile(name);
            
            profile.setMessages(messages);
            return profile;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Save user profile to TXT file
    public static void saveProfile(UserProfile profile) {

        File dir = new File(PROFILE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(PROFILE_DIR + profile.getName() + ".txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {

            writer.write("Name:" + profile.getName());
            writer.newLine();

            for (String message : profile.getMessages()) {
                writer.write("Message: " + message);
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}