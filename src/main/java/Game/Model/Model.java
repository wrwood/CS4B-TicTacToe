package Game.Model;

import Game.Util.*;
import Game.Views.ViewFactory;
import GameServer.Model.MessageType;
import GameServer.Model.Message;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static GameServer.Model.MessageType.START;
import static GameServer.Model.MessageType.UPDATE_BOARD;

/*
    Structured as

    EventManager    Manages and notifies of events
    GameLogic       Methods that have to do with the game model
    Getters         
 */

public class Model {
    private static Model model;
    private ViewFactory viewFactory;

    private Map<String, List<Observer>> observersMap;

    private Boolean isPlayer1Turn;
    private Boolean isPlayer2Turn;
    private char player1Marker;
    private char player2Marker;
    private char actingPlayerMarker;

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private int gamePort; 

    private Boolean clientIsPlayer1;

    int[] winningLine;

    private GameModes gameMode;
    public GameResult gameResult;

    private GameBoard gameBoard;

    private int player1Score;
    private int player2Score;
    private int tieScore;

    private Model() {
        observersMap = new HashMap<>();
        this.viewFactory = new ViewFactory();
        isPlayer1Turn = true;
        isPlayer2Turn = false;
        player1Marker = Config.DEFAULT_PLAYER1_MARKER.charAt(0);
        player2Marker = Config.DEFAULT_PLAYER2_MARKER.charAt(0);
        actingPlayerMarker = player1Marker;
        gameMode = Config.DEFAULT_GAME_MODE;
        clientIsPlayer1 = true;

        gameBoard = new GameBoard();

        player1Score = 0;
        player2Score = 0;
        tieScore = 0;
    }

    // GameLogic ==================================================
    public void makeMove(int cellNumber) {
        switch (gameMode) {
            case SINGLE_PLAYER: 
                gameBoard.placeMarker(cellNumber, player1Marker);
                break;
            case ONLINE_MULTIPLAYER: 
                gameBoard.placeMarker(cellNumber, player1Marker);
                sendMessage(MessageType.PLAYER_MOVE, cellNumber);
                break;
            default:
        }
    }

    
    public void getOtherPlayerMove() { // Updates BoardController when a move is decided 
        switch (gameMode) {
            case SINGLE_PLAYER: notifyObservers(Config.PLAYER2_MOVE, gameBoard.computerPlay(player2Marker));   
                break;
            case LOCAL_MULTIPLAYER: notifyObservers(Config.PLAYER_TURN,  true);
                break;
        }
    }

    public void switchPlayerTurn() {
        isPlayer1Turn = !isPlayer1Turn;
        isPlayer2Turn = !isPlayer2Turn;
        // Updates BoardController when it is the active player turn to enable interaction with the board 
        if(isPlayer1Turn) {
            notifyObservers(Config.PLAYER_TURN,  true);
        }
    }

    public boolean isGameOver() {
        return gameBoard.checkWin() != ' ';
    }

    public void handleGameOver() {
        setGameResult(gameBoard.checkWin());
        notifyObservers(Config.GAME_OVER, gameResult);

        // Resets for a new game 
        gameBoard = new GameBoard();
        isPlayer1Turn = true;
        isPlayer2Turn = false;
    }

    private void setGameResult(char winner) {
        if (winner == 't') {
            gameResult = GameResult.TIE;
            tieScore++;
        } else if (winner == player1Marker) {
            gameResult = GameResult.WIN;
            player1Score++;
        } else if (winner == player2Marker) {
            gameResult = GameResult.LOSS;
            player2Score++;
        }

        notifyObservers(Config.SCORE_UPDATE, new int[]{player1Score, player2Score, tieScore});
    }

    // EventManager ==================================================
    public void registerObserver(String eventType, Observer observer) {
        observersMap.computeIfAbsent(eventType, k -> new ArrayList<>()).add(observer);
    }

    public void removeObserver(String eventType, Observer observer) {
        List<Observer> observers = observersMap.get(eventType);
        if (observers != null) {
            observers.remove(observer);
        }
    }

    public void notifyObservers(String eventType, Object data) {
        List<Observer> observers = observersMap.get(eventType);
        if (observers != null) {
            List<Observer> observersCopy = new ArrayList<>(observers);
            for (Observer observer : observersCopy) {
                observer.update(eventType, data);
            }
        }
    }

    // Getters ==================================================
    public static synchronized Model getInstance()
    {
        if (model == null) {
            model = new Model();
        }
        return model;
    }

    public ViewFactory getViewFactory()
    {
        return viewFactory;
    }

    public char getActingPlayerMarker() {
        return actingPlayerMarker;
    }

    public int[] getWinningLine() {
        return winningLine;
    }

    public boolean getIsPlayer1() {
        return clientIsPlayer1;
    }

    public GameModes getGameMode() {
        return gameMode;
    }

    public void setWinningLine(int[] winningLine) {
        this.winningLine = winningLine;
    }

    public void connectToServer(String serverAddress, int port) {
        try {
            socket = new Socket(serverAddress, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
    
            Thread receiveMessagesThread = new Thread(() -> {
                try {
                    Message receivedMessage;
                    while (!Thread.currentThread().isInterrupted() && socket.isConnected()) {
                        receivedMessage = (Message) in.readObject();
                        handleMessage(receivedMessage);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    disconnectFromServer();
                }
            });
            receiveMessagesThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleMessage(Message message) {
        MessageType messageType = message.getType();
        Object content = message.getContent();
        switch (messageType) {
            case START:                
                if((boolean) content == false) {
                    clientIsPlayer1 = false;
                } 
                notifyObservers("gameStart", "gameStart");
                gameMode = GameModes.ONLINE_MULTIPLAYER;
                break;
            case CHAT:
                notifyObservers("message", content);
                break;
            case UPDATE_BOARD:
                gameBoard.placeMarker((int) content, player2Marker);
                notifyObservers(Config.PLAYER2_MOVE, content);
                notifyObservers(Config.PLAYER_TURN, true);
                break;
            case GAME_RESULT:
                Platform.runLater(() -> {
                    System.out.println("asdfl;kajsd;flkjas;dlfkj :::::::::::: " + gameBoard.checkWin());
                    char winner = (char) content;
                    setGameResult(winner);
                    notifyObservers(Config.GAME_OVER, gameResult);
                    gameBoard = new GameBoard();
                    isPlayer1Turn = true;
                    isPlayer2Turn = false;
                });
                break;
            default:
        }
    }

    public void disconnectFromServer() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            notifyObservers("event", "Disconnected from server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(MessageType messageType, Object Message) {
        Message message = new Message(messageType, Message);
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resetGameState() {
        gameBoard = new GameBoard();
        isPlayer1Turn = true;
        isPlayer2Turn = false;
        actingPlayerMarker = player1Marker;
        gameResult = null;
        notifyObservers(Config.GAME_RESET, null);
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public int getTieScore() {
        return tieScore;
    }
}
