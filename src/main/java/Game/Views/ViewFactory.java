package Game.Views;

import Game.Controller.Board.GameController;
import Game.Controller.MainMenuController;
import Game.Util.Config;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/*
    Structured as

    GetPane     Prevents duplication of panes if already exists
    ShowPane    Will load stages

    Helper Methods
 */

public class ViewFactory {
    AnchorPane mainMenu;
    AnchorPane board;
    AnchorPane paused;
    AnchorPane results;
    AnchorPane loadingScreen;

    public ViewFactory()
    {

    }

// GetPane ==================================================
    public AnchorPane GetMainMenu()
    {
        if (mainMenu == null) {
            try {
                mainMenu = new FXMLLoader(getClass().getResource("/fxml/mainMenu.fxml")).load();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return mainMenu;
    }

    public AnchorPane GetBoard()
    {
        if (board == null) {
            try {
                board = new FXMLLoader(getClass().getResource("/fxml/Board/Board.fxml")).load();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return board;
    }

    public AnchorPane GetPaused() {
        if (paused == null) {
            try {
                paused = new FXMLLoader(getClass().getResource("/fxml/paused.fxml")).load();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return paused;
    }

    public AnchorPane GetResults() {
        if (results == null) {
            try {
                results = new FXMLLoader(getClass().getResource("/fxml/resultScene.fxml")).load();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return results;
    }

    public AnchorPane GetLoadingScreen() {
        if (loadingScreen == null) {
            try {
                loadingScreen = new FXMLLoader(getClass().getResource("/fxml/multiplayerLoadingScreen.fxml")).load();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return loadingScreen;
    }

// ShowPane ==================================================
    public void showMainMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainMenu.fxml"));
        MainMenuController controller = new MainMenuController();
        loader.setController(controller);
        createStage(loader);
    }

    public void showGameWindow() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Board/Game.fxml"));
        GameController gameController = new GameController();
        loader.setController(gameController);
        createStage(loader);
    }

    public void showBoard() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Board/Game.fxml"));
        createStage(loader);
    }

    public void showPaused() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/paused.fxml"));
        createStage(loader);
    }

    public void showResults() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/resultScene.fxml"));
        createStage(loader);
    }

    public void showLoadingScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/multiplayerLoadingScreen.fxml"));
        createStage(loader);
    }

// Helper Methods ==================================================
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
