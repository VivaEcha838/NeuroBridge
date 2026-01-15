package presentation;

import java.util.HashMap;
import java.util.Map;

import business.MessageBuilder;
import data.FileManager;
import data.UserProfile;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static Map <String, String> phraseMeaningDictMap = new HashMap<String, String>(); 

    private static String css(String path) {
        var url = MainApp.class.getResource(path);
        if (url == null) {
            throw new IllegalArgumentException("CSS file not found: " + path);
        }
        return url.toExternalForm();
    }

    @Override
    public void start(Stage stage) {
        UserProfile profile = FileManager.loadProfileFromTxt("profiles/Jane.txt");
        if (profile == null) {
            profile = new UserProfile("User");
        }

        UserScreen screen = new UserScreen(profile);

        Scene scene = new Scene(screen.getScreen(), 1300, 820);
        scene.getStylesheets().add(css("/resources/styles/neuro-theme.css"));
        scene.getStylesheets().add(css("/resources/styles/popups.css"));

        stage.setTitle("NeuroBridge " + profile.getName());
        stage.setMinWidth(1100);
        stage.setMinHeight(720);
        stage.setScene(scene);
        stage.show();

        // Loads phrase dictionary into MainApp.phraseMeaningDictMap
        MessageBuilder.loadPhraseDict();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


