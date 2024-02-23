package cs4b.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cs4b.Model.Model;
import cs4b.Model.Observer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class BoardController implements Initializable, Observer {
    private Pane[] cells = new Pane[9];
    Map<Pane, ImageView> cellToXMarkerViewMap = new HashMap<>();
    Map<Pane, ImageView> cellToOMarkerViewMap = new HashMap<>();

    @FXML
    private GridPane boardRoot;

    @FXML
    private BorderPane gameBoardParent;

    @FXML
    private GridPane gameBoardGrid;

    @FXML
    private Button menuButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        menuButton.setOnAction(e-> {
            openMenu();
        });

        Model.getInstance().registerObserver("PlayerTurn", this);
        Model.getInstance().registerObserver("Player2Move", this);
        Model.getInstance().registerObserver("Win", this);
        Model.getInstance().registerObserver("Tie", this);

        handleBoardRescale();

        for (int i = 0; i < 9; i++) {
            Pane pane = (Pane) gameBoardGrid.lookup("#cell" + i);
            if (pane != null) {
                Image xMarkerImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                        "/images/dragon_egg_png_overlay__by_lewis4721_de8r1hj-fullview.png")));
                Image oMarkerImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                        "/images/dragon_egg_png_overlay__by_lewis4721_de8r1hq-414w-2x.png")));

                ImageView xMarkerView = createMarkerView(xMarkerImage);
                ImageView oMarkerView = createMarkerView(oMarkerImage);

                cells[i] = pane;

                setupMarkerView(xMarkerView, cells[i]);
                setupMarkerView(oMarkerView, cells[i]);

                cellToOMarkerViewMap.put(cells[i], xMarkerView);
                cellToOMarkerViewMap.put(cells[i], oMarkerView);

                int moveIndex = i;

                cells[i].setOnMouseClicked(event -> {
                    Model.getInstance().makeMove(moveIndex);
                    if(Model.getInstance().getPlayer1Marker() ==  'x') {
                        xMarkerView.setVisible(true);
                    } else {
                        oMarkerView.setVisible(true);
                    }
                    cells[moveIndex].setOnMouseClicked(null);
                });
            }
        }
    }

    @Override
    public void update(String eventType, Object data) {
        switch (eventType) {
            case "PlayerTurn":
                break;
            case "Player2Move": cellToOMarkerViewMap.get(cells[(int)data]).setVisible(true);
                cells[(int)data].setOnMouseClicked(null);
                break;
            case "Win": openMenu();
                break;
            case "Tie": openMenu();
        }
    }

    private void openMenu() {
        Stage stage = (Stage)menuButton.getScene().getWindow();
        Model.getInstance().removeObserver("PlayerTurn", this);
        Model.getInstance().removeObserver("Player2Move", this);
        Model.getInstance().removeObserver("Win", this);
        Model.getInstance().removeObserver("Tie", this);
        Model.getInstance().getViewFactory().closeStage(stage);
        try {
            Model.getInstance().getViewFactory().showPaused();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private ImageView createMarkerView(Image image) {
        ImageView markerView = new ImageView(image);
        markerView.setVisible(false);
        return markerView;
    }

    private void setupMarkerView(ImageView markerView, Pane cell) {
        // moves image to the center of the cell
        markerView.layoutXProperty().bind(cell.widthProperty().divide(10));
        markerView.layoutYProperty().bind(cell.heightProperty().divide(120));
        // keeps image ratio (1280 x 1630)
        markerView.fitWidthProperty().bind(cell.widthProperty().divide(1.273));
        markerView.fitHeightProperty().bind(cell.heightProperty());

        cell.getChildren().add(markerView);
    }

    private void handleBoardRescale() {
        // keeps a 1 to 1 ratio when scaling the game board
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