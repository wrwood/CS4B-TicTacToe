package GameServer.Model;

import GameServer.Views.ViewFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Model {
    private static Model model;
    private ViewFactory viewFactory;

    private Map<String, List<Observer>> observersMap;

    private Thread serverThread;
    private ServerSocket serverSocket;
    private MatchManager matchManager;

    private Model() {
        this.viewFactory = new ViewFactory();
        observersMap = new HashMap<>();
    }

    public static synchronized Model getInstance()
    {
        if (model == null) {
            model = new Model();
        }
        return model;
    }

    public void startServer(int serverPort) {
        serverThread = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(serverPort);
                notifyObservers("message", "Server started on port " + serverPort);
                matchManager = new MatchManager();
    
                while (!Thread.currentThread().isInterrupted()) {
                    Socket clientSocket = serverSocket.accept();
                    notifyObservers("message", "New client connected: " + clientSocket);
                    matchManager.addPlayer(clientSocket);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (serverSocket != null && !serverSocket.isClosed()) {
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        serverThread.start();
    }
    
    //TODO handle resource cleanup and match results when a player unexpectantly diconnects
    public void stopServer() { 
        if (serverThread != null && serverThread.isAlive()) {
            serverThread.interrupt();
            try {
                matchManager.cleanup();

                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                matchManager = null;
                notifyObservers("message", "Server Stopped. ");
            }
        }
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
            List<Observer> observersCopy = new ArrayList<>(observers);
            for (Observer observer : observersCopy) {
                observer.update(eventType, data);
            }
        }
    }

    public ViewFactory getViewFactory() {
        return viewFactory;
    }
}
