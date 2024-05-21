package Game.Controller.Board;

import Game.Model.Model;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().setGameController(this);
    }
}
