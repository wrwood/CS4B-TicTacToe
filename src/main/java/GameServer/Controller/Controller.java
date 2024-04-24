package GameServer.Controller;

import GameServer.Model.Model;
import GameServer.Model.Observer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

public class Controller implements Observer {
    @FXML
    private TextField PortEntry;

    @FXML
    private TextArea ServerTextField;

    @FXML
    private Button StartButton;

    @FXML
    private void handleStartButtonClick(ActionEvent event) {
        String portString = PortEntry.getText();
        if (!portString.isEmpty()) {
            int port = Integer.parseInt(portString);
            Model.getInstance().startServer(port);
        } else {
            printToServerTextField("Please enter a port number.");
        }
    }

    @FXML
    private void handleStopButtonClick(ActionEvent event) {
        Model.getInstance().stopServer();
    }

    @FXML
    public void initialize() {
        Model.getInstance().registerObserver("message", this);
    }

    @Override
    public void update(String eventType, Object data) {
        switch (eventType) {
            case "message" : printToServerTextField(data.toString());
        }
    }

    public void printToServerTextField(String line) {
        ServerTextField.appendText(line + "\n");
    }
}
