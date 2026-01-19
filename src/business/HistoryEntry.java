package business;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HistoryEntry {
    private LocalDateTime timestamp;
    private String message;

    public HistoryEntry(LocalDateTime timestamp, String message) {
        this.timestamp = timestamp;
        this.message = message;
    }

    public LocalDateTime getTimeStamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public int getHour() {
        if (timestamp == null) {
            return -1;
        }

        return timestamp.getHour();
    }

    public String getDateString() {
        if (timestamp == null) {
            return null;
        }

        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return timestamp.format(formatDate);
    }

    public String getTimeString() {
        if (timestamp == null) {
            return null;
        }

        DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("HH:mm:ss");
        return timestamp.format(formatTime);
    }

    public boolean hasTimestamp() {
        return timestamp != null;
    }

    public String getDayOfWeek() {
        if (timestamp == null) {
            return null;
        }
        DateTimeFormatter formatDay = DateTimeFormatter.ofPattern("EEEE");
        return timestamp.format(formatDay);
    }

    public boolean isToday() {
        if (timestamp == null) {
            return false;
        }

        LocalDateTime current = LocalDateTime.now();
        return timestamp.toLocalDate().equals(current.toLocalDate());
    }

    public int getDaysSince() {
        if (timestamp == null) {
            return -1;
        }

        LocalDateTime current = LocalDateTime.now();

        long daysInBetween = java.time.temporal.ChronoUnit.DAYS.between(timestamp.toLocalDate(), current.toLocalDate());

        return (int) daysInBetween;
    }

    public String toString() {
        if (timestamp == null) {
            return message;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        return timestamp.format(formatter) + " | " + message;
    }
}
