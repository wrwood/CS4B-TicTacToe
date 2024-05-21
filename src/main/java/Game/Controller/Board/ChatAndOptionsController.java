package Game.Controller.Board;

import Game.Controller.PausedController;
import Game.Model.Model;
import Game.Model.Observer;
import Game.Util.ChatManager;
import GameServer.Model.MessageType;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatAndOptionsController implements Initializable, Observer {

    @FXML public TextFlow chatDisplay;
    @FXML private Pane scalePane;
    @FXML private ScrollPane scrollPane;
    @FXML private TextField chatInput;
    @FXML private Button sendButton;
    @FXML private TextArea ChatTextField;
    @FXML private TextField SendMessageEntry;
    @FXML private Button quitButton;

    private ChatManager chatManager;

    public AnchorPane ChatOptionRoot;
    @FXML private Button pauseButton;

    private Stage pauseMenu;


    @FXML
    private void sendMessage() {
        String text = chatInput.getText().trim();
        chatManager.addMessage("Sender", text, Color.GREEN);
        if (!text.isEmpty()) {
            Model.getInstance().sendMessage(MessageType.CHAT, (Object) text);
            chatInput.clear();
        }
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
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
    }

    // Pause ==================================================
    private void pauseGame() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/paused.fxml"));
            Parent root = loader.load();

            if (pauseMenu == null) {
                pauseMenu = new Stage();
                pauseMenu.initModality(Modality.APPLICATION_MODAL);
                pauseMenu.initOwner(pauseButton.getScene().getWindow());
                pauseMenu.initStyle(StageStyle.TRANSPARENT);
            }

            Scene pauseMenuScene = new Scene(root);
            pauseMenuScene.setFill(Color.TRANSPARENT);
            pauseMenu.setScene(pauseMenuScene);

            pauseMenu.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeBoard() {
        Stage stage = (Stage) pauseButton.getScene().getWindow();
        //Board.Co unregisterObservers();
        Model.getInstance().getViewFactory().closeStage(stage);
    }

    public void resumeGame() {
        if (pauseMenu != null)
        {
            pauseMenu.hide();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().registerObserver("message", this);
        Model.getInstance().registerObserver("event", this);

        TextFlow chatDisplay = new TextFlow();
        textFlowInit(chatDisplay);

        chatManager = new ChatManager(chatDisplay, scrollPane);
        chatManager.addMessage("System", "Welcome to the chat!", Color.RED);

        pauseButton.setOnAction(e -> pauseGame());
        sendButton.setOnAction(e -> sendMessage());
        chatInput.setOnAction(e -> sendMessage());
        quitButton.setOnAction(e->{
            System.exit(0);
        });
    }
}
