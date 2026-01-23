/*   
    Vivaan Echambadi
    1/22/2026

    FileManager class will handle loading and saving UserProfile data to and from text files. It will provide methods 
    to read a user profile from a specified text file and to save a UserProfile object back to a text file. The class will include
    helper methods to parse different data types and handle formatting of the profile data.

*/

// importing packages
package data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// FileManager class definition
public class FileManager {
    
    // loadProfileFromTxt() method will take a filePath String as input and load a user profile from a text file
    public static UserProfile loadProfileFromTxt(String filePath) {
        // creating a new UserProfile instance with default name
        UserProfile profile = new UserProfile("Unknown");

        // reading the file line by line
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // variable to hold each line read from the file
            String line;

            // reading each line until the end of the file
            while ((line = br.readLine()) != null) {
                line = line.trim();

                // skipping empty lines
                if (line.isEmpty()) {
                    continue;
                }
                // normalizing line to start with a known key
                line = normalizeToFirstKnownKey(line);

                // parsing different attributes based on line prefixes
                if (line.startsWith("Name:")) {
                    // setting the name attribute of the profile
                    String name = valueAfter(line, "Name:");

                    // only set name if it is not empty
                    if (!name.isEmpty()) {
                        profile.setName(name);
                    }
                    continue;
                }

                // parsing age attribute
                if (line.startsWith("Age:")) {
                    // setting the age attribute of the profile
                    profile.setAge(parseIntSafe(valueAfter(line, "Age:"), -1));
                    continue;
                }

                // parsing primary diagnosis attribute
                if (line.startsWith("Primary Diagnosis:")) {
                    // setting the primary diagnosis attribute of the profile
                    profile.setPrimaryDiagnosis(valueAfter(line, "Primary Diagnosis:"));
                    continue;
                }

                // parsing sensory preferences attribute
                if (line.startsWith("Sensory Preferences:")) {
                    // adding each sensory preference to the profile using a for each loop
                    for (String item : splitSemicolonList(valueAfter(line, "Sensory Preferences:"))) {
                        profile.addSensoryPref(item);
                    }
                    continue;
                }

                // parsing communication methods attribute
                if (line.startsWith("Communication Methods:")) {
                    // adding each communication method to the profile using a for each loop
                    for (String item : splitSemicolonList(valueAfter(line, "Communication Methods:"))) {
                        profile.addCommunicationMethod(item);
                    }
                    continue;
                }

                // parsing known triggers attribute
                if (line.startsWith("Known Triggers:")) {
                    // adding each known trigger to the profile using a for each loop
                    for (String item : splitSemicolonList(valueAfter(line, "Known Triggers:"))) {
                        profile.addTrigger(item);
                    }
                    continue;
                }

                // parsing calming strategies attribute
                if (line.startsWith("Calming Strategies:")) {
                    // adding each calming strategy to the profile using a for each loop
                    for (String item : splitSemicolonList(valueAfter(line, "Calming Strategies:"))) {
                        profile.addCalmingStrategy(item);
                    }
                    continue;
                }

                // parsing favorite activities attribute
                if (line.startsWith("Favorite Activities:")) {
                    // adding each favorite activity to the profile using a for each loop
                    for (String item : splitSemicolonList(valueAfter(line, "Favorite Activities:"))) {
                        profile.addFavoriteActivity(item);
                    }
                    continue;
                }

                // parsing preferred learning style attribute
                if (line.startsWith("Preferred Learning Style:")) {
                    // setting the preferred learning style attribute of the profile
                    profile.setLearningPreference(valueAfter(line, "Preferred Learning Style:"));
                    continue;
                }

                // parsing nonverbal attribute
                if (line.startsWith("Nonverbal:")) {
                    // setting the nonverbal attribute of the profile
                    profile.setNonVerbal(parseBooleanLoose(valueAfter(line, "Nonverbal:")));
                    continue;
                }

                // parsing notes attribute
                if (line.startsWith("Notes:")) {
                    // setting the notes attribute of the profile
                    profile.setNotes(valueAfter(line, "Notes:"));
                    continue;
                }
                
                // parsing message attribute
                if (line.startsWith("Message:")) {
                    // adding the message to the profile
                    String msg = valueAfter(line, "Message:");
                    if (!msg.isEmpty()) profile.addMessage(msg);
                    continue;
                }
            }

        } 
        // handling IOException during file reading
        catch (IOException e) {
            System.out.println("Error loading profile from " + filePath + ": " + e.getMessage());
        }

