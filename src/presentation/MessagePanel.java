package presentation;

import business.PhraseExtractor;
import business.AISuggestionEngine;
import business.PhraseSuggestion;
import data.PhraseTile;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Priority;

import java.util.List;

public class MessagePanel {
    private final PhraseExtractor EXTRACTOR;
    private final GridPane GRID;
    private final AISuggestionEngine AI_ENGINE;

    public MessagePanel(PhraseExtractor extractor) {
        this.EXTRACTOR = extractor;
        String userName = getUserNameFromExtractor();
        this.AI_ENGINE = new AISuggestionEngine(userName);

        GRID = new GridPane();
        GRID.getStyleClass().add("tile-grid");
        GRID.setPadding(new Insets(10));
        GRID.setHgap(14);
        GRID.setVgap(14);
        GRID.setAlignment(Pos.TOP_CENTER);

        buildTiles();
    }

    private String getUserNameFromExtractor() {
        return EXTRACTOR.getUserName();
    }

    private void buildTiles() {
        List<PhraseTile> tiles = EXTRACTOR.loadTiles();

        int col = 0;
        int row = 0;
        int maxCols = 3;

        final double ICON_SIZE = 82;
        final boolean SHOW_LABELS = true;

        for (PhraseTile tile : tiles) {
            String phrase;
            if (tile.getPhrase() == null) {
                phrase = "";
            }
            else {
                phrase = tile.getPhrase().trim();
            }

            Button button = new Button(tile.getPhrase());
            button.getStyleClass().add("tile-button");

            button.setPrefSize(190, 130);
            button.setMinSize(190, 130);
            button.setMaxSize(190, 130);
            button.setWrapText(true);

            if (SHOW_LABELS) {
                button.setStyle("");
            }
            else {
                button.setStyle("-fx-content-display: graphic-only");
            }

            if (!phrase.isEmpty()) {
                button.setTooltip(new Tooltip(phrase));
            }

            if (tile.getIcon() != null) {
                ImageView iconView = new ImageView(tile.getIcon());
                iconView.setFitHeight(ICON_SIZE);
                iconView.setFitWidth(ICON_SIZE);
                iconView.setPreserveRatio(true);
                iconView.setSmooth(true);

                button.setGraphic(iconView);
                
                if (SHOW_LABELS) {
                    button.setText(phrase);
                    button.setContentDisplay(ContentDisplay.TOP);
                }
                else {
                    button.setText("");
                    button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                }
            }
            else {
                button.setText(phrase);
                button.setContentDisplay(ContentDisplay.TEXT_ONLY);
            }

            button.setOnAction(e -> EXTRACTOR.handleTileClick(tile));

            GRID.add(button, col, row);

            col++;
            if (col >= maxCols) {
                col = 0;
                row++;
            }
        }
    }

    public Node loadTiles() {
        VBox suggestionsBox = buildSuggestionsSection();
        ScrollPane sp = new ScrollPane(GRID);
        sp.setFitToWidth(true);
        sp.setPannable(true);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        VBox suggestionsContainer = new VBox(14);
        suggestionsContainer.getChildren().add(sp);
        suggestionsContainer.getChildren().add(suggestionsBox);
        VBox.setVgrow(sp, Priority.ALWAYS);
        return suggestionsContainer;
    }

    private VBox buildSuggestionsSection() {
        Label suggestionsHeader = new Label("💡 Suggested Phrases");
        suggestionsHeader.getStyleClass().add("card-title");
        suggestionsHeader.setStyle("-fx-font-szie: 14px");

        List<PhraseSuggestion> suggestions = AI_ENGINE.getSuggestions(5);

        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(8);
        flowPane.setVgap(8);
        flowPane.setPadding(new Insets(8));
        flowPane.setAlignment(Pos.CENTER_LEFT);

        for (int i = 0; i < suggestions.size(); i++) {
            PhraseSuggestion suggestion = suggestions.get(i);
            Button button = new Button(suggestion.getPhrase());
            button.getStyleClass().add("button");
            button.getStyleClass().add("primary");
            button.setStyle("-fx-font-size: 12px; -fx-padding: 6 10 6 10;");

            Tooltip tooltip = new Tooltip(suggestion.getReason());
            button.setTooltip(tooltip);

            String addPhrase = suggestion.getPhrase();
            button.setOnAction(e -> {
                handleSuggestionClick(addPhrase);
            });
        
            flowPane.getChildren().add(button);

        }

        if (suggestions.isEmpty()) {
            Label noSuggestions = new Label("No suggestions yet, click on some phrases to get started");
            noSuggestions.getStyleClass().add("muted");
            noSuggestions.setStyle("-fx-font-size: 10px;");
            flowPane.getChildren().add(noSuggestions);
        }

        VBox card = new VBox(6);
        card.getStyleClass().add("card");
        card.getChildren().add(suggestionsHeader);
        card.getChildren().add(new Separator());
        card.getChildren().add(flowPane);
        card.setMaxHeight(120);

        return card;
    }

    private void handleSuggestionClick(String phrase) {
        List<PhraseTile> tiles = EXTRACTOR.loadTiles();
        for (int i = 0; i < tiles.size(); i++) {
            PhraseTile tile = tiles.get(i);
            if (tile.getPhrase().equals(phrase)) {
                EXTRACTOR.handleTileClick(tile);
                AI_ENGINE.recordSuggestionUsed(phrase);
                break;
            }
        }
    }
}
