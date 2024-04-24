package Game.Controller;

import Game.Model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PausedController implements Initializable {

    private BoardController boardController;

    public BorderPane pausedRoot;
    @FXML private Button exitGameButton;
    @FXML private Button optionsButton;
    @FXML private Button resumeButton;

    @FXML
    void exitProgram(ActionEvent event) {

		System.exit(0);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resumeButton.setOnAction(e-> {
            boardController.resumeGame();
        });
        exitGameButton.setOnAction(e->{
            System.exit(0);
        });
    }

    public void setBoardController(BoardController boardController) {
        this.boardController = boardController;
    }

    
    private void closeMenu() {
        Stage stage = (Stage)resumeButton.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        try {
            Model.getInstance().getViewFactory().showBoard();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
