package GameServer.Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewFactory {
    AnchorPane View;
    Stage stage;

    public ViewFactory() {

    }

    public AnchorPane GetView() {
        if (View == null) {
            try {
                View = new FXMLLoader(getClass().getResource("ChatServer.fxml")).load();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return View;
    }

    public void showView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ChatServer.fxml"));

        createStage(loader);
    }

    private void createStage(FXMLLoader loader) throws IOException {
        Scene scene = new Scene(loader.load());
        stage = new Stage();
        stage.setScene(scene);

        stage.setTitle("Chat Server");
        stage.show();
    }



    public void closeStage(Stage stage) {
        stage.close();
    }

    public Stage getStage() {
        return stage;
    }
}
