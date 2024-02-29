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
        int bestScore = Integer.MAX_VALUE;
        int bestMove = -1;
        char otherMarker = getOtherMarker(marker);

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (gameBoard[row][col] == ' ') {
                    gameBoard[row][col] = marker;
                    int score = minimax(0, true, Integer.MIN_VALUE, Integer.MAX_VALUE, marker, otherMarker);
                    gameBoard[row][col] = ' ';
                    if (score < bestScore) {
                        bestScore = score;
                        bestMove = row * 3 + col;
                    }
                }
            }
        }

        placeMarker(bestMove, marker);
        return bestMove;
    }

    private int minimax(int depth, boolean isMaximizing, int alpha, int beta, char computerMarker, char otherMarker) {
        char winner = checkWin();
        if (winner == otherMarker) {
            return 1;
        } else if (winner == computerMarker) {
            return -1;
        } else if (winner == 't') {
            return 0;
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (gameBoard[row][col] == ' ') {
                        gameBoard[row][col] = otherMarker; 
                        int score = minimax(depth + 1, false, alpha, beta, computerMarker, otherMarker);
                        gameBoard[row][col] = ' '; 
                        bestScore = Math.max(score, bestScore);                        
                        alpha = Math.max(alpha, bestScore);
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (gameBoard[row][col] == ' ') {
                        gameBoard[row][col] = computerMarker;  
                        int score = minimax(depth + 1, true, alpha, beta, computerMarker, otherMarker);
                        gameBoard[row][col] = ' '; 
                        bestScore = Math.min(score, bestScore);
                        beta = Math.min(beta, bestScore);
                        if (beta <= alpha) {
                            break; 
                        }
                    }
                }
            }
            return bestScore;
        }
    }

    public char checkWin() {
        char winner = ' ';

        for (int i = 0; i < 3; i++) {
            if (checkEqual(gameBoard[i][0], gameBoard[i][1], gameBoard[i][2]))
            {
                winner = gameBoard[i][0];
                Model.getInstance().setWinningLine(new int[]{i * 3, i * 3 + 1, i * 3 + 2});
                return winner;
            }
            if (checkEqual(gameBoard[0][i], gameBoard[1][i], gameBoard[2][i]))
            {
                winner = gameBoard[0][i];
                Model.getInstance().setWinningLine(new int[]{i, i + 3, i + 6});
                return winner;
            }
        }

        if (checkEqual(gameBoard[0][0], gameBoard[1][1], gameBoard[2][2]))
        {
            winner = gameBoard[0][0];
            Model.getInstance().setWinningLine(new int[]{0, 4, 8});
        }
        else if (checkEqual(gameBoard[0][2], gameBoard[1][1], gameBoard[2][0]))
        {
            winner = gameBoard[0][2];
            Model.getInstance().setWinningLine(new int[]{2, 4, 6});
        }

        if (isBoardFull()) {
            winner = 't';
            Model.getInstance().gameResult = GameResult.TIE;
            Model.getInstance().setWinningLine(null);
            return winner;
        }

        if (winner == ' ')
        {
            Model.getInstance().setWinningLine(null);
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

    public char getOtherMarker(char marker) {
        if(marker == 'x') {
            return 'o';
        } else {
            return 'x';
        }
    }
}

