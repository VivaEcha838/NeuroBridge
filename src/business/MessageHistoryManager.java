package business;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MessageHistoryManager {
    private String userName;
    private String historyFilePath;
    private static final String HISTORY_FOLDER = "history/";
    private static final String SEPARATOR = " | ";
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public MessageHistoryManager(String userName) {
        this.userName = userName;
        this.historyFilePath = HISTORY_FOLDER + userName + "_history.txt";

        File folder = new File(HISTORY_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    public boolean saveMessage(String message) {
        if (message == null || message.trim().isEmpty()) {
            return false;
        }

        try {
            FileWriter writer = new FileWriter(historyFilePath, true);
            LocalDateTime now = LocalDateTime.now();
            String time = now.format(FORMAT);

            writer.write(time + SEPARATOR + message.trim());
            writer.write(System.lineSeparator());

            writer.close();
            return true;
        }
        catch (IOException e) {
            System.out.println("Error saving message to history: " + e.getMessage());
            return false;
        }
    }

    public List<HistoryEntry> loadHistory() {
        List<HistoryEntry> history = new ArrayList<>();

        File file = new File(historyFilePath);
        if (!file.exists()) {
            return history;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();

            while (line != null) {
                HistoryEntry entry = parseLine(line);
                if (entry != null) {
                    history.add(entry);
                }
                line = reader.readLine();
            }

            reader.close();
        }
        catch (IOException e) {
            System.out.println("Error reading history: " + e.getMessage());
        }

        return history;
    }


    public List<String> loadMessages() {
        List<String> messages = new ArrayList<>();
        List<HistoryEntry> history = loadHistory();

        for (int i = 0; i < history.size(); i++) {
            messages.add(history.get(i).getMessage());
        }

        return messages;
    }

    private HistoryEntry parseLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        int sepIndex = line.indexOf(SEPARATOR);
        if (sepIndex < 0) {
            return new HistoryEntry(null, line.trim());
        }

        String timeStampString = line.substring(0, sepIndex).trim();
        String message = line.substring(sepIndex + SEPARATOR.length()).trim();

        LocalDateTime timestamp = null;
        try {
            timestamp = LocalDateTime.parse(timeStampString, FORMAT);
        }
        catch (Exception e) {
            System.out.println("Warning: Could not parse timestamp");
        }

        return new HistoryEntry(timestamp, message);
    }

    public boolean clearHistory() {
        File file = new File(historyFilePath);
        if (file.exists()) {
            return file.delete();
        }

        return true;
    }

    public int getMessageCount() {
        return loadHistory().size();
    }

    public boolean historyExists() {
        File file = new File(historyFilePath);
        return file.exists();
    }

    public List<String> getRecentMessages(int hours) {
        List<String> recentMessages = new ArrayList<>();
        List<HistoryEntry> history = loadHistory();

        LocalDateTime cutoff = LocalDateTime.now().minusHours(hours);

        for (int i = 0; i < history.size(); i++) {
            HistoryEntry entry = history.get(i);
            if (entry.getTimeStamp() != null && entry.getTimeStamp().isAfter(cutoff)) {
                recentMessages.add(entry.getMessage());
            }
        }

        return recentMessages;
    }
}
