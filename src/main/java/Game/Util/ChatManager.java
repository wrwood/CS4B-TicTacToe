package Game.Util;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ChatManager {
    private TextFlow chatDisplay;
    private ScrollPane scrollPane;

    // Temp
    private Map<String, Color> userColors = new HashMap<>();
    private Set<String> mutedUsers = new HashSet<>();

    private double chatFontSize = 12.0;

    public ChatManager(TextFlow chatDisplay, ScrollPane scrollPane) {
        this.chatDisplay = chatDisplay;
        this.scrollPane = scrollPane;
    }


    public void addMessage(String playerName, String message, Color nameColor) {
        if (!message.startsWith("/") && mutedUsers.contains(playerName)) {
            displaySystemMessage("You are muted and cannot send messages.");
            return;
        }

        if (message.startsWith("/")) {
            processCommand(playerName, message);
        } else {
            animateMessage(playerName, message, nameColor);
        }
    }

    // Commands ==================================================================
    private void processCommand(String playerName, String command) {
        String[] parts = command.substring(1).split(" ", 2);
        String commandBase = parts[0].toLowerCase();
        String commandArgs = parts.length > 1 ? parts[1] : "";

        switch (commandBase) {
            case "help":
                helpCommand();
                break;
            case "fontsize":
                try {
                    chatFontSize = Double.parseDouble(commandArgs);
                    updateChatDisplayFontSize();
                    displaySystemMessage("Chat font size updated to " + chatFontSize + ".");
                } catch (NumberFormatException e) {
                    displaySystemMessage("Invalid font size.");
                }
                break;
            case "color":
                colorCommand(playerName, commandArgs);
                break;
            case "whisper":
                // TODO Implement whisper, currently only states whisper
                displayMessage(playerName + " (whisper)", commandArgs, Color.GRAY);
                break;
            case "mute":
                mutedUsers.add(commandArgs);
                displaySystemMessage(commandArgs + " has been muted.");
                break;
            case "unmute":
                mutedUsers.remove(commandArgs);
                displaySystemMessage(commandArgs + " has been unmuted.");
                break;
            case "users":
                // TODO Implement user, currently only states users
                String usersList = String.join(", ", mutedUsers);
                displaySystemMessage("Online Users: " + usersList);
                break;
            case "me":
                displayActionMessage(playerName, commandArgs);
                break;
            default:
                displayMessage("System", "Unknown command: " + command, Color.RED);
        }
    }

    private void colorCommand(String playerName, String commandArgs) {
        try {
            Color newColor = Color.valueOf(commandArgs.toUpperCase());
            userColors.put(playerName, newColor);
            displayMessage("System", playerName + "'s color changed.", Color.GRAY);
        } catch (IllegalArgumentException e) {
            displayMessage("System", "Invalid color.", Color.RED);
        }
    }

    private void helpCommand() {
        String helpMessage = "Available commands: " +
                "/help - Show this message " +
                "/color <color> - Change your text color. " +
                "/fontsize <size> - Change the font size of the chat. " +
                "/whisper <user> <message> - Send a private message to a user. " +
                "/mute <user> - Mute a user. Their messages will no longer appear in the chat. " +
                "/unmute <user> - Unmute a user. Their messages will appear in the chat again. " +
                "/users - List all users currently in the chat. " +
                "/me <action> - Perform an action.";
            displaySystemMessage(helpMessage);
    }
    // Commands =END==============================================================

    private void updateChatDisplayFontSize() {
        Platform.runLater(() -> {
            chatDisplay.getChildren().forEach(node -> {
                if (node instanceof Text) {
                    ((Text) node).setFont(Font.font(chatFontSize));
                }
            });
        });
    }

    private void displayMessage(String playerName, String message, Color nameColor) {
        Platform.runLater(() -> {
            Text nameText = new Text(playerName + ": ");
            nameText.setFill(nameColor);
            nameText.setFont(Font.font(chatFontSize));

            Text messageText = new Text(message + "\n");
            messageText.setFill(Color.BLACK);
            messageText.setFont(Font.font(chatFontSize));

            chatDisplay.getChildren().addAll(nameText, messageText);
            autoScroll();
        });
    }

    private void displaySystemMessage(String message) {
        displayMessage("System", message, Color.GRAY);
    }

    private void displayActionMessage(String playerName, String action) {
        Platform.runLater(() -> {
            Text actionText = new Text("*" + playerName + " " + action + "*\n");
            actionText.setFill(Color.DARKGRAY);

            chatDisplay.getChildren().add(actionText);
            autoScroll();
        });
    }

    private void autoScroll() {
        Platform.runLater(() -> scrollPane.setVvalue(1.0));
    }

    // Animation ===================================================================
    private void animateMessage(String playerName, String message, Color nameColor) {
        int timing = 50;

        Platform.runLater(() -> {
            Text nameText = new Text(playerName + ": ");
            nameText.setFill(userColors.getOrDefault(playerName, nameColor));
            nameText.setFont(Font.font(chatFontSize));
            chatDisplay.getChildren().add(nameText);

            final int[] i = {0};
            String[] chars = message.split("");
            PauseTransition pause = new PauseTransition(Duration.millis(timing));

            Runnable addChar = () -> {
                if (i[0] < chars.length) {
                    Text messageText = new Text(chars[i[0]]);
                    messageText.setFill(Color.BLACK);
                    messageText.setFont(Font.font(chatFontSize));
                    chatDisplay.getChildren().add(messageText);
                    i[0]++;
                    pause.playFromStart();
                } else {
                    chatDisplay.getChildren().add(new Text("\n"));
                    autoScroll();
                }
            };

            pause.setOnFinished(event -> addChar.run());
            addChar.run();
        });
    }
}