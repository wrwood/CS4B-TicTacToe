package GameServer.Model;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private MessageType type;
    private Object content;

    public Message(MessageType type, Object content) {
        this.type = type;
        this.content = content;
    }

    public MessageType getType() {
        return type;
    }

    public Object getContent() {
        return content;
    }
}