package Game;

import Game.Model.Model;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Model.getInstance().getViewFactory().showMainMenu();
    }

    public static void main(String[] args) {
        launch();
    }

}