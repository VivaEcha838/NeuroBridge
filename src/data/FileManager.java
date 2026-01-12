package data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileManager {
    public static UserProfile loadProfileFromTxt(String filePath) {
        UserProfile profile = new UserProfile("Unknown");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                line = normalizeToFirstKnownKey(line);

                if (line.startsWith("Name:")) {
                    String name = valueAfter(line, "Name:");
                    if (!name.isEmpty()) {
                        profile.setName(name);
                    }
                    continue;
                }

                if (line.startsWith("Age:")) {
                    profile.setAge(parseIntSafe(valueAfter(line, "Age:"), -1));
                    continue;
                }

                if (line.startsWith("Primary Diagnosis:")) {
                    profile.setPrimaryDiagnosis(valueAfter(line, "Primary Diagnosis:"));
                    continue;
                }

                if (line.startsWith("Sensory Preferences:")) {
                    for (String item : splitSemicolonList(valueAfter(line, "Sensory Preferences:"))) {
                        profile.addSensoryPref(item);
                    }
                    continue;
                }

                if (line.startsWith("Communication Methods:")) {
                    for (String item : splitSemicolonList(valueAfter(line, "Communication Methods:"))) {
                        profile.addCommunicationMethod(item);
                    }
                    continue;
                }

                if (line.startsWith("Known Triggers:")) {
                    for (String item : splitSemicolonList(valueAfter(line, "Known Triggers:"))) {
                        profile.addTrigger(item);
                    }
                    continue;
                }

                if (line.startsWith("Calming Strategies:")) {
                    for (String item : splitSemicolonList(valueAfter(line, "Calming Strategies:"))) {
                        profile.addCalmingStrategy(item);
                    }
                    continue;
                }

                if (line.startsWith("Favorite Activities:")) {
                    for (String item : splitSemicolonList(valueAfter(line, "Favorite Activities:"))) {
                        profile.addFavoriteActivity(item);
                    }
                    continue;
                }

                if (line.startsWith("Preferred Learning Style:")) {
                    profile.setLearningPreference(valueAfter(line, "Preferred Learning Style:"));
                    continue;
                }

                if (line.startsWith("Nonverbal:")) {
                    profile.setNonVerbal(parseBooleanLoose(valueAfter(line, "Nonverbal:")));
                    continue;
                }

                if (line.startsWith("Notes:")) {
                    profile.setNotes(valueAfter(line, "Notes:"));
                    continue;
                }

                if (line.startsWith("Message:")) {
                    String msg = valueAfter(line, "Message:");
                    if (!msg.isEmpty()) profile.addMessage(msg);
                    continue;
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading profile from " + filePath + ": " + e.getMessage());
        }

        return profile;
    }

    // Helper Methods

    private static String valueAfter(String line, String prefix) {
        return line.substring(prefix.length()).trim();
    }

    private static int parseIntSafe(String s, int fallback) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return fallback;
        }
    }

    public static void saveProfileToTxt(UserProfile profile, String filePath) {
        try (java.io.PrintWriter out = new java.io.PrintWriter(new java.io.FileWriter(filePath))) {

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

            for (String msg : profile.getMessages()) {
                out.println("Message: " + msg);
            }
    
        } catch (java.io.IOException e) {
            System.out.println("Error saving profile: " + e.getMessage());
        }
    }

    private static boolean parseBooleanLoose(String s) {
        if (s == null) {
            return false;
        }
        String v = s.trim().toLowerCase();
        return v.equals("true") || v.equals("yes") || v.equals("1");
    }

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
        String[] keys = {
                "Name:", "Age:", "Primary Diagnosis:", "Sensory Preferences:", "Communication Methods:",
                "Known Triggers:", "Calming Strategies:", "Favorite Activities:", "Preferred Learning Style:",
                "Nonverbal:", "Notes:", "Message:"
        };

        int best = -1;
        for (String k : keys) {
            int idx = line.indexOf(k);
            if (idx >= 0 && (best == -1 || idx < best)) {
                best = idx;
            }
        }
        
        if (best >= 0) {
            return line.substring(best).trim();
        } else {
            return line;
        }
    }
}
