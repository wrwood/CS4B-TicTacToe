package Game.Controller.Board;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import Game.Controller.PausedController;

import Game.Util.*;
import Game.Model.Model;
import Game.Model.Observer;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

/*
    Structured as

    TurnLogic          Determines when players can take turns and when a win is determined and handled
    ObserverMethods    Handles registration, removal, and updates of events
    Rescaling          Scales components
    ButtonCreation     Creates and assigns actions to buttons
    Pause              pauseButton needs
    SwapStage          Will close board and open new stages
 */

public class BoardController implements Initializable, Observer {

    private Pane[] cells = new Pane[9];
    Map<Pane, ImageView> cellToXMarkerViewMap = new HashMap<>();
    Map<Pane, ImageView> cellToOMarkerViewMap = new HashMap<>();

    @FXML private GridPane boardRoot;
    @FXML private BorderPane gameBoardParent;
    @FXML private GridPane gameBoardGrid;
    @FXML private Pane overlayPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        registerObservers();
        handleBoardRescale();
        createBoardButtons();

        if(!(Model.getInstance().getIsPlayer1())) {
            gameBoardGrid.setDisable(true);
        }
        overlayPane.setMouseTransparent(true);
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
            case Config.PLAYER_TURN: gameBoardGrid.setDisable(!((boolean) data));
                break;
            // Receives Player2's move and places marker while disabling the click event
            case Config.PLAYER2_MOVE: cellToOMarkerViewMap.get(cells[(int)data]).setVisible(true);
                cells[(int)data].setDisable(true);
                break;
            case Config.GAME_OVER: GameOverAnimation();
                break;
            case Config.GAME_RESET: resetBoard();
                break;
        }
    }

    private void registerObservers() {
        Model.getInstance().registerObserver(Config.PLAYER_TURN, this);
        Model.getInstance().registerObserver(Config.PLAYER2_MOVE, this);
        Model.getInstance().registerObserver(Config.GAME_OVER, this);
        Model.getInstance().registerObserver(Config.GAME_RESET, this);
    }

    private void unregisterObservers() {
        Model.getInstance().removeObserver(Config.PLAYER_TURN, this);
        Model.getInstance().removeObserver(Config.PLAYER2_MOVE, this);
        Model.getInstance().removeObserver(Config.GAME_OVER, this);
        Model.getInstance().removeObserver(Config.GAME_RESET, this);
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
                cellToXMarkerViewMap.put(cells[i], xMarkerView);
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

            cells[i].setDisable(true);
            gameBoardGrid.setDisable(true);

            if(Model.getInstance().getGameMode() == GameModes.ONLINE_MULTIPLAYER) {
                Model.getInstance().makeMove(i);
            } else {
                makePlayerMove(i);
            }


        });
    }

    private void resetBoard() {
        for (Pane cell : cells) {
            cell.getChildren().clear();
        }
        cellToXMarkerViewMap.clear();
        cellToOMarkerViewMap.clear();
        createBoardButtons();
        gameBoardGrid.setDisable(!Model.getInstance().getIsPlayer1());
    }


    // GameOverAnimation ==================================================
    private void GameOverAnimation() {
        PauseTransition initialPause = new PauseTransition(Duration.seconds(0.5));

        initialPause.setOnFinished(event -> {
            int[] winningLine = Model.getInstance().getWinningLine();
            if (winningLine != null) {
                Line line = getLine(winningLine);

                overlayPane.getChildren().add(line);

                overlayPane.toFront();
                gameBoardGrid.toBack();

                PauseTransition showLinePause = new PauseTransition(Duration.seconds(2));
                showLinePause.setOnFinished(e -> openResult());
                showLinePause.play();
            } else {
                openResult();
            }
        });
        initialPause.play();
    }

    private Line getLine(int[] winningLine) {
        Point2D startPoint = cells[winningLine[0]].localToScene(cells[winningLine[0]].getWidth() / 2, cells[winningLine[0]].getHeight() / 2);
        Point2D endPoint = cells[winningLine[2]].localToScene(cells[winningLine[2]].getWidth() / 2, cells[winningLine[2]].getHeight() / 2);

        Point2D overlayStart = overlayPane.sceneToLocal(startPoint);
        Point2D overlayEnd = overlayPane.sceneToLocal(endPoint);

        // Debug
        //System.out.println("Start: " + overlayStart + ", End: " + overlayEnd);

        Line line = new Line(overlayStart.getX(), overlayStart.getY(), overlayEnd.getX(), overlayEnd.getY());
        line.setStrokeWidth(5);
        line.setStroke(Color.RED);

        return line;
    }

    // SwapStage ==================================================
    private void openMenu() {
        Stage stage = (Stage) boardRoot.getScene().getWindow();
        unregisterObservers();
        Model.getInstance().getViewFactory().closeStage(stage);

        try {
            Model.getInstance().getViewFactory().showPaused();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void openResult() {
        Stage stage = (Stage) boardRoot.getScene().getWindow();
        unregisterObservers();
        Model.getInstance().getViewFactory().closeStage(stage);
        try {
            Model.getInstance().getViewFactory().showResults();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void createStage(FXMLLoader loader) throws IOException {
        Scene scene = new Scene(loader.load());
        URL styleSheetURL = getClass().getResource("/StyleSheet/darkMode.css");
        if(styleSheetURL != null) {
            scene.getStylesheets().add(styleSheetURL.toExternalForm());
        } else {
            System.out.println("Stylesheet not found.");
        }

        Stage stage = new Stage();
        stage.setScene(scene);

        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream(Config.PLAYER1_MARKER_IMAGE))));
        stage.setTitle("Tic Tac Toe");
        stage.show();
    }

    public void closeStage(Stage stage){
        stage.close();
    }


}