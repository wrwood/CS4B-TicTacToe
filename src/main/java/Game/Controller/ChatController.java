package Game.Controller;

import Game.Model.*;
import Game.Util.ChatManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.TextFlow;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;

public class ChatController implements Observer {
    @FXML private Pane scalePane;
    @FXML private ScrollPane scrollPane;
    @FXML private TextField chatInput;
    @FXML private Button sendButton;
    @FXML private TextField PortEntry;
    @FXML private TextField ServerIPEntry;
    @FXML private TextArea ChatTextField;
    @FXML private TextField SendMessageEntry;

    private ChatManager chatManager;

    @FXML
    private void sendMessage(ActionEvent event) {
        String messageString = SendMessageEntry.getText();
        Model.getInstance().sendMessage(messageString);
        SendMessageEntry.clear();
    }
    
    @FXML
    private void handleServerConnection(ActionEvent event) {
        String portString = PortEntry.getText();
        String ipString = ServerIPEntry.getText();

        if (!portString.isEmpty() && !ipString.isEmpty()) {
            int port = Integer.parseInt(portString);
            Model.getInstance().connectToServer(ipString, port);
        } else {
            chatManager.addMessage("System", "Please enter a port and IP.", Color.RED);
        }
    }

    @FXML
    private void handleServerDisconnect(ActionEvent event) {
        Model.getInstance().disconnectFromServer();
    }

    @FXML
    private Button StopButton;

    @FXML
    private void sendMessage() {
        String text = chatInput.getText().trim();
        if (!text.isEmpty()) {
            Model.getInstance().sendMessage(text);
            chatInput.clear();
        }
    }

    @FXML
    public void initialize() {
        Model.getInstance().registerObserver("message", this);
        Model.getInstance().registerObserver("event", this);

        TextFlow textFlow = new TextFlow();
        textFlowInit(textFlow);

        chatManager = new ChatManager(textFlow, scrollPane);
        chatManager.addMessage("System", "Welcome to the chat, please join a server.", Color.RED);

        sendButton.setOnAction(e -> sendMessage());
        chatInput.setOnAction(e -> sendMessage());
    }

    @Override
    public void update(String eventType, Object data) {
        switch (eventType) {
            case "message" : chatManager.addMessage("Received", data.toString(), Color.BLUE);
                            break;
            case "event" : chatManager.addMessage("System", data.toString(), Color.RED);
        }
    }

    private void textFlowInit(TextFlow textFlow) {
        textFlow.prefWidthProperty().bind(scalePane.widthProperty());
        textFlow.prefHeightProperty().bind(scalePane.heightProperty());

        scrollPane.setContent(textFlow);
    }
}
