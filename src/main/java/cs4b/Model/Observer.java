package cs4b.Model;

public interface Observer {
    void update(String eventType, Object data);
}