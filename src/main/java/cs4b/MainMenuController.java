package cs4b;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainMenuController {

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

}
