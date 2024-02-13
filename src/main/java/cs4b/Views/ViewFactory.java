package cs4b.Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

/*
    Structured as

    GetPane     Prevents duplication of panes if already exists
    ShowPane    Will load stages

    Helper Methods
 */

public class ViewFactory {
    AnchorPane mainMenu;
    AnchorPane board;

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
                board = new FXMLLoader(getClass().getResource("/fxml/board.fxml")).load();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return board;
    }

// ShowPane ==================================================
    public void showMainMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainMenu.fxml"));
        createStage(loader);
    }

    public void showBoard() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/board.fxml"));
        createStage(loader);
    }

// Helper Methods ==================================================
    private void createStage(FXMLLoader loader) throws IOException {
        Scene scene = null;
        scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Tic Tac Toe");
        stage.show();
    }
    public void closeStage(Stage stage){
        stage.close();
    }
}
