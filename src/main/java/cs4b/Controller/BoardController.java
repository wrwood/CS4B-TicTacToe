package cs4b.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cs4b.config.*;
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

/*
    Structured as

    TurnLogic          Determines when players can take turns and when a win is determined and handled
    ObververMethods    Handles registration, removal, and updates of events 
    Rescaling          Scales components 
    ButtonCreation     Creates and assigns actions to buttons
    SwapStage          Will close board and open new stages 
 */

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

        registerObservers();
        handleBoardRescale();
        createBoardButtons();
    }

    // TurnLogic ==================================================
    private void makePlayerMove(int moveIndex) { // When a board button is clicked 
        Model.getInstance().makeMove(moveIndex);

        // If the game is not over moves to the next player turn 
        if(Model.getInstance().isGameOver()) {     
            Model.getInstance().handleGameOver();
        } else {
            Model.getInstance().switchPlayerTurn();
            makeOtherPlayerMove();
        }
    }

    private void makeOtherPlayerMove() {
        Model.getInstance().getOtherPlayerMove();

        // If the game is not over switches active players to allow for access to board buttons
        if(Model.getInstance().isGameOver()) {
            Model.getInstance().handleGameOver();
        } else {
            Model.getInstance().switchPlayerTurn();
        }
    }

    // ObserverMethods ==================================================
    @Override
    public void update(String eventType, Object data) {
        switch (eventType) {
            // When it is the players turn allows for access to board buttons 
            case Config.PLAYER_TURN: gameBoardGrid.setDisable(false); 
                break;
            // Receives Player2's move and places marker while disabling the click event 
            case Config.PLAYER2_MOVE: cellToOMarkerViewMap.get(cells[(int)data]).setVisible(true);
                                      cells[(int)data].setOnMouseClicked(null);
                break;
            case Config.GAME_OVER: openResult();
                break;
        }
    }

    private void registerObservers() {
        Model.getInstance().registerObserver(Config.PLAYER_TURN, this);
        Model.getInstance().registerObserver(Config.PLAYER2_MOVE, this);
        Model.getInstance().registerObserver(Config.GAME_OVER, this);
    }

    private void unregisterObservers() {
        Model.getInstance().removeObserver(Config.PLAYER_TURN, this);
        Model.getInstance().removeObserver(Config.PLAYER2_MOVE, this);
        Model.getInstance().removeObserver(Config.GAME_OVER, this);
    }

    // Rescaling ==================================================
    private void handleBoardRescale() {
        // Keeps a 1 to 1 ratio when scaling the game board
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

    // ButtonCreation ==================================================
    private void createBoardButtons() {
        for (int i = 0; i < 9; i++) {
            Pane pane = (Pane) gameBoardGrid.lookup("#cell" + i);
            if (pane != null) {
                Image xMarkerImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(Config.PLAYER1_MARKER_IMAGE)));
                Image oMarkerImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(Config.PLAYER2_MARKER_IMAGE)));

                ImageView xMarkerView = createMarkerView(xMarkerImage);
                ImageView oMarkerView = createMarkerView(oMarkerImage);

                cells[i] = pane;

                setupMarkerView(xMarkerView, cells[i]);
                setupMarkerView(oMarkerView, cells[i]);

                // Maps markers and cells for later use 
                cellToOMarkerViewMap.put(cells[i], xMarkerView);
                cellToOMarkerViewMap.put(cells[i], oMarkerView);

                setUpCellClickEvent(i, xMarkerView, oMarkerView);
            }
        }
    }

    private ImageView createMarkerView(Image image) {
        ImageView markerView = new ImageView(image);
        markerView.setVisible(false);
        return markerView;
    }

    private void setupMarkerView(ImageView markerView, Pane cell) {
        // Moves image to the center of the cell
        markerView.layoutXProperty().bind(cell.widthProperty().divide(10));
        markerView.layoutYProperty().bind(cell.heightProperty().divide(120));
        // Keeps image ratio (1280 x 1630)
        markerView.fitWidthProperty().bind(cell.widthProperty().divide(1.273));
        markerView.fitHeightProperty().bind(cell.heightProperty());

        cell.getChildren().add(markerView);
    }

    private void setUpCellClickEvent (int i, ImageView xMarkerView, ImageView oMarkerView) {
        cells[i].setOnMouseClicked(event -> {
            // Sets the correct marker of the active player to visible
            if(Model.getInstance().getActingPlayerMarker() == 'x') {
                xMarkerView.setVisible(true);
            } else {
                oMarkerView.setVisible(true);
            }

            cells[i].setOnMouseClicked(null);
            gameBoardGrid.setDisable(true);

            makePlayerMove(i);
        });
    }


    // SwapStage ==================================================
    private void openMenu() {
        Stage stage = (Stage)menuButton.getScene().getWindow();
        unregisterObservers();
        Model.getInstance().getViewFactory().closeStage(stage);

        try {
            Model.getInstance().getViewFactory().showPaused();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private void openResult() {
        Stage stage = (Stage)menuButton.getScene().getWindow();
        unregisterObservers();
        Model.getInstance().getViewFactory().closeStage(stage);
        try {
            Model.getInstance().getViewFactory().showResults();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}