package business;

import java.util.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import presentation.MainApp;

public class MessageBuilderNew {
    private String currentMessage;
    private List<String> selectedPhrases;
    private String userName;
    private MessageHistoryManager historyManager;

    private static final String PHRASES_FILE = "resources/phrases.txt";

    public MessageBuilderNew() {
        this.currentMessage = "";
        this.selectedPhrases = new ArrayList<>();
        this.userName = "defaultUser";
        this.historyManager = new MessageHistoryManager(userName);
    }

    public MessageBuilderNew(String userName) {
        this.userName = userName;
        this.currentMessage = "";
        this.selectedPhrases = new ArrayList<>();
        this.historyManager = new MessageHistoryManager(userName);
    }

    public void addButtonPhrase(String phrase) {
        this.selectedPhrases.add(phrase);
        updateCurrentMessage();
    }

    public static void loadPhrases() {
        try (BufferedReader br = new BufferedReader(new FileReader(PHRASES_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    MainApp.phraseMeaningDictMap.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading phrases: " + e.getMessage());
        }
    }

    public String showCurrentMessage() {
        currentMessage = "";
        for (int i = 0; i < this.selectedPhrases.size(); i++) {
            if (MainApp.phraseMeaningDictMap.containsKey(this.selectedPhrases.get(i))) {
                currentMessage += MainApp.phraseMeaningDictMap.get(this.selectedPhrases.get(i)) + " ";
            } else {
                currentMessage += this.selectedPhrases.get(i) + " ";
            }
        }
        
        if (!currentMessage.trim().isEmpty()) {
            historyManager.saveMessage(currentMessage.trim());
        }
        this.selectedPhrases.clear();
        return currentMessage.trim();
    }

    public void clearCurrentMessage() {
        this.currentMessage = "";
        this.selectedPhrases.clear();
    }

    public void removeLastPhrase() {
        if (!this.selectedPhrases.isEmpty()) {
            this.selectedPhrases.remove(this.selectedPhrases.size() - 1);
            updateCurrentMessage();
        }
    }

    public String updateCurrentMessage() {
        StringBuilder messageBuilder = new StringBuilder();

        for (int i = 0; i < this.selectedPhrases.size(); i++) {
            if (MainApp.phraseMeaningDictMap.containsKey(this.selectedPhrases.get(i))) {
                messageBuilder.append(MainApp.phraseMeaningDictMap.get(this.selectedPhrases.get(i))).append(" ");

                if (i < this.selectedPhrases.size() - 1) {
                    messageBuilder.append(" ");
                }
            }
        }
        this.currentMessage = messageBuilder.toString().trim();
        return this.currentMessage;
    }

    public List<String> getSelectedPhrases() {
        return new ArrayList<>(this.selectedPhrases);
    }

    public int getSelectedPhrasesCount() {
        return this.selectedPhrases.size();
    }

    public List<String> getMessageHistory() {
        List<String> history = new ArrayList<>();
        List<HistoryEntry> entries = historyManager.loadHistory();

        for (int i = 0; i < entries.size(); i++) {
            history.add(entries.get(i).toString());
        }

        return history;
    }

    public List<String> getMessagesOnly() {
        return historyManager.loadMessages();
    }

    public List<String> getMessageHistoryByDate(String date) {
        List<String> history = getMessageHistory();
        List<String> filteredHistory = new ArrayList<>();

        for (String line: history) {
            if (line.startsWith(date)) {
                filteredHistory.add(line);
            }
        }

        return filteredHistory;
    }

    public List<String> getRecentMessages(int count) {
        List<String> history = getMessageHistory();
        int start = Math.max(0, history.size() - count);
        return history.subList(start, history.size());
    }

    public List<String> getRecentMessagesByHour(int hours) {
        return historyManager.getRecentMessages(hours);
    }

    public void clearMessageHistory() {
        historyManager.clearHistory();
    }

    public int getHistoryCount() {
        return historyManager.getMessageCount();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        this.historyManager = new MessageHistoryManager(userName);
    }

    public MessageHistoryManager getHistoryManager() {
        return historyManager;
    }
}



