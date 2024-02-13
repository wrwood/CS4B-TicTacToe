package cs4b.Controller;

import cs4b.Model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML
    private Button singlePlayerButton;
  
    @FXML
    private Button onlineButton;

    @FXML
    private Button localButton;

 	  @FXML
    private Button quitButton;

    @FXML
    void exitProgram(ActionEvent event) {

		System.exit(0);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        singlePlayerButton.setOnAction(e-> {
            getBoard();
        });
        onlineButton.setOnAction(e->{
            getBoard();
        });
        localButton.setOnAction(e->{
            getBoard();
        });
        quitButton.setOnAction(e->{
            System.exit(0);
        });
    }

    private void getBoard() {
        Stage stage = (Stage)singlePlayerButton.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        try {
            Model.getInstance().getViewFactory().showBoard();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
