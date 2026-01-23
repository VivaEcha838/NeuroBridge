/* 
    Vivaan Echambadi
    1/22/2026

    MessagePanel class will consist of a grid of phrase tiles loaded from the PhraseExtractor. It will
    include many different functionalities, and the purpose of MessagePanel is to create a visual message board
    for the AAC. Each tile will display an icon or a phrase, and clicking a tile will trigger the app's 
    association between icon and phrase. It will also include a section for AI-generated phrase suggestions based on user data. 
    The class will handle the layout and user interactions. It will use JavaFX components to create a visually appealing and user-friendly interface.

*/

// importing packages
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

// creating MessagePanel class
public class MessagePanel {
    // defining class variables
    private final PhraseExtractor EXTRACTOR;
    private final GridPane GRID;
    private final AISuggestionEngine AI_ENGINE;

    // constructor for MessagePanel class that will accept a PhraseExtractor object and initialize the class variables
    public MessagePanel(PhraseExtractor extractor) {
        // initializing class variables
        this.EXTRACTOR = extractor;
        String userName = getUserNameFromExtractor();
        this.AI_ENGINE = new AISuggestionEngine(userName);

        // setting up the grid pane for tiles, its styles and properties
        GRID = new GridPane();
        GRID.getStyleClass().add("tile-grid");
        GRID.setPadding(new Insets(10));
        GRID.setHgap(14);
        GRID.setVgap(14);
        GRID.setAlignment(Pos.TOP_CENTER);

        // calling buildTiles() method to populate the grid with tiles
        buildTiles();
    }

    // getUserNameFromExtractor() method retrieves the user name from the PhraseExtractor.
    private String getUserNameFromExtractor() {
        return EXTRACTOR.getUserName();
    }

    // buildTiles() method populates the grid with phrase tiles
    private void buildTiles() {
        List<PhraseTile> tiles = EXTRACTOR.loadTiles();

        // setting up variables for grid placement (columns and rows, as well as max columns)
        int col = 0;
        int row = 0;
        int maxCols = 3;

        // creating two final variables that will define icon size and whether to show labels
        final double ICON_SIZE = 82;
        final boolean SHOW_LABELS = true;

        // for loop that iterates through each tile and creates a button for it
        for (PhraseTile tile : tiles) {
            String phrase;
            // checking if the tile's phrase is null, and setting phrase variable accordingly
            if (tile.getPhrase() == null) {
                phrase = "";
            }
            else {
                phrase = tile.getPhrase().trim();
            }

            // creating button for the tile and setting its styles and properties
            Button button = new Button(phrase);
            button.getStyleClass().add("tile-button");

            button.setPrefSize(190, 130);
            button.setMinSize(190, 130);
            button.setMaxSize(190, 130);
            button.setWrapText(true);

            // adjusting button style based on if SHOW_LABELS variable is true or false
            if (SHOW_LABELS) {
                button.setStyle("");
            }
            // if SHOW_LABELS is false, only show the icon without text
            else {
                button.setStyle("-fx-content-display: graphic-only");
            }

            // setting tooltip for button if phrase is not empty
            if (!phrase.isEmpty()) {
                button.setTooltip(new Tooltip(phrase));
            }

            // if tile has an icon, then create an ImageView for it and set it as the button's graphic
            if (tile.getIcon() != null) {
                // creating ImageView for the icon and setting its properties with center-crop behavior
                ImageView iconView = new ImageView(tile.getIcon());
                
                // calculating viewport to center-crop the image instead of distorting it
                double imageWidth = tile.getIcon().getWidth();
                double imageHeight = tile.getIcon().getHeight();
                double aspectRatio = imageWidth / imageHeight;
                
                // if image is wider than square, crop the sides
                if (aspectRatio > 1.0) {
                    double cropWidth = imageHeight;
                    double cropX = (imageWidth - cropWidth) / 2.0;
                    iconView.setViewport(new javafx.geometry.Rectangle2D(cropX, 0, cropWidth, imageHeight));
                } 
                // if image is taller than square, crop top and bottom
                else if (aspectRatio < 1.0) {
                    double cropHeight = imageWidth;
                    double cropY = (imageHeight - cropHeight) / 2.0;
                    iconView.setViewport(new javafx.geometry.Rectangle2D(0, cropY, imageWidth, cropHeight));
                }
                // if already square, no viewport needed
                
                iconView.setFitHeight(ICON_SIZE);
                iconView.setFitWidth(ICON_SIZE);
                iconView.setPreserveRatio(true);
                iconView.setSmooth(true);
                // setting the icon as the button's graphic
                button.setGraphic(iconView);
                
                // adjusting button text and content display based on SHOW_LABELS variable
                if (SHOW_LABELS) {
                    button.setText(phrase);
                    button.setContentDisplay(ContentDisplay.TOP);
                }
                // if SHOW_LABELS is false, only show the icon without text
                else {
                    button.setText("");
                    button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                }
            }

            // if tile does not have an icon, only show the phrase text
            else {
                button.setText(phrase);
                button.setContentDisplay(ContentDisplay.TEXT_ONLY);
            }

            // setting action for button click to handle tile click using PhraseExtractor
            button.setOnAction(e -> EXTRACTOR.handleTileClick(tile));

            // adding button to the grid at the current column and row, as well as incrementing column index
            GRID.add(button, col, row);
            col++;

            // if column index exceeds max columns, reset column index and increment row index
            if (col >= maxCols) {
                col = 0;
                row++;
            }
        }
    }

