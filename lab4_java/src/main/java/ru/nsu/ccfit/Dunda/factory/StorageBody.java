package ru.nsu.ccfit.Dunda.factory;

import java.util.ArrayDeque;
import java.util.Queue;

public class StorageBody {
    public final int size;
    private Queue<Body> storage = new ArrayDeque<>();
    StorageBody(int size_conf) {
        size = size_conf;
    }

    public boolean supply(Body newBody) {
        if (storage.size() == size) {
            return true;
        }
        storage.add(newBody);
        return false;
    }
    public Body getBody() {
        if (size > 0) {
            return storage.remove();
        }
        return null;
    }
}
