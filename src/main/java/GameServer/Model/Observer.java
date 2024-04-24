package GameServer.Model;

public interface Observer {
    void update(String eventType, Object data);
}