    // loadTiles() method will return a Node that contains the grid of tiles along with the suggestions section
    public Node loadTiles() {
        // creating VBox for suggestions section and setting up ScrollPane for the grid
        VBox suggestionsBox = buildSuggestionsSection();
        ScrollPane sp = new ScrollPane(GRID);
        // setting ScrollPane properties
        sp.setFitToWidth(true);
        sp.setPannable(true);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        // creating main container VBox that holds both the suggestions section and the grid ScrollPane
        VBox suggestionsContainer = new VBox(14);
        suggestionsContainer.getChildren().add(sp);
        suggestionsContainer.getChildren().add(suggestionsBox);
        // setting VBox grow priority for ScrollPane and returning suggestionsContainer
        VBox.setVgrow(sp, Priority.ALWAYS);
        return suggestionsContainer;
    }

    // buildSuggestionsSection() method creates the suggestions section with AI-generated phrase suggestions
    private VBox buildSuggestionsSection() {
        // creating Label for suggestions header and setting its styles
        Label suggestionsHeader = new Label("💡 Suggested Phrases");
        suggestionsHeader.getStyleClass().add("card-title");
        suggestionsHeader.setStyle("-fx-font-szie: 14px");

        // getting suggestions from AI engine
        List<PhraseSuggestion> suggestions = AI_ENGINE.getSuggestions(5);

        // creating FlowPane to hold suggestion buttons and setting its properties
        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(8);
        flowPane.setVgap(8);
        flowPane.setPadding(new Insets(8));
        flowPane.setAlignment(Pos.CENTER_LEFT);

        // for loop that iterates through each suggestion and creates a button for it
        for (int i = 0; i < suggestions.size(); i++) {
            // creating PhraseSuggestion object for the current suggestion
            PhraseSuggestion suggestion = suggestions.get(i);
            // creating button for the suggestion and setting its styles and properties
            Button button = new Button(suggestion.getPhrase());
            button.getStyleClass().add("button");
            button.getStyleClass().add("primary");
            button.setStyle("-fx-font-size: 12px; -fx-padding: 6 10 6 10;");

            // setting tooltip for the button to show the reason for the suggestion
            Tooltip tooltip = new Tooltip(suggestion.getReason());
            button.setTooltip(tooltip);

            // setting action for button click to handle suggestion click
            String addPhrase = suggestion.getPhrase();
            button.setOnAction(e -> {
                handleSuggestionClick(addPhrase);
            });
            
            // adding button to the FlowPane
            flowPane.getChildren().add(button);

        }

        // if suggestions list is empty, show a message indicating no suggestions are available
        if (suggestions.isEmpty()) {
            // creating Label for no suggestions message and setting its styles
            Label noSuggestions = new Label("No suggestions yet, click on some phrases to get started");
            noSuggestions.getStyleClass().add("muted");
            noSuggestions.setStyle("-fx-font-size: 10px;");

            // adding no suggestions message to the FlowPane
            flowPane.getChildren().add(noSuggestions);
        }

        // creating VBox for the suggestions card and setting its properties
        VBox card = new VBox(6);
        card.getStyleClass().add("card");
        card.getChildren().add(suggestionsHeader);
        card.getChildren().add(new Separator());
        card.getChildren().add(flowPane);
        card.setMaxHeight(120);

        // returning the card
        return card;
    }

    // handleSuggestionClick() method processes the click on a suggested phrase button
    private void handleSuggestionClick(String phrase) {
        // loading tiles from PhraseExtractor and searching for the tile with the matching phrase
        List<PhraseTile> tiles = EXTRACTOR.loadTiles();
        
        // for loop that iterates through each tile to find the matching phrase
        for (int i = 0; i < tiles.size(); i++) {
            PhraseTile tile = tiles.get(i);

            // if the tile's phrase matches the clicked suggestion, handle the tile click and record the suggestion usage
            if (tile.getPhrase().equals(phrase)) {
                // handling tile click and recording suggestion usage
                EXTRACTOR.handleTileClick(tile);
                AI_ENGINE.recordSuggestionUsed(phrase);
                break;
            }
        }
    }
}