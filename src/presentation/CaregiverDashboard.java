package presentation;

import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

import data.UserProfile;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class CaregiverDashboard {
    private final String USERNAME;
    private final SupportRecommendation RECOMMENDER;

    public CaregiverDashboard(String username, UserProfile profile) {
        this.USERNAME = username;
        this.RECOMMENDER = new SupportRecommendation();
    }

    public Scene buildScene() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

       Label titleLabel = new Label("Caregiver Dashboard for " + USERNAME);
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

         VBox headerBox = new VBox(5, titleLabel);

         Label scenarioLabel = new Label("Select Scenario:");
         ComboBox<String> scenarioComboBox = new ComboBox<>();
            scenarioComboBox.getItems().addAll(
                "Morning Routine",
                "After school Transition",
                "Focus Time",
                "Transitions",
                "Overload",
                "Wind Down and Sleep"
            );

            scenarioComboBox.getSelectionModel().selectFirst();
            
            Button generateButton = new Button("Generate Recommendations");
            VBox left = new VBox(10, scenarioLabel, scenarioComboBox, generateButton);
            left.setPadding(new Insets(10));
            left.setStyle("-fx -border-color: lightgray; -fx-border-width: 1px;");

            TabPane tabPane = new TabPane();
            tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

            TextArea recommendationsArea = new TextArea(loadGuideText());
            recommendationsArea.setWrapText(true);
            recommendationsArea.setEditable(false);
            ScrollPane scrollPane = new ScrollPane(recommendationsArea);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            Tab recommendationsTab = new Tab("Caregiver Guide", scrollPane);

            TextArea strategiesArea = new TextArea();
            strategiesArea.setWrapText(true);
            strategiesArea.setEditable(false);
            Tab strategiesTab = new Tab("Recommended Strategies", strategiesArea);

            tabPane.getTabs().addAll(recommendationsTab, strategiesTab);

            generateButton.setOnAction(e -> {
                String scenario = scenarioComboBox.getValue();
                recommendationsArea.setText(buildRecommendationText(scenario));
                tabPane.getSelectionModel().select(strategiesTab);
            });

            HBox top = new HBox(16, headerBox);
            top.setPadding(new Insets(0, 0, 10, 0));

            root.setTop(top);
            root.setLeft(left);
            root.setCenter(tabPane);
            recommendationsArea.setText(buildRecommendationText(scenarioComboBox.getValue()));

        return new Scene(root, 800, 600);
    }

    private String buildRecommendationText(String scenario) {
        String key = RECOMMENDER.scenarioToKey(scenario);
        List<String> tips = RECOMMENDER.getScenarioTips(key);

        StringBuilder sb = new StringBuilder();
        sb.append("Recommendations\n");
        sb.append("Situation: ").append(scenario).append("\n\n");

        if (tips.isEmpty()) {
            sb.append("No recommendations available for this scenario.\n");
        } else {
            for (int i = 0; i < tips.size(); i++) {
                sb.append(i + 1).append(". ").append(tips.get(i)).append("\n");
            }
        }

        sb.append("\n");
        sb.append("Quick Tips:\n");
        sb.append("- Stay calm and patient.\n");
        sb.append("- Use clear and simple language.\n");
        sb.append("- Be consistent with routines.\n");

        return sb.toString();
    }

    private String loadGuideText() {
        StringBuilder sb = new StringBuilder();
        String resource = "/resources/caregiver_guide.txt";
        try {
            InputStream input = getClass().getResourceAsStream(resource);
            if (input == null) {
                return "Caregiver guide file not found.";
            }

            Scanner scanner = new Scanner(input);
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine()).append("\n");
            }
            scanner.close();
        } catch (Exception e) {
            return "Error loading caregiver guide: " + e.getMessage();
        }

        return sb.toString();
    }

    private String fallBackText() {
        return "Caregiver guide content is unavailable at this time.\n" +
               "Please contact support for assistance.";
    }
}