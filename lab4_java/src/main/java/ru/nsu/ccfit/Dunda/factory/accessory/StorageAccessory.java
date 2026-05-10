package ru.nsu.ccfit.Dunda.factory.accessory;


import java.util.ArrayDeque;
import java.util.Queue;

public class StorageAccessory {
    private final int capacity;
    private final Queue<Accessory> storage = new ArrayDeque<>();

    public StorageAccessory(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void supply(Accessory accessory) throws InterruptedException {
        if (storage.size() == capacity) {
            wait();
        }
        storage.add(accessory);
        notifyAll();
    }

    public synchronized Accessory getAccessory() throws InterruptedException {
        if (storage.isEmpty()) {
            wait();
        }
        Accessory accessory = storage.remove();
        notifyAll();
        return accessory;
    }

}
