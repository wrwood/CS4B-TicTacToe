package cs4b.Controller;

import cs4b.Model.Model;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML private GridPane gridPane;
    @FXML private Pane imagePane;


    @FXML private Button singlePlayerButton;
    @FXML private Button onlineButton;
    @FXML private Button localButton;
    @FXML private Button quitButton;


    @FXML
    void exitProgram(ActionEvent event) {

		System.exit(0);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image redEgg = new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                "/images/dragon_egg_png_overlay__by_lewis4721_de8r1hj-fullview.png")));
        ImageView redView = new ImageView(redEgg);
        Image blueEgg = new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                "/images/dragon_egg_png_overlay__by_lewis4721_de8r1hq-414w-2x.png")));
        ImageView blueView = new ImageView(blueEgg);
        Image orangeEgg = new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                "/images/dragon_egg_png_overlay__by_lewis4721_de8r1i1-414w-2x.png")));
        ImageView orangeView = new ImageView(orangeEgg);

        eggViewAdjustments(redView, imagePane);
        eggViewAdjustments(blueView, imagePane);
        eggViewAdjustments(orangeView, imagePane);

        int bottomLine = 300;

        redView.setLayoutX(100);
        redView.setLayoutY(bottomLine);
        blueView.setLayoutX(200);
        blueView.setLayoutY(bottomLine);
        orangeView.setLayoutX(300);
        orangeView.setLayoutY(bottomLine);

        redView.setRotate(redView.getRotate() - 35);
        //blueView.setRotate(0);
        orangeView.setRotate(orangeView.getRotate() + 45);

        gridPane.setStyle("-fx-background-color: #000000;");

        for (int i = 0; i < 10; i++) {
            double xPos = 325;
            double yPos = 350;

            Circle circle = new Circle(xPos, yPos, Math.random() * 200 + 50, Color.WHITE);
            circle.setOpacity(Math.random() * 0.1);
            circle.setEffect(new GaussianBlur(1));

            Glow glow = new Glow();
            glow.setLevel(Math.random() * 0.1 + 0.2);
            circle.setEffect(glow);

            imagePane.getChildren().add(circle);

            TranslateTransition transition = new TranslateTransition(Duration.seconds(3), circle);
            transition.setByY(15);
            transition.setCycleCount(TranslateTransition.INDEFINITE);
            transition.setAutoReverse(true);
            transition.play();
        }

        singlePlayerButton.setOnAction(e-> {
            getBoard();
        });
        onlineButton.setOnAction(e->{
            getLoadingScreen();
        });
        localButton.setOnAction(e->{
            getBoard();
        });
        quitButton.setOnAction(e->{
            System.exit(0);
        });
    }

    // Egg ImageView adjustments
    private void eggViewAdjustments(ImageView imageView, Pane imagePane) {
        imageView.setFitWidth(200);
        imageView.setPreserveRatio(true);

        glowAnimation(imageView);

        imagePane.getChildren().add(imageView);
    }

    // Glow Animation
    private void glowAnimation(ImageView imageView) {
        Glow glow = new Glow();

        glow.setLevel(0.9);
        imageView.setEffect(glow);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(glow.levelProperty(), 0.0)),
                new KeyFrame(Duration.seconds(1), new KeyValue(glow.levelProperty(), 0.9)),
                new KeyFrame(Duration.seconds(2), new KeyValue(glow.levelProperty(), 0.0))
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        timeline.play();
    }

    // Get Pane
    private void getBoard() {
        Stage stage = (Stage)singlePlayerButton.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        try {
            Model.getInstance().getViewFactory().showBoard();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void getLoadingScreen() {
        Stage stage = (Stage)onlineButton.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        try {
            Model.getInstance().getViewFactory().showLoadingScreen();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
