package GameServer.Model;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MatchManager {
    private List<ClientHandler> waitingPlayers;
    private List<Match> matches;

    public MatchManager() {
        waitingPlayers = new ArrayList<>();
        matches = new ArrayList<>();
    }

    public synchronized void addPlayer(Socket clientSocket) {
        ClientHandler clientHandler = new ClientHandler(clientSocket);
        waitingPlayers.add(clientHandler);
        clientHandler.start();
        matchPlayers();
    }

    public synchronized void removePlayer(ClientHandler clientHandler) {
        waitingPlayers.remove(clientHandler);
    }

    public synchronized void matchPlayers() {
        if (waitingPlayers.size() >= 2) {
            ClientHandler player1 = waitingPlayers.remove(0);
            ClientHandler player2 = waitingPlayers.remove(0);
            Match match = new Match(player1, player2);
            matches.add(match);
            match.startGame();
        }
    }

    public synchronized void cleanup() {
        for (ClientHandler waitingPlayer : waitingPlayers) {
            waitingPlayer.disconnect();
        }
        for (Match match : matches) {
            match.stop();
        }
        waitingPlayers.clear();
        matches.clear();
    }
}
