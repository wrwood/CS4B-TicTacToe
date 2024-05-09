package GameServer.Model;

import java.io.IOException;

public class Match {
    private ClientHandler player1;
    private ClientHandler player2;
    //TODO implement gamelogic class

    public Match(ClientHandler player1, ClientHandler player2) {
        this.player1 = player1;
        this.player2 = player2;
        player1.setMatch(this);
        player2.setMatch(this);
    }

    public void startGame() {
        Message startMessage = new Message(MessageType.START, "");
        sendMessage(startMessage, player1);
        sendMessage(startMessage, player2);
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
        //Object content = message.getContent();
        //TODO implement the rest of the messages here
        switch (messageType) {
            case CHAT:
                sendToOpponent(message, sender);
                break;
            default: 
        }
    }
}
