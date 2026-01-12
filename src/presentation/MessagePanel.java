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

        for (PhraseTile tile : tiles) {
            Button button = new Button(tile.getPhrase());
            button.getStyleClass().add("tile-button");

            button.setPrefSize(190, 130);
            button.setMinSize(190, 130);
            button.setMaxSize(190, 130);
            button.setWrapText(true);

            if (tile.getIcon() != null) {
                ImageView iconView = new ImageView(tile.getIcon());
                iconView.setFitHeight(56);
                iconView.setPreserveRatio(true);

                button.setGraphic(iconView);
                button.setContentDisplay(ContentDisplay.TOP);
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
