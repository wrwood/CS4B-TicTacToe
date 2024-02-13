package cs4b.Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewFactory {
    AnchorPane mainMenu;

    public ViewFactory()
    {

    }

    public AnchorPane GetMainMenu()
    {
        if (mainMenu == null) {
            try {
                mainMenu = new FXMLLoader(getClass().getResource("/fxml/mainMenu.fxml")).load();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return mainMenu;
    }

    public void showMainMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainMenu.fxml"));
        createStage(loader);
    }

    private void createStage(FXMLLoader loader) throws IOException {
        Scene scene = null;
        scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Tic Tac Toe");
        stage.show();
    }
    public void closeStage(Stage stage){
        stage.close();
    }
}
