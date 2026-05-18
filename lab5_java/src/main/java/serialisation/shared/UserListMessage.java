package serialisation.shared;

import java.util.ArrayList;
import java.util.List;

public class UserListMessage extends Message{
    private final List<String> users;

    public UserListMessage(List<String> users) {
        this.users = users;
    }

    public List<String> getUsers() {
        return new ArrayList<>(users);
    }
}