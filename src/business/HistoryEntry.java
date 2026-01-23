/* 
    Vivaan Echambadi
    1/22/2026

    HistoryEntry() class will represent a history entry with a timestamp and a message. 
    It will provide getter methods to access the timestamp and message as well as methods to get formatted date 
    and time strings, check if the entry is from today, calculate days since the entry, and 
    a toString() method for easy display. It will also handle cases where the timestamp may be null.

*/

// importing package
package business;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// HistoryEntry class definition
public class HistoryEntry {
    // private attributes for timestamp and message
    private LocalDateTime timestamp;
    private String message;

    // constructor to initialize history entry with a timestamp and a message
    public HistoryEntry(LocalDateTime timestamp, String message) {
        // setting the timestamp and message attributes
        this.timestamp = timestamp;
        this.message = message;
    }

    // getTimeStamp() method will return the timestamp of the history entry
    public LocalDateTime getTimeStamp() {
        return timestamp;
    }

    // getMessage() method will return the message of the history entry
    public String getMessage() {
        return message;
    }

    // getHour() method will return the year of the timestamp
    public int getHour() {
        if (timestamp == null) {
            // returning -1 if timestamp is null
            return -1;
        }

        // returning the hour from the timestamp
        return timestamp.getHour();
    }

    // getDateString() method will return the date string in "yyyy/MM/dd" format
    public String getDateString() {
        // checking if timestamp is null
        if (timestamp == null) {
            return null;
        }  

        // formatting the date to "yyyy/MM/dd"
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return timestamp.format(formatDate);
    }

    // getTimeString() method will return the time string in "HH:mm:ss" format
    public String getTimeString() {
        // checking if timestamp is null
        if (timestamp == null) {
            return null;
        }

        // formatting the time to "HH:mm:ss"
        DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("HH:mm:ss");

        // returning the formatted time string
        return timestamp.format(formatTime);
    }

    // hasTimestamp() method will return true if the timestamp is not null, false otherwise
    public boolean hasTimestamp() {
        // checking if timestamp is not null
        return timestamp != null;
    }

    // getDayOfWeek() method will return the day of the week for the timestamp
    public String getDayOfWeek() {
        // checking if timestamp is null
        if (timestamp == null) {
            // returning null if timestamp is null
            return null;
        }

        // formatting the day of the week
        DateTimeFormatter formatDay = DateTimeFormatter.ofPattern("EEEE");
        return timestamp.format(formatDay);
    }

    // isToday() method will return true if the timestamp is from today, false otherwise
    public boolean isToday() {
        // checking if timestamp is null
        if (timestamp == null) {
            // returning false if timestamp is null
            return false;
        }

        // getting the current date and comparing with the timestamp date
        LocalDateTime current = LocalDateTime.now();

        // returning true if the dates match, false otherwise
        return timestamp.toLocalDate().equals(current.toLocalDate());
    }

    // getDaysSince() method will return the number of days since the timestamp
    public int getDaysSince() {
        // checking if timestamp is null
        if (timestamp == null) {
            return -1;
        }

        // getting the current date
        LocalDateTime current = LocalDateTime.now();

        // calculating the number of days between the timestamp and current date
        long daysInBetween = java.time.temporal.ChronoUnit.DAYS.between(timestamp.toLocalDate(), current.toLocalDate());

        // returning the number of days as an integer
        return (int) daysInBetween;
    }

    // toString() method will return a string representation of the history entry
    public String toString() {
        // checking if timestamp is null
        if (timestamp == null) {
            // returning just the message if timestamp is null
            return message;
        }

        // formatting the timestamp and returning it with the message
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

        // returning the formatted string
        return timestamp.format(formatter) + " | " + message;
    }
}
