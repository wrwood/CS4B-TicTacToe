package GameServer;

import GameServer.Model.Model;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class GameServer extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Model.getInstance().getViewFactory().showView();

        Model.getInstance().getViewFactory().getStage().setOnCloseRequest(event-> {
            Model.getInstance().stopServer();
        });
    }

    public static void main(String[] args) {
        launch();
    }

}