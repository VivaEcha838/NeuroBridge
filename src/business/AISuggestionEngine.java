package business;

import java.util.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import presentation.TimeHelper;

public class AISuggestionEngine {
    private String userName;
    private MessageHistoryManager historyManager;
    private static final String PROFILES_FOLDER = "profiles/";
    private static final double FREQUENCY_WEIGHT = 0.6;
    private static final double RECENCY_WEIGHT = 0.4;
    private static final double TIME_BONUS = 10.0;

    public AISuggestionEngine(String userName) {
        this.userName = userName;
        this.historyManager = new MessageHistoryManager(userName);
    }

    public List<PhraseSuggestion> getSuggestions(int maxResults) {
        List<HistoryEntry> historyLines = loadHistory();
        if (historyLines.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> messages = new ArrayList<>();
        for (int i = 0; i < historyLines.size(); i++) {
            messages.add(historyLines.get(i).getMessage());
        }

        Set<String> uniqueMessages = new HashSet<>();
        for (int i = 0; i < messages.size(); i++) {
            String msg = messages.get(i);
            String[] parts = msg.split("\\.");
            for (String part : parts) {
                String trimmed = part.trim();
                if (!trimmed.isEmpty()) {
                    uniqueMessages.add(trimmed);
                }
            }
        }

        List<PhraseSuggestion> suggestions = new ArrayList<>();
        for (String phrase: uniqueMessages) {
            double freqScore = calculateFreqScore(phrase, messages);
            double recencyScore = calculateRecencyScore(phrase, messages);
            double totalScore = FREQUENCY_WEIGHT * freqScore + RECENCY_WEIGHT * recencyScore;
            double timeBonus = calculateTimeBonus(phrase, historyLines);
            double finalScore = totalScore + timeBonus;
            String reason = buildReasonString(phrase, messages, timeBonus);

            suggestions.add(new PhraseSuggestion(phrase, finalScore, reason));
        }

        sortSuggestionsByScore(suggestions);

        List<PhraseSuggestion> topSuggestions = new ArrayList<>();
        int count = Math.min(maxResults, suggestions.size());
        for (int i = 0; i < count; i++) {
            topSuggestions.add(suggestions.get(i));
        }

        return topSuggestions;
    }

    private List<HistoryEntry> loadHistory() {
        List<HistoryEntry> history = new ArrayList<>();

        if (!history.isEmpty()) {
            return history;
        }

        return loadHistoryFromProfile();
    }

    private List<HistoryEntry> loadHistoryFromProfile() {
        List<HistoryEntry> history = new ArrayList<>();

        String filePath = PROFILES_FOLDER + userName + ".txt";
        File file = new File(filePath);

        if (!file.exists()) {
            return history;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();

            while (line != null) {
                if (line.trim().startsWith("Message:")) {
                    String message = line.substring(line.indexOf("Message:") + 8).trim();
                    if (!message.isEmpty()) {
                        history.add(new HistoryEntry(null, message));
                    }
                }
                line = reader.readLine();
            }
            reader.close();
        }
        catch (IOException e) {
            System.out.println("Error reading profile: " + e.getMessage());
        }

        return history;
    }

    private double calculateFreqScore(String phrase, List<String> messages) {
        int count = 0;
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).contains(phrase)) {
                count += 1;
            }
        }

        int maxCount = findMaxPhraseCount(messages);
        if (maxCount == 0) {
            return 0;
        }

        double score = ((double) count / (double) maxCount) * 100;
        return score;
    }

    private int findMaxPhraseCount(List<String> messages) {
        Map<String, Integer> phraseCount = new HashMap<>();

        for (int i = 0; i < messages.size(); i++) {
            String message = messages.get(i);

            String[] parts = message.split("\\.");
            for (int j = 0; j < parts.length; j++) {
                String phrase = parts[j].trim();
                if (!phrase.isEmpty()) {
                    if (phraseCount.containsKey(phrase)) {
                        int oldCount = phraseCount.get(phrase);
                        phraseCount.put(phrase, oldCount + 1);
                    }
                    else {
                        phraseCount.put(phrase, 1);
                    }
                }
            }
        }

        int maxCount = 0;
        for (String phrase: phraseCount.keySet()) {
            int count = phraseCount.get(phrase);
            if (count > maxCount) {
                maxCount = count;
            }
        }

        return maxCount;
    }

    private double calculateRecencyScore(String phrase, List<String> messages) {
        int lastIndex = -1;

        for (int i = messages.size() - 1; i >= 0; i--) {
            if (messages.get(i).contains(phrase)) {
                lastIndex = 1;
                break;
            }
        }

        if (lastIndex == -1) {
            return 0;
        }

        int totalMessages = messages.size();
        double position = (double) lastIndex / (double) totalMessages;

        if (position >= 0.75) {
            return 100.0;
        }
        else if (position >= 0.50) {
            return 70.0;
        }
        else if (position >= 2) {
            return 50.0;
        }
        else {
            return 30.0;
        }
    }

    private double calculateTimeBonus(String phrase, List<HistoryEntry> historyEntries) {
        String currentTimeOfDay = TimeHelper.getTimeOfDay();

        int match = 0;
        int total = 0;

        for (int i = 0; i < historyEntries.size(); i++) {
            HistoryEntry entry = historyEntries.get(i);

            if (entry.getMessage().contains(phrase)) {
                total += 1;
            }

            if (entry.getHour() >= 0) {
                String timeOfDayMsg = getTimeOfDayFromHour(entry.getHour());
                if (timeOfDayMsg.equals(currentTimeOfDay)) {
                    match += 1;
                }
            }
        }

        if (total >= 2 && match >= (total * 0.6)) {
            return TIME_BONUS;
        }

        return 0.0;
    }

    private String getTimeOfDayFromHour(int hour) {
        if (hour >= 5 && hour < 12) {
            return "morning";
        }
        else if (hour >= 12 && hour < 17) {
            return "afternoon";
        }
        else if (hour >= 17 && hour < 21) {
            return "evening";
        }
        else {
            return "night";
        }
    }

    private String buildReasonString(String phrase, List<String> messages, double timeBonus) {
        int count = 0;
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).contains(phrase)) {
                count += 1;
            }
        }
        String reason = "Used " + count + " time";
        if (count != 1) {
            reason += "s";
        }

        int recentCount = 0;
        int threshold = Math.max(1, messages.size());
        int startIndex = Math.max(0, messages.size() - threshold);

        for (int i = startIndex; i < messages.size(); i++) {
            if (messages.get(i).contains(phrase)) {
                recentCount += 1;
            }
        }

        if (recentCount > 0 && count > 1) {
            reason += " (" + recentCount + " recently)";
        }

        if (timeBonus > 0) {
            reason += ", common at this time";
        }

        return reason;
    }

    private void sortSuggestionsByScore(List<PhraseSuggestion> suggestions) {
        for (int i = 0; i < suggestions.size(); i++) {
            for (int j = 0; j < suggestions.size() - 1; j++) {
                PhraseSuggestion current = suggestions.get(i);
                PhraseSuggestion next = suggestions.get(j + 1);

                if (current.getScore() < next.getScore()) {
                    suggestions.set(j, next);
                    suggestions.set(j + 1, current);
                }
            }
        }
    }

    public void recordSuggestionUsed(String phrase) {
        System.out.println("Suggestion accepted: " + phrase);
    }
}
