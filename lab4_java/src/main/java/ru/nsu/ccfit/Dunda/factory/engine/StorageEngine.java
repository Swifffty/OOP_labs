package ru.nsu.ccfit.Dunda.factory.engine;

import java.util.ArrayDeque;
import java.util.Queue;

public class StorageEngine {
    private final int capacity;
    private final Queue<Engine> storage = new ArrayDeque<>();
    public StorageEngine(int capacity) {
        this.capacity = capacity;
    }
    public synchronized void supply(Engine engine) throws InterruptedException {
            if (storage.size() == capacity) {
                wait();
            }
            storage.add(engine);
            notifyAll();
    }
    public synchronized Engine getEngine() throws InterruptedException {
            if (storage.isEmpty()) {
                wait();
            }
            Engine engine = storage.remove();
        notifyAll();
            return engine;

    }
}
