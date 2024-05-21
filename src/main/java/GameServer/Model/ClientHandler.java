package GameServer.Model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Match currentMatch;
    private char marker;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        try {
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            inputStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            Message message;
            while (!Thread.currentThread().isInterrupted()) {
                message = (Message) inputStream.readObject();
                currentMatch.handleMessage(message, this);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            this.interrupt();
            if(inputStream != null) {
                inputStream.close();
            }
            if(outputStream != null) {
                outputStream.close();
            }
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public char getMarker() {
        return marker;
    }

    public void setMatch(Match match) {
        this.currentMatch = match;
    }

    public void setMarker(char marker) {
        this.marker = marker;
    }
}
