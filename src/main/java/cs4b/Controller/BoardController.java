package cs4b.Controller;

import java.io.IOException;

import cs4b.Model.Model;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class BoardController {
    @FXML
    private GridPane boardRoot;

    @FXML
    private BorderPane gameBoardParent;

    @FXML
    private GridPane gameBoardGrid;

    @FXML
    private Button menuButton;

    @FXML
    public void initialize() {
        menuButton.setOnAction(e-> {
            openMenu();
        });

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

    public GridPane getBoardRoot() {
        return boardRoot;
    }

    private void openMenu() {
        Stage stage = (Stage)menuButton.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        try {
            Model.getInstance().getViewFactory().showPaused();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
