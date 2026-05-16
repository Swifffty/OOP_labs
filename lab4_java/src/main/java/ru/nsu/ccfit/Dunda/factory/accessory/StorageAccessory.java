package ru.nsu.ccfit.Dunda.factory.accessory;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.Dunda.factory.ControllerStorage;

import java.util.ArrayDeque;
import java.util.Queue;

public class StorageAccessory {
    public final int capacity;
    private final Queue<Accessory> storage = new ArrayDeque<>();
    private final Logger log = LogManager.getLogger(StorageAccessory.class);
    private final Object controllerObject;

    public StorageAccessory(int capacity, Object controllerObject) {
        this.capacity = capacity;
        this.controllerObject = controllerObject;
    }

    public void supply(Accessory accessory) throws InterruptedException {
        synchronized (this) {
            while (storage.size() == capacity) {
                log.info("Склад accessory переполнен");
                wait();
            }
            storage.add(accessory);
            log.info("На склад добавили новый accessory с id: " + accessory.id);
            notifyAll();
        }
        synchronized (controllerObject) {
            controllerObject.notifyAll();
        }
    }

    public synchronized Accessory getAccessory() throws InterruptedException {
        while (storage.isEmpty()) {
            log.info("Склад с accessory пустой");
            wait();
        }
        Accessory accessory = storage.remove();
        log.info("Забрали со склада accessory с id: " + accessory.id);
        notifyAll();
        return accessory;
    }

    public synchronized boolean isEmpty() {
        return storage.isEmpty();
    }

    public int currentSize() {
        return storage.size();
    }

}
