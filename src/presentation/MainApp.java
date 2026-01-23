/* 
    Vivaan Echambadi
    1/22/2026

    MainApp() class will have extend the Application class from JavaFX and will be responsible for launching the application.
    It will set up the primary stage, load user profiles, and initialize the main user interface. It will also have a method that
    loads in the css stylesheets for the application, and a method to launch the application.
*/

// importing packages
package presentation;

import java.util.HashMap;
import java.util.Map;

import business.MessageBuilder;
import data.FileManager;
import data.UserProfile;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

// MainApp class definition that extends Application
public class MainApp extends Application {

    // static map to hold phrase-meaning dictionary
    public static Map <String, String> phraseMeaningDictMap = new HashMap<String, String>(); 

    // css() method to load CSS files for styling the application
    private static String css(String path) {
        // getting the URL of the CSS file
        var url = MainApp.class.getResource(path);

        // throwing an exception if the CSS file is not found
        if (url == null) {
            throw new IllegalArgumentException("CSS file not found: " + path);
        }

        // returning the external form of the URL
        return url.toExternalForm();
    }

    // start() method that has been overrided to set up the primary stage and initialize the user interface
    @Override
    public void start(Stage stage) {
        // loading user profile from a text file
        UserProfile profile = FileManager.loadProfileFromTxt("profiles/Jane.txt");

        // if no profile is found, create a default profile
        if (profile == null) {
            profile = new UserProfile("User");
        }

        // creating UserScreen instance with the loaded profile
        UserScreen screen = new UserScreen(profile);

        // setting up the scene with the user screen and applying CSS stylesheets
        Scene scene = new Scene(screen.getScreen(), 1300, 820);
        scene.getStylesheets().add(css("/resources/styles/neuro-theme.css"));
        scene.getStylesheets().add(css("/resources/styles/popups.css"));

        // setting up the primary stage with title, minimum dimensions, and the scene
        stage.setTitle("NeuroBridge " + profile.getName());
        stage.setMinWidth(1100);
        stage.setMinHeight(720);
        stage.setScene(scene);
        stage.show();

        // Loads phrase dictionary into MainApp.phraseMeaningDictMap
        MessageBuilder.loadPhraseDict();
    }

    // main() method to launch the application
    public static void main(String[] args) {
        // launching the JavaFX application
        launch(args);
    }
}


