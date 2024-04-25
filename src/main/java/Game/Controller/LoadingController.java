package Game.Controller;

import Game.Model.Model;
import Game.Model.Observer;
import Game.Views.ViewFactory;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoadingController implements Initializable, Observer {


    public Pane multiplayerPane;

    private volatile boolean gameStarting = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LoadingCircles();
        Model.getInstance().registerObserver("gameStart", this);
        Model.getInstance().connectToServer("localhost", 1234);
    }

    private void switchToGame() {
        try {
            Stage stage = (Stage) multiplayerPane.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);

            System.out.println("Observer removed");
            Model.getInstance().removeObserver("gameStart", this);

            Model.getInstance().getViewFactory().showGameWindow();
        } catch (IOException e) {
            System.err.println("Failed to open the game window: " + e.getMessage());
        }
    }

    @Override
    public void update(String eventType, Object data) {
        if ("gameStart".equals(eventType) && !gameStarting) {
            gameStarting = true;
            Platform.runLater(this::switchToGame);
        }
    }

    private void LoadingCircles() {
        Group circleGroup = new Group();

        // Parameters for the circles
        int radius = 5;
        int circleCount = 8;
        int circleLayoutRadius = 50;

        // Creating and positioning circles
        for (int i = 0; i < circleCount; i++) {
            double angle = 2 * Math.PI * i / circleCount;
            double x = Math.cos(angle) * circleLayoutRadius + circleLayoutRadius + radius;
            double y = Math.sin(angle) * circleLayoutRadius + circleLayoutRadius + radius;

            Circle circle = new Circle(x, y, radius, Color.WHITE);
            circleGroup.getChildren().add(circle);
        }
        multiplayerPane.getChildren().add(circleGroup);

        circleGroup.setLayoutX(231);
        circleGroup.setLayoutY(200);

        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(2), circleGroup);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
        rotateTransition.play();
    }
}
