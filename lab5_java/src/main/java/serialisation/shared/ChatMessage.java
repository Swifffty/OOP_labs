package serialisation.shared;

public class ChatMessage extends Message {
    private final String sender;
    private final String text;
    private final String recipient;

    public ChatMessage(String sender, String text) {
        this.sender = sender;
        this.text = text;
        this.recipient = null;
    }

    public ChatMessage(String sender, String text, String recipient) {
        this.sender = sender;
        this.text = text;
        this.recipient = recipient;
    }

    public String getSender() { return sender; }
    public String getText() { return text; }
    public String getRecipient() { return recipient; }
}
