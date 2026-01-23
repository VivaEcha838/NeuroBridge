/* 
    Vivaan Echambadi
    1/22/2026

    MessageGarden() class will represent a user's message garden, tracking the number of messages sent each day
    and determining the growth stage of the garden based on message activity. It will provide methods to
    record messages, get the current stage of the garden, and load/save the garden state to a file. It will also
    handle daily resets of message counts.

*/

// importing package
package business;

import java.io.*;
import java.time.LocalDate;

// MessageGarden class definition
public class MessageGarden {
    // private attributes for state file, date, and messages today
    private File stateFile;
    private LocalDate date;
    private int messagesToday;

    // constructor to initialize message garden with a username and load state from file
    public MessageGarden(String userName) {
        // setting the state file path based on the username
        this.stateFile = new File("profiles/" + userName + "_garden.txt");

        // Calling 2 methods: loadFromFile() and checkNewDay()
        loadFromFile();
        checkNewDay();
    }

    // recordMessage() method to record a new message and update the garden state
    public void recordMessage() {
        // checking if it's a new day and updating messages today
        checkNewDay();
        messagesToday++;
        saveToFile();
    }

    // getMessagesToday() method will return the number of messages sent today
    public int getMessagesToday() {
        // checking if it's a new day before returning the count
        return messagesToday;
    }

    // getCurrentStageName() method will return the current stage name of the garden based on messages today
    public String getCurrentStageName() {
        // determining the stage name based on the number of messages sent today
        if (messagesToday >= 20) {
            // returning "Flower" if messages today are 20 or more
            return "Flower";
        }
        else if (messagesToday >= 10) {
            // returning "Plant" if messages today are 10 or more
            return "Plant";
        }
        else if (messagesToday >= 5) {
            // returning "Sprout" if messages today are 5 or more
            return "Sprout";
        }
        else {
            // returning "Seed" if messages today are less than 5
            return "Seed";
        }
    }

    // getImagePath() method will return the image path for the current garden stage
    public String getImagePath() {

        // determining the image path based on the number of messages sent today
        if (messagesToday >= 20) {
            // returning the flower image path if messages today are 20 or more
            return "/resources/garden/flowers.jpg";
        }
        else if (messagesToday >= 10) {
            // returning the plant image path if messages today are 10 or more
            return "/resources/garden/plants.jpg";
        }
        else if (messagesToday >= 5) {
            // returning the sprout image path if messages today are 5 or more
            return "/resources/garden/sprout.jpg";
        }
        else {
            // returning the seed image path if messages today are less than 5
            return "/resources/garden/seeds.jpg";
        }
    }

    // checkNewDay() method will check if it's a new day and reset messages today if needed
    public void checkNewDay() {
        // checking if the stored date is null or different from today's date
        if (date == null || !date.equals(LocalDate.now())) {
            // resetting messages today and updating the date, as well as saving to file
            messagesToday = 0;
            date = LocalDate.now();
            saveToFile();
        }
    }

    // loadFomFile() method will load the garden state from the state file
    private void loadFromFile() {
        // checking if the state file exists
        if (!stateFile.exists()) {
            // if not, initializing messages today to 0 and date to today
            messagesToday = 0;
            date = LocalDate.now();
            return;
        }

        // reading the state file to load messages today and date
        try (BufferedReader reader = new BufferedReader(new FileReader(stateFile))) {
            // Storing messagesToday and date from the file
            String line = reader.readLine();

            // parsing messagesToday and date from the file
            if (line != null) {
                // setting messagesToday from the first line of the file
                messagesToday = Integer.parseInt(line);
            }

            // reading the second line for the date
            line = reader.readLine();

            // setting date from the second line of the file
            if (line != null) {
                date = LocalDate.parse(line);
            }
        } 
        // handling IOException and NumberFormatException during file reading
        catch (IOException | NumberFormatException e) {
            // initializing messages today to 0 and date to today if an error occurs
            messagesToday = 0;
            date = LocalDate.now();
        }
    }

    // saveToFile() method will save the garden state to the state file
    private void saveToFile() {
        // writing messages today and date to the state file
        try {
            // creating the state file if it doesn't exist
            PrintWriter writer = new PrintWriter(new FileWriter(stateFile));
                writer.println(date.toString());
                writer.println(messagesToday);
                writer.close();
        } 
        // handling IOException during file writing
        catch (Exception e) {
            System.out.println("Error saving garden state: " + e.getMessage());
        }
    }
}
