package ru.nsu.ccfit.Dunda.factory.body;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Queue;

public class StorageBody {
    private final int capacity;
    private final Queue<Body> storage = new ArrayDeque<>();
    private final Logger log = LogManager.getLogger(StorageBody.class);

    public StorageBody(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void supply(Body newBody) throws InterruptedException {
        while (storage.size() >= capacity) {
            log.info("Ожидание освобождения места в StorageBody");
            wait();
        }
        storage.add(newBody);
        log.info("Добавление нового тела в хранилище");
        notifyAll();
    }

    public synchronized Body getBody() throws InterruptedException {
        while (storage.isEmpty()) {
            log.info("Ожиадние нового тела на складе");
            wait();
        }
        Body body = storage.remove();
        log.info("Удаление старого тела со склада");
        notifyAll();
        return body;
    }

    public synchronized int getCurrentSize() {
        return storage.size();
    }

    public int getCapacity() {
        return capacity;
    }
}