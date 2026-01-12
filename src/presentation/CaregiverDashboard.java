package presentation;

import data.UserProfile;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;

public class CaregiverDashboard {

    private final String USERNAME;
    private final UserProfile PROFILE;
    private final SupportRecommendation RECOMMENDER;

    public CaregiverDashboard(String username, UserProfile profile) {
        this.USERNAME = username;
        this.PROFILE = profile;
        this.RECOMMENDER = new SupportRecommendation();
    }

    public Scene buildScene() {
        BorderPane root = new BorderPane();
        root.getStyleClass().addAll("app-root", "popup-root");
        root.setPadding(new Insets(14));

        // Header
        Label title = new Label("Caregiver Dashboard");
        title.getStyleClass().add("app-title");

        Label sub = new Label("For: " + USERNAME);
        sub.getStyleClass().add("app-subtitle");

        VBox header = new VBox(4, title, sub);
        header.setPadding(new Insets(10, 10, 14, 10));

        // Left controls (scenario + generate)
        Label scenarioLabel = new Label("Select Scenario");
        scenarioLabel.getStyleClass().add("muted");

        ComboBox<String> scenarioCombo = new ComboBox<>();
        scenarioCombo.getItems().addAll(
                "Morning Routine",
                "School / Homework",
                "Social Situation",
                "Public Place",
                "Bedtime Routine"
        );
        scenarioCombo.getSelectionModel().selectFirst();

        Button openTab = new Button("Open Situation Tab");
        openTab.getStyleClass().addAll("button", "primary");
        openTab.setMaxWidth(Double.MAX_VALUE);

        Button generate = new Button("Generate Recommendations");
        generate.getStyleClass().addAll("button", "primary");
        generate.setMaxWidth(Double.MAX_VALUE);

        VBox left = new VBox(10, scenarioLabel, scenarioCombo, generate);
        left.setAlignment(Pos.TOP_LEFT);
        left.setFillWidth(true);
        VBox leftCard = card("Controls", left);
        leftCard.setMinWidth(280);

        // Right: tabs built from Caregiver Guide.txt
        TextArea generalArea = new TextArea(loadGeneralGuideText());
        generalArea.setWrapText(true);
        generalArea.setEditable(false);

        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // General Recommendations tab = everything above SUPPORT RECOMMENDATIONS BY SITUATION
        Tab generalTab = new Tab("General Recommendations", generalArea);
        tabs.getTabs().add(generalTab);

        // Situational tabs = each "SITUATION: ..." line under the marker
        Map<String, Tab> situationTabIndex = new HashMap<>();
        for (String[] pair : loadSituationalRecs()) {
            String situationKey = pair[0];     // e.g. "MORNING ROUTINE"
            String situationText = pair[1];    // e.g. "dim lights, quiet voice, ..."

            TextArea area = new TextArea(situationText);
            area.setWrapText(true);
            area.setEditable(false);
            Tab t = new Tab(toTitleCase(situationKey), area);
            tabs.getTabs().add(t);
            situationTabIndex.put(normalizeKey(situationKey), t);
        }

        VBox rightCard = card("Recommendations", tabs);

        SplitPane split = new SplitPane(leftCard, rightCard);
        split.setDividerPositions(0.28);

        root.setTop(header);
        root.setCenter(split);

        // Button jumps to the matching situation tab (if found), otherwise stays on General
        openTab.setOnAction(e -> {
            String selected = scenarioCombo.getValue();
            Tab t = situationTabIndex.get(normalizeKey(selected));
            if (t != null) {
                tabs.getSelectionModel().select(t);
            } else {
                tabs.getSelectionModel().select(generalTab);
            }
        });

        Scene scene = new Scene(root, 1100, 780);
        scene.getStylesheets().add(MainApp.class.getResource("/resources/styles/neuro-theme.css").toExternalForm());
        scene.getStylesheets().add(MainApp.class.getResource("/resources/styles/popups.css").toExternalForm());
        return scene;
    }

    private String normalizeKey(String s) {
        if (s == null) return "";
            return s.trim().toUpperCase()
                .replace(" / ", "/")
                .replace("/", "/")
                .replace("-", "-");
    }

    private String toTitleCase(String s) {
        if (s == null || s.isBlank()) return s;
        String[] parts = s.toLowerCase().split(" ");
        StringBuilder sb = new StringBuilder();
        for (String p : parts) {
            if (p.length() > 0) {
                sb.append(Character.toUpperCase(p.charAt(0)));
                if (p.length() > 1) {
                    sb.append(p.substring(1));
                }
                sb.append(" ");
            }
        }
        return sb.toString().trim();
    }

    private VBox card(String title, javafx.scene.Node content) {
        Label t = new Label(title);
        t.getStyleClass().add("card-title");

        VBox box = new VBox(10, t, new Separator(), content);
        box.getStyleClass().add("card");
        VBox.setVgrow(content, Priority.ALWAYS);
        return box;
    }

    private String buildRecommendationText(String scenario) {
        StringBuilder sb = new StringBuilder();
        sb.append("Situation: ").append(scenario).append("\n\n");

        var recs = RECOMMENDER.getScenarioTips(scenario);
        if (recs != null && !recs.isEmpty()) {
            sb.append("Recommendations:\n");
            for (int i = 0; i < recs.size(); i++) {
                sb.append(i + 1).append(". ").append(recs.get(i)).append("\n");
            }
        } else {
            sb.append("No recommendations available for this scenario.\n");
        }

        var tips = RECOMMENDER.getScenarioTips(scenario);
        if (tips != null && !tips.isEmpty()) {
            sb.append("\nQuick Tips:\n");
            for (int i = 0; i < tips.size(); i++) {
                sb.append("- ").append(tips.get(i)).append("\n");
            }
        }

        return sb.toString();
    }

    private String readGuideFile() {
        try (InputStream is = getClass().getResourceAsStream("/resources/Caregiver Guide.txt")) {
            if (is == null) {
                return new String(is.readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (Exception ignored) {}
        try (InputStream is = new FileInputStream("resources/Caregiver Guide.txt")) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println("Failed to read caregiver guide file: " + e.getMessage());
            return "";
        }
    }

    private String loadGeneralGuideText() {
        String full = readGuideFile();
        if (full.isBlank()) {
            return "Caregiver Guide unavailable.";
        }

        String marker = "SUPPORT RECOMMENDATIONS BY SITUATION";
        int idx = full.indexOf(marker);

        if (idx < 0) {
            return full.trim();
        }

        return full.substring(0, idx).trim();
    }

    private List<String[]> loadSituationalRecs() {
        String full = readGuideFile();
        List<String[]> recs = new ArrayList<>();
        if (full.isBlank()) {
            return recs;
        }

        String marker = "SUPPORT RECOMMENDATIONS BY SITUATION";
        int idx = full.indexOf(marker);

        if (idx < 0) {
            return recs;
        }

        String situationalPart = full.substring(idx + marker.length()).trim();
        if (situationalPart.isBlank()) {
            return recs;
        }

        String[] sections = situationalPart.split("===");
        for (String sec : sections) {
            String[] lines = sec.trim().split("\n");
            if (lines.length >= 2) {
                String scenario = lines[0].trim();
                for (int i = 1; i < lines.length; i++) {
                    String tip = lines[i].trim();
                    if (!tip.isEmpty()) {
                        recs.add(new String[]{scenario, tip});
                    }
                }
            }
        }

        return recs;
    }
}
