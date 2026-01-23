/* 
    Vivaan Echambadi
    1/22/2026

    AISuggestionEngine() class will provide AI-based phrase suggestions based on user history.
    It will analyze the user's message history to suggest phrases that are frequently used, recently used, and contextually relevant based on the time of day.
    It will provide methods to get suggestions, calculate scores, and record when a suggestion is used. It will load 
    history from the user's profile file and rank suggestions based on calculated scores.

*/

// importing package
package business;

import java.util.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import presentation.TimeHelper;

// AISuggestionEngine class definition
public class AISuggestionEngine {
    // private attribute for username
    private String userName;
    private static final String PROFILES_FOLDER = "profiles/";
    private static final double FREQUENCY_WEIGHT = 0.6;
    private static final double RECENCY_WEIGHT = 0.4;
    private static final double TIME_BONUS = 10.0;

    // constructor to initialize AISuggestionEngine with a username
    public AISuggestionEngine(String userName) {
        // setting the username attribute
        this.userName = userName;
    }

    // getSuggestions() method will return a list of phrase suggestions based on user history
    public List<PhraseSuggestion> getSuggestions(int maxResults) {
        // loading user history
        List<HistoryEntry> historyLines = loadHistory();

        // checking if history is empty
        if (historyLines.isEmpty()) {
            return new ArrayList<>();
        }

        // extracting messages from history entries
        List<String> messages = new ArrayList<>();

        // iterating through history entries to get messages using a for loop
        for (int i = 0; i < historyLines.size(); i++) {
            messages.add(historyLines.get(i).getMessage());
        }

        // collecting unique phrases from messages using a set
        Set<String> uniqueMessages = new HashSet<>();

        // iterating through messages to extract unique phrases
        for (int i = 0; i < messages.size(); i++) {
            // Splitting messages into phrases based on periods
            String msg = messages.get(i);
            String[] parts = msg.split("\\.");

            // adding each trimmed phrase to the set of unique messages
            for (String part : parts) {
                String trimmed = part.trim();

                // adding non-empty trimmed phrases to the set
                if (!trimmed.isEmpty()) {
                    uniqueMessages.add(trimmed);
                }
            }
        }

        // calculating scores for each unique phrase and creating suggestions
        List<PhraseSuggestion> suggestions = new ArrayList<>();

        // iterating through unique phrases to calculate scores and create suggestions using a for each loop
        for (String phrase: uniqueMessages) {
            // calculating frequency score, recency score, total score, time bonus, and final score
            double freqScore = calculateFreqScore(phrase, messages);
            double recencyScore = calculateRecencyScore(phrase, messages);
            double totalScore = FREQUENCY_WEIGHT * freqScore + RECENCY_WEIGHT * recencyScore;
            double timeBonus = calculateTimeBonus(phrase, historyLines);
            double finalScore = totalScore + timeBonus;
            String reason = buildReasonString(phrase, messages, timeBonus);

            // adding the new PhraseSuggestion to the suggestions list
            suggestions.add(new PhraseSuggestion(phrase, finalScore, reason));
        }

        // sorting suggestions by score in descending order
        sortSuggestionsByScore(suggestions);

        // returning the top maxResults suggestions
        List<PhraseSuggestion> topSuggestions = new ArrayList<>();
        int count = Math.min(maxResults, suggestions.size());

        // iterating through suggestions to get the top results
        for (int i = 0; i < count; i++) {
            // adding the suggestion to the top suggestions list
            topSuggestions.add(suggestions.get(i));
        }

        // returning the list of top suggestions
        return topSuggestions;
    }
    
    // loadHistory() method will load the user's message history from their profile file
    private List<HistoryEntry> loadHistory() {
        // initializing an empty list for history entries
        List<HistoryEntry> history = new ArrayList<>();

        // attempting to load history from an external source (not implemented)
        if (!history.isEmpty()) {
            // return the history if successfully loaded
            return history;
        }

        // loading history from the user's profile file
        return loadHistoryFromProfile();
    }

