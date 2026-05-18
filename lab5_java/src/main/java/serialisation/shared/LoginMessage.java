package serialisation.shared;

public class LoginMessage extends Message {
    private final String username;

    public LoginMessage(String username) {
        this.username = username;
    }

    public String getUsername() { return username; }
}
