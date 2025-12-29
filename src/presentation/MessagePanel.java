package presentation;
import java.util.*;

import business.*;
import data.PhraseTile;


import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.ImageView;

public class MessagePanel {

    GridPane grid;

    public MessagePanel(PhraseExtractor extractor) {
        grid = new GridPane();

        grid.setPadding(new Insets(20));
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);
        //MessageBuilder mb = new MessageBuilder();
        List<PhraseTile> tiles = extractor.loadTiles();
        int col = 0;
        int row = 0;
        for (PhraseTile tile : tiles) {
            Button button = new Button(tile.getPhrase());
             button.setPrefSize(160, 90);

            if (tile.getIcon() != null) {
                ImageView imgView = new ImageView(tile.getIcon());
                imgView.setFitHeight(40);
                imgView.setPreserveRatio(true);
                button.setGraphic(imgView);
                button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            }
            
            grid.add(button, col, row);
            col++;
            if (col > 2) {
                col = 0;
                row++;
            }

            button.setOnAction(e -> {
               extractor.handleTileClick(tile);
            });
        }
    }

    public GridPane loadTiles() {
        return grid;
    }
}