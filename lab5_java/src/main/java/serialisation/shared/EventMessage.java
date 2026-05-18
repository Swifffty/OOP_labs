package serialisation.shared;

public class EventMessage extends Message {
    private final String eventType;
    private final String username;

    public EventMessage(String eventType, String username) {
        this.eventType = eventType;
        this.username = username;
    }

    public String getEventType() { return eventType; }
    public String getUsername() { return username; }
}
