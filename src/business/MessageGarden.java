package business;

import java.io.*;
import java.time.LocalDate;

public class MessageGarden {
    private File stateFile;
    private LocalDate date;
    private int messagesToday;
    
    public MessageGarden(String userName) {
        this.stateFile = new File("profiles/" + userName + "_garden.txt");
        loadFromFile();
        checkNewDay();
    }

    public void recordMessage() {
        checkNewDay();
        messagesToday++;
        saveToFile();
    }

    public int getMessagesToday() {
        return messagesToday;
    }

    public String getCurrentStageName() {
        if (messagesToday >= 20) {
            return "Flower";
        }
        else if (messagesToday >= 10) {
            return "Plant";
        }
        else if (messagesToday >= 5) {
            return "Sprout";
        }
        else {
            return "Seeds";
        }
    }

    public String getImagePath() {
        if (messagesToday >= 20) {
            return "/resources/garden/flowers.jpg";
        }
        else if (messagesToday >= 10) {
            return "/resources/garden/plants.jpg";
        }
        else if (messagesToday >= 5) {
            return "/resources/garden/sprout.jpg";
        }
        else {
            return "/resources/garden/seeds.jpg";
        }
    }

    public void checkNewDay() {
        if (date == null || !date.equals(LocalDate.now())) {
            messagesToday = 0;
            date = LocalDate.now();
            saveToFile();
        }
    }

    private void loadFromFile() {
        if (!stateFile.exists()) {
            messagesToday = 0;
            date = LocalDate.now();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(stateFile))) {
            String line = reader.readLine();
            if (line != null) {
                messagesToday = Integer.parseInt(line);
            }
            line = reader.readLine();
            if (line != null) {
                date = LocalDate.parse(line);
            }
        } catch (IOException | NumberFormatException e) {
            messagesToday = 0;
            date = LocalDate.now();
        }
    }

    private void saveToFile() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(stateFile));
                writer.println(date.toString());
                writer.println(messagesToday);
                writer.close();
        } 
        catch (Exception e) {

        }
    }
}
