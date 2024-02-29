package cs4b.Model;

import cs4b.config.GameResult;

public class GameBoard {
    private char[][] gameBoard = new char[3][3];

    public GameBoard() {
        for (int row = 0; row < 3; row++) {
            for(int col = 0; col < 3; col++) {
                gameBoard[row][col] = ' ';
            }
        }
    }

    public void placeMarker(int cellNumber, char marker) {
        int row = cellNumber / 3;
        int col = cellNumber % 3;

        gameBoard[row][col] = marker;
    }

    public int computerPlay(char marker) {
        for (int row = 0; row < 3; row++) {
            for(int col = 0; col < 3; col++) {
                if(gameBoard[row][col] == ' ') {
                    gameBoard[row][col] = marker;
                    return row * 3 + col;
                }
            }
        }
        return 0;
    }

    public char checkWin() {
        char winner = ' ';

        if (isBoardFull()) {
            winner = 't';
            Model.getInstance().gameResult = GameResult.TIE;
        }

        for (int i = 0; i < 3; i++) {
            if (checkEqual(gameBoard[i][0], gameBoard[i][1], gameBoard[i][2])) {
                winner = gameBoard[i][0];
                break;
            }
            if (checkEqual(gameBoard[0][i], gameBoard[1][i], gameBoard[2][i])) {
                winner = gameBoard[0][i];
                break;
            }
        }

        if (checkEqual(gameBoard[0][0], gameBoard[1][1], gameBoard[2][2]) ||
                checkEqual(gameBoard[0][2], gameBoard[1][1], gameBoard[2][0])) {
            winner = gameBoard[1][1];
        }

        return winner;
    }

    private boolean checkEqual(char a, char b, char c) {
        return a != ' ' && a == b && b == c;
    }

    private boolean isBoardFull() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (gameBoard[row][col] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }
}

