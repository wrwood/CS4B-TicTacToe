package cs4b.Controller;

import cs4b.Model.Model;
import cs4b.config.GameResult;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ResultController implements Initializable {

    public BorderPane resultRoot;
    public Label resultLabel;
    @FXML private Button playAgainButton;
    @FXML private Button exitGameButton;
    @FXML private Button mainMenuButton;

    @FXML
    void exitProgram(ActionEvent event) {

		System.exit(0);

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        switch (Model.getInstance().gameResult) {
            case WIN:
                resultLabel.setText("You won!");
                VictoryEffect();
                break;
            case TIE:
                resultLabel.setText("It's a tie!");
                break;
            default:
                resultLabel.setText("You lost!");
                break;
        }

        playAgainButton.setOnAction(e-> {
            Model.getInstance().gameResult = GameResult.LOSS;
            closeResults();
        });
        mainMenuButton.setOnAction(e->{
            showMainMenu();
        });
        exitGameButton.setOnAction(e->{
            System.exit(0);
        });

    }

    private void VictoryEffect() {
        for (int i = 0; i < 20; i++) {
            double xPos = Math.random() * resultRoot.getPrefWidth();
            double yPos = Math.random() * resultRoot.getPrefHeight();

            Circle circle = new Circle(xPos, yPos, Math.random() * 200 + 50, Color.WHITE);
            circle.setOpacity(Math.random() * 0.5 + 0.1);
            circle.setEffect(new GaussianBlur(Math.random() * 15 + 5));

            circle.setMouseTransparent(true);

            resultRoot.getChildren().add(0,circle);
        }
    }

    private void showMainMenu() {
        Stage stage = (Stage)mainMenuButton.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        try {
            Model.getInstance().getViewFactory().showMainMenu();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private void closeResults() {
        Stage stage = (Stage)playAgainButton.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        try {
            Model.getInstance().getViewFactory().showBoard();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
