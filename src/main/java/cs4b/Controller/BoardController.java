package cs4b.Controller;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class BoardController {

    @FXML
    private BorderPane gameBoardParent;

    @FXML
    private GridPane gameBoardGrid;

    @FXML
    public void initialize() {
        gameBoardParent.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double size = Math.min(newWidth.doubleValue(), gameBoardParent.getHeight());
            gameBoardGrid.setMaxWidth(size);
            gameBoardGrid.setMaxHeight(size);
        });

        gameBoardParent.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            double size = Math.min(newHeight.doubleValue(), gameBoardParent.getWidth());
            gameBoardGrid.setMaxWidth(size);
            gameBoardGrid.setMaxHeight(size);
        });
    }
}
