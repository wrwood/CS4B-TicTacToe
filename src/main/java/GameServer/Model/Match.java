package GameServer.Model;

import java.io.IOException;
import Game.Model.GameBoard;
import Game.Util.Config;
import Game.Util.GameResult;

public class Match {
    private ClientHandler player1;
    private ClientHandler player2;

    private GameBoard gameBoard;
    private ClientHandler actingPlayer;

    public Match(ClientHandler player1, ClientHandler player2) {
        this.player1 = player1;
        this.player2 = player2;
        player1.setMatch(this);
        player2.setMatch(this);

        gameBoard = new GameBoard();
        actingPlayer = player1;
        player1.setMarker(Config.DEFAULT_PLAYER1_MARKER.charAt(0)); 
        player2.setMarker(Config.DEFAULT_PLAYER2_MARKER.charAt(0)); 
    }

    public void startGame() {
        gameBoard = new GameBoard();
        Message startMessage1 = new Message(MessageType.START, true);
        Message startMessage2 = new Message(MessageType.START, false);
        sendMessage(startMessage1, player1);
        sendMessage(startMessage2, player2);
    }

    public void stop() {
        player1.disconnect();
        player2.disconnect();
    }

    public void sendMessage(Message message, ClientHandler receiver) {
        try {
            receiver.getOutputStream().writeObject(message);
            receiver.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToOpponent(Message message, ClientHandler sender) {
        synchronized (sender) {
            if(sender != player1) {
                sendMessage(message, player1);
            } else {
                sendMessage(message, player2);
            }
        }
    }

    public void handleMessage(Message message, ClientHandler sender) {
        MessageType messageType = message.getType();
        Object content = message.getContent();

        switch (messageType) {
            case CHAT:
                sendToOpponent(message, sender);
                break;
            case PLAYER_MOVE:
                if (sender == actingPlayer) {
                    gameBoard.placeMarker((int) content, sender.getMarker());
                    Message updateMessage = new Message(MessageType.UPDATE_BOARD, (int) content);
                    sendToOpponent(updateMessage, sender);

                    char winner = gameBoard.checkWin();
                    if (winner == ' ') {
                        switchActingPlayer();
                    } else {
                        gameBoard = new GameBoard();
                        handleGameOver(winner);
                    }
                }
                break;
            default:
        }
    }

    private void handleGameOver(char winner) {
        if (winner == player1.getMarker()) {
            sendMessage(new Message(MessageType.GAME_RESULT, player1.getMarker()), player1);
            sendMessage(new Message(MessageType.GAME_RESULT, player2.getMarker()), player2);
        } else if (winner == player2.getMarker()) {
            sendMessage(new Message(MessageType.GAME_RESULT, player2.getMarker()), player1);
            sendMessage(new Message(MessageType.GAME_RESULT, player1.getMarker()), player2);
        } else if (winner == 't') {
            sendMessage(new Message(MessageType.GAME_RESULT, 't'), player1);
            sendMessage(new Message(MessageType.GAME_RESULT, 't'), player2);
        }
    }

    private void switchActingPlayer() {
        if(actingPlayer == player1) {
            actingPlayer = player2;
        } else {
            actingPlayer = player1;
        }
    }
}
