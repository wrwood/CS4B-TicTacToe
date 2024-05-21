package Game.Controller.Board;

import Game.Model.Model;
import Game.Model.Observer;
import Game.Util.Config;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ScoreBoardController implements Initializable, Observer {

    @FXML public Label player1Score;
    @FXML public Label player2Score;
    @FXML public Label gameTieScore;
    @FXML public Label moveTimer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().registerObserver(Config.SCORE_UPDATE, this);
        updateScores();
    }

    @Override
    public void update(String eventType, Object data) {
        if (eventType.equals(Config.SCORE_UPDATE)) {
            updateScores();
        }
    }

    private void updateScores() {
        player1Score.setText(String.valueOf(Model.getInstance().getPlayer1Score()));
        player2Score.setText(String.valueOf(Model.getInstance().getPlayer2Score()));
        gameTieScore.setText(String.valueOf(Model.getInstance().getTieScore()));
    }
}