    // loadHistoryFromProfile() method will read the user's profile file and extract message history
    private List<HistoryEntry> loadHistoryFromProfile() {
        // initializing an empty list for history entries
        List<HistoryEntry> history = new ArrayList<>();

        // constructing the file path for the user's profile
        String filePath = PROFILES_FOLDER + userName + ".txt";
        File file = new File(filePath);

        // checking if the file exists
        if (!file.exists()) {
            // returning empty history if file does not exist
            return history;
        }

        // reading the user's profile file to extract message history
        try {
            // creating a BufferedReader to read the file
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();

            // iterating through each line of the file
            while (line != null) {

                // checking if the line contains a message entry
                if (line.trim().startsWith("Message:")) {
                    // extracting the message text from after "Message:"
                    String fullMessage = line.substring(line.indexOf("Message:") + 8).trim();
                    
                    // checking if the message contains a timestamp separator (|)
                    String messageContent;
                    if (fullMessage.contains(" | ")) {
                        // splitting at the pipe to separate timestamp from message content
                        String[] parts = fullMessage.split(" \\| ", 2);
                        if (parts.length == 2) {
                            // extracting just the message content (after the timestamp)
                            messageContent = parts[1].trim();
                        } else {
                            // if split failed, use the full message
                            messageContent = fullMessage;
                        }
                    } else {
                        // if no timestamp separator, use the full message
                        messageContent = fullMessage;
                    }

                    // adding a new HistoryEntry with null timestamp and the extracted message content
                    if (!messageContent.isEmpty()) {
                        history.add(new HistoryEntry(null, messageContent));
                    }
                }

                // reading the next line from the file
                line = reader.readLine();
            }

            // closing the BufferedReader after reading the file
            reader.close();
        }
        // handling IOException during file reading
        catch (IOException e) {
            // printing the error message
            System.out.println("Error reading profile: " + e.getMessage());
        }

        // returning the list of history entries
        return history;
    }

    // calculateFreqScore() method will calculate the frequency score for a phrase based on its occurrences in messages
    private double calculateFreqScore(String phrase, List<String> messages) {
        // counting occurrences of the phrase in messages
        int count = 0;

        // iterating through messages to count occurrences
        for (int i = 0; i < messages.size(); i++) {
            // checking if the message contains the phrase
            if (messages.get(i).contains(phrase)) {
                // incrementing the count if the phrase is found
                count += 1;
            }
        }

        // finding the maximum phrase count among all phrases
        int maxCount = findMaxPhraseCount(messages);

        // calculating and returning the frequency score as a percentage
        if (maxCount == 0) {
            return 0;
        }

        // calculating the frequency score as a percentage of the maximum count using type casting
        double score = ((double) count / (double) maxCount) * 100;
        return score;
    }

    // findMaxPhraseCount() method will find the maximum occurrence count of any phrase in the messages
    private int findMaxPhraseCount(List<String> messages) {

        // using a map to count occurrences of each phrase
        Map<String, Integer> phraseCount = new HashMap<>();

        // iterating through messages to count occurrences of each phrase
        for (int i = 0; i < messages.size(); i++) {
            // getting the message and splitting it into phrases
            String message = messages.get(i);

            // Splitting messages into phrases based on periods
            String[] parts = message.split("\\.");

            // iterating through each phrase to count occurrences
            for (int j = 0; j < parts.length; j++) {
                String phrase = parts[j].trim();

                // updating the count for the phrase in the map
                if (!phrase.isEmpty()) {
                    // checking if the phrase is already in the map
                    if (phraseCount.containsKey(phrase)) {
                        // incrementing the count for the phrase
                        int oldCount = phraseCount.get(phrase);
                        phraseCount.put(phrase, oldCount + 1);
                    }
                    // adding the phrase to the map with an initial count of 1
                    else {
                        // adding the phrase with count 1
                        phraseCount.put(phrase, 1);
                    }
                }
            }
        }

        // finding the maximum count among all phrases
        int maxCount = 0;
        
        // iterating through the phrase count map to find the maximum count
        for (String phrase: phraseCount.keySet()) {
            // getting the count for the phrase
            int count = phraseCount.get(phrase);
            if (count > maxCount) {
                // updating maxCount if the current count is greater
                maxCount = count;
            }
        }

        // returning the maximum phrase count
        return maxCount;
    }

