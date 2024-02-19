package cs4b.Controller;

import cs4b.Model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
    @FXML
    private Button playAgainButton;

    @FXML
    private Button exitGameButton;

    @FXML
    private Button mainMenuButton;

    @FXML
    void exitProgram(ActionEvent event) {

		System.exit(0);

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playAgainButton.setOnAction(e-> {
            closeResults();
        });
        mainMenuButton.setOnAction(e->{
            showMainMenu();
        });
        exitGameButton.setOnAction(e->{
            System.exit(0);
        });

    }

/*    private void VictoryEffect() {
        for (int i = 0; i < 20; i++) {
            double xPos = Math.random() * imagePane.getPrefWidth();
            double yPos = Math.random() * imagePane.getPrefHeight();

            Circle circle = new Circle(xPos, yPos, Math.random() * 200 + 50, Color.WHITE);
            circle.setOpacity(Math.random() * 0.5 + 0.1);
            circle.setEffect(new GaussianBlur(Math.random() * 15 + 5));

            imagePane.getChildren().add(circle);
        }
    }*/

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
