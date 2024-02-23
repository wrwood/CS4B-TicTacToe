package cs4b.Model;

import cs4b.GameResult;
import cs4b.Views.ViewFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model {
    private static Model model;
    private ViewFactory viewFactory;
    private Map<String, List<Observer>> observersMap;
    private Boolean isPlayer1Turn;
    private Boolean isPlayer2Turn;
    private char player1Marker;
    private char player2Marker;
    private String gameMode;

    public GameResult gameResult;

    private GameBoard gameBoard;

    private Model()
    {
        observersMap = new HashMap<>();
        this.viewFactory = new ViewFactory();
        isPlayer1Turn = true;
        isPlayer2Turn = false;
        player1Marker = 'x';
        player2Marker = 'o';
        gameMode = "SinglePlayer";

        gameBoard = new GameBoard();
    }

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
            List<Observer> observersCopy = new ArrayList<>(observers); // Make a copy of the list
            for (Observer observer : observersCopy) { // Iterate over the copy
                observer.update(eventType, data);
            }
        }
    }

    public char getPlayer1Marker() {
        return player1Marker;
    }

    public void makeMove(int cellNumber) {
        gameBoard.placeMarker(cellNumber, player1Marker);

        if(gameBoard.checkWin() == ' ') {
            switchPlayerTurn();
            otherPlayerMove();
        } else {
            notifyGameStatus(gameBoard.checkWin());
        }
    }

    private void switchPlayerTurn() {
        isPlayer1Turn = !isPlayer1Turn;
        isPlayer2Turn = !isPlayer2Turn;
    }

    private void otherPlayerMove() {
        switch (gameMode) {
            case "SinglePlayer": computerMove();
                break;
            case "OnlineMultiplayer":
                break;
            case "LocalMultiplayer":
                break;
        }

        if(gameBoard.checkWin() == ' ') {
            switchPlayerTurn();
        } else {
            notifyGameStatus(gameBoard.checkWin());
        }
    }

    private void computerMove() {
        int computerMove = gameBoard.computerPlay(player2Marker);
        notifyObservers("Player2Move", computerMove);
    }

    private void notifyGameStatus(char winner) {
        if (winner != ' ') {
            notifyObservers("Win", winner);
            if (winner == 'x') {
                gameResult = GameResult.Win;
            } else if (winner =='o') {
                gameResult = GameResult.Loss;
            }
        } else if (winner == 't') {
            notifyObservers("Tie", null);
            gameResult = GameResult.Tie;
        }
        gameBoard = new GameBoard();
    }

    // Synchronized prevents
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

}