    // calculateRecencyScore() method will calculate the recency score for a phrase based on its recent usage in messages
    private double calculateRecencyScore(String phrase, List<String> messages) {
        // finding the last index of the phrase in messages
        int lastIndex = -1;
        
        // iterating through messages in reverse to find the last occurrence of the phrase
        for (int i = messages.size() - 1; i >= 0; i--) {
            // checking if the message contains the phrase
            if (messages.get(i).contains(phrase)) {
                // setting lastIndex to the current index and breaking the loop
                lastIndex = 1;
                break;
            }
        }

        // calculating and returning the recency score based on the position of the last occurrence
        if (lastIndex == -1) {
            return 0;
        }

        // calculating the position of the last occurrence as a fraction of total messages
        int totalMessages = messages.size();
        double position = (double) lastIndex / (double) totalMessages;
        
        // determining the recency score based on the position
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

    // calculateTimeBonus() method will calculate a time-based bonus score for a phrase based on its usage at the current time of day
    private double calculateTimeBonus(String phrase, List<HistoryEntry> historyEntries) {
        // getting the current time of day
        String currentTimeOfDay = TimeHelper.getTimeOfDay();

        // counting matches of the phrase used at the current time of day
        int match = 0;
        int total = 0;

        // iterating through history entries to count matches
        for (int i = 0; i < historyEntries.size(); i++) {
            // entry at the current index
            HistoryEntry entry = historyEntries.get(i);

            // checking if the entry message contains the phrase
            if (entry.getMessage().contains(phrase)) {
                // incrementing total count if phrase is found
                total += 1;
            }

            // checking if the entry timestamp hour corresponds to the current time of day
            if (entry.getHour() >= 0) {
                // getting the time of day from the entry hour
                String timeOfDayMsg = getTimeOfDayFromHour(entry.getHour());

                // checking if the time of day matches the current time of day
                if (timeOfDayMsg.equals(currentTimeOfDay)) {
                    // incrementing match count if time of day
                    match += 1;
                }
            }
        }

        if (total >= 2 && match >= (total * 0.6)) {
            // returning the time bonus if conditions are met
            return TIME_BONUS;
        }

        // returning 0.0 if no time bonus is applicable
        return 0.0;
    }

    // getTimeOfDayFromHour() method will return the time of day string based on the hour
    private String getTimeOfDayFromHour(int hour) {
        // determining the time of day based on the hour
        if (hour >= 5 && hour < 12) {
            // returning morning time
            return "morning";
        }   
        // determining afternoon time of day
        else if (hour >= 12 && hour < 17) {
            // returning afternoon time 
            return "afternoon";
        }
        // determining evening time of day
        else if (hour >= 17 && hour < 21) {
            // returning evening time
            return "evening";
        }
        // determining night time of day
        else {
            // returning night time
            return "night";
        }
    }

    // buildReasonString() method will construct a reason string explaining the suggestion score
    private String buildReasonString(String phrase, List<String> messages, double timeBonus) {
        // counting total occurrences of the phrase in messages
        int count = 0;

        // iterating through messages to count occurrences
        for (int i = 0; i < messages.size(); i++) {
            // checking if the message contains the phrase
            if (messages.get(i).contains(phrase)) {
                // incrementing the count if the phrase is found
                count += 1;
            }
        }

        // constructing the reason string based on counts and time bonus
        String reason = "Used " + count + " time";

        // pluralizing "time" if count is not 1
        if (count != 1) {
            reason += "s";
        }

        // counting recent occurrences of the phrase in the most recent messages, up to a threshold, to add to the reason
        int recentCount = 0;
        int threshold = Math.max(1, messages.size());
        int startIndex = Math.max(0, messages.size() - threshold);

        // iterating through recent messages to count occurrences
        for (int i = startIndex; i < messages.size(); i++) {
            // checking if the message contains the phrase
            if (messages.get(i).contains(phrase)) {
                // incrementing recent count if the phrase is found
                recentCount += 1;
            }
        }

        // appending recent usage information to the reason string
        if (recentCount > 0 && count > 1) {
            // adding recent count to reason if applicable
            reason += " (" + recentCount + " recently)";
        }

        // appending time bonus information to the reason string if applicable
        if (timeBonus > 0) {
            // adding time bonus reason
            reason += ", common at this time";
        }

        // returning the constructed reason string
        return reason;
    }

    // sortSuggestionsByScore() method will sort the list of phrase suggestions by their score in descending order
    private void sortSuggestionsByScore(List<PhraseSuggestion> suggestions) {
        // using bubble sort to sort suggestions by score in descending order using nested for loops
        for (int i = 0; i < suggestions.size(); i++) {
            for (int j = 0; j < suggestions.size() - 1; j++) {

                // getting the current and next suggestions
                PhraseSuggestion current = suggestions.get(i);
                PhraseSuggestion next = suggestions.get(j + 1);

                // swapping suggestions if the current score is less than the next score
                if (current.getScore() < next.getScore()) {
                    suggestions.set(j, next);
                    suggestions.set(j + 1, current);
                }
            }
        }
    }

    // recordSuggestionUsed() method will log when a suggestion is accepted by the user
    public void recordSuggestionUsed(String phrase) {
        // logging the accepted suggestion (implementation can be expanded as needed)
        System.out.println("Suggestion accepted: " + phrase);
    }
}