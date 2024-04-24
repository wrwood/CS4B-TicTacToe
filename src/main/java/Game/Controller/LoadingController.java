package Game.Controller;

import javafx.animation.RotateTransition;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class LoadingController implements Initializable {


    public Pane multiplayerPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LoadingCircles();
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