        // returning the loaded UserProfile instance
        return profile;
    }

    // HELPER METHODS

    // valueAfter() method will return the substring after a specified prefix
    private static String valueAfter(String line, String prefix) {
        return line.substring(prefix.length()).trim();
    }

    // parseIntSafe() method will safely parse an integer from a string, returning a fallback value if parsing fails
    private static int parseIntSafe(String s, int fallback) {
        // attempting to parse the integer
        try {
            return Integer.parseInt(s.trim());
        } 
        // returning fallback value if parsing fails
        catch (Exception e) {
            return fallback;
        }
    }

    // saveProfileToTxt() method will save a UserProfile instance to a specified text file
    public static void saveProfileToTxt(UserProfile profile, String filePath) {
        // writing the profile data to the file
        try (java.io.PrintWriter out = new java.io.PrintWriter(new java.io.FileWriter(filePath))) {

            // writing each attribute of the profile to the file
            out.println("Name: " + profile.getName());
            out.println("Age: " + profile.getAge());
            out.println("Primary Diagnosis: " + profile.getPrimaryDiagnosis());
            out.println("Sensory Preferences: " + String.join("; ", profile.getSensoryPref()));
            out.println("Communication Methods: " + String.join("; ", profile.getCommunicationMethods()));
            out.println("Known Triggers: " + String.join("; ", profile.getKnownTriggers()));
            out.println("Calming Strategies: " + String.join("; ", profile.getCalmingStrategies()));
            out.println("Favorite Activities: " + String.join("; ", profile.getFavoriteActivities()));
            out.println("Preferred Learning Style: " + profile.getPreferredLearning());
            out.println("Nonverbal: " + profile.getIsNonVerbal());
            out.println("Notes: " + profile.getNotes());

            // writing each message of the profile to the file using a for each loop
            for (String msg : profile.getMessages()) {
                out.println("Message: " + msg);
            }
    
        } 
        // handling IOException during file writing
        catch (java.io.IOException e) {
            System.out.println("Error saving profile: " + e.getMessage());
        }
    }

    // parseBooleanLoose() method will take a String as input and parse a boolean value from a string in a loose manner
    private static boolean parseBooleanLoose(String s) {
        // if the string is null, return false
        if (s == null) {
            return false;
        }

        // trimming and converting the string to lowercase for comparison
        String v = s.trim().toLowerCase();
        return v.equals("true") || v.equals("yes") || v.equals("1");
    }

    // splitSemicolonList() method will split a semicolon-separated string into an array of trimmed strings
    private static String[] splitSemicolonList(String raw) {
        if (raw == null) {
            return new String[0];
        }
        raw = raw.trim();
        if (raw.isEmpty()) {
            return new String[0];
        }

        String[] parts = raw.split(";");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }
        return parts;
    }

    // If a line contains extra text before a known key, strip everything before the first known key.
    private static String normalizeToFirstKnownKey(String line) {
        // array of known keys to search for
        String[] keys = {
                "Name:", "Age:", "Primary Diagnosis:", "Sensory Preferences:", "Communication Methods:",
                "Known Triggers:", "Calming Strategies:", "Favorite Activities:", "Preferred Learning Style:",
                "Nonverbal:", "Notes:", "Message:"
        };

        // finding the earliest occurrence of any known key in the line
        int best = -1;

        // looping through each known key to find its index in the line using a for each loop
        for (String k : keys) {
            // index of the current key in the line
            int idx = line.indexOf(k);

            // updating best index if this key occurs earlier
            if (idx >= 0 && (best == -1 || idx < best)) {
                best = idx;
            }
        }
        
        // returning the substring starting from the earliest known key, or the original line if no key is found
        if (best >= 0) {
            // returning the substring starting from the earliest known key
            return line.substring(best).trim();
        } 
        // returning the original line if no known key is found
        else {
            // returning the original line
            return line;
        }
    }
}
