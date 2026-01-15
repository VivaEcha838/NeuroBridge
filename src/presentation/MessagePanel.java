package presentation;

import business.PhraseExtractor;
import data.PhraseTile;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Tooltip;

import java.util.List;

public class MessagePanel {
    private final PhraseExtractor EXTRACTOR;
    private final GridPane GRID;

    public MessagePanel(PhraseExtractor extractor) {
        this.EXTRACTOR = extractor;

        GRID = new GridPane();
        GRID.getStyleClass().add("tile-grid");
        GRID.setPadding(new Insets(10));
        GRID.setHgap(14);
        GRID.setVgap(14);
        GRID.setAlignment(Pos.TOP_CENTER);

        buildTiles();
    }

    private void buildTiles() {
        List<PhraseTile> tiles = EXTRACTOR.loadTiles();

        int col = 0;
        int row = 0;
        int maxCols = 3;

        final double ICON_SIZE = 82;
        final boolean SHOW_LABELS = true;

        for (PhraseTile tile : tiles) {
            String phrase = tile.getPhrase() == null ? "" : tile.getPhrase().trim();
            Button button = new Button(tile.getPhrase());
            button.getStyleClass().add("tile-button");

            button.setPrefSize(190, 130);
            button.setMinSize(190, 130);
            button.setMaxSize(190, 130);
            button.setWrapText(true);

            button.setStyle(SHOW_LABELS ? "" : "-fx-content-display: graphic-only;");

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
        ScrollPane sp = new ScrollPane(GRID);
        sp.setFitToWidth(true);
        sp.setPannable(true);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        return sp;
    }
}
