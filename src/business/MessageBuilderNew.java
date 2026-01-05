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
    private String historyFile;

    private static final String PHRASES_FILE = "resources/phrases.txt";
    private static final String HISTORY_FOLDER = "history/";

    public MessageBuilderNew() {
        this.currentMessage = "";
        this.selectedPhrases = new ArrayList<>();
        this.userName = "defaultUser";
        this.historyFile = HISTORY_FOLDER + userName + "_history.txt";
    }

    public MessageBuilderNew(String userName) {
        this.userName = userName;
        this.currentMessage = "";
        this.selectedPhrases = new ArrayList<>();
        this.historyFile = HISTORY_FOLDER + userName + "_history.txt";

        File folder = new File(HISTORY_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
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
            saveMessageToHistory(currentMessage.trim());
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

    private void saveMessageToHistory(String message) {
        if (userName == null || userName.isEmpty()) {
            return;
        }

        try (FileWriter fw = new FileWriter(historyFile, true)) {
            DateTimeFormatter dateTime = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            fw.write(dateTime.format(now) + " - " + message + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Error saving message to history: " + e.getMessage());
        }
    }

    public List<String> getMessageHistory() {
        List<String> history = new ArrayList<>();
        if (userName == null || userName.isEmpty()) {
            return history;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(historyFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                history.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading message history: " + e.getMessage());
        }
        return history;
    }

    public List<String> getMessageHistoryByDate(String date) {
        List<String> allHistory = getMessageHistory();
        List<String> filteredHistory = new ArrayList<>();

        for (String line: allHistory) {
            if (line.startsWith(date)) {
                filteredHistory.add(line);
            }
        }

        return filteredHistory;
    }

    public List<String> getRecentMessages(int count) {
        List<String> allHistory = getMessageHistory();
        int start = Math.max(0, allHistory.size() - count);
        return allHistory.subList(start, allHistory.size());
    }

    public void clearMessageHistory() {
        File file = new File(historyFile);
        if (file.exists()) {
            file.delete();
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        this.historyFile = HISTORY_FOLDER + userName + "_history.txt";
    }


}



