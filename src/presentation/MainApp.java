
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
    @Override
    public void start(Stage stage) {
        String userName = "Jane";
        UserProfile profile = FileManager.loadProfile(userName);
        if (profile == null) {
            profile = new UserProfile(userName);
        }
        UserScreen userScreen = new UserScreen(profile);
        Scene scene = new Scene(
            userScreen.getScreen(), 800, 600
        );

        stage.setTitle("NeuroBridge " + profile.getName());
        stage.setScene(scene);
        stage.show();

        MessageBuilder.loadPhraseDict();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


