package Game.Model;

public interface Observer {
    void update(String eventType, Object data);
}