package ru.nsu.ccfit.Dunda.factory.body;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.Dunda.factory.ControllerStorage;

import java.util.ArrayDeque;
import java.util.Queue;

public class StorageBody {
    public final int capacity;
    private final Queue<Body> storage = new ArrayDeque<>();
    private final Logger log = LogManager.getLogger(StorageBody.class);
    private final Object controllerObject;

    public StorageBody(int capacity, Object controllerObject) {
        this.capacity = capacity;
        this.controllerObject = controllerObject;
    }

    public void supply(Body newBody) throws InterruptedException {
        synchronized (this) {
            while (storage.size() == capacity) {
                log.info("Склад body переполнен");
                wait();
            }
            storage.add(newBody);
            log.info("Добавление нового тела в хранилище c id: " + newBody.id);
            notifyAll();
        }
        synchronized (controllerObject) {
            controllerObject.notifyAll();
        }
    }

    public synchronized Body getBody() throws InterruptedException {
        while (storage.isEmpty()) {
            log.info("Склад body пустой");
            wait();
        }
        Body body = storage.remove();
        log.info("Забрали со склада body с id: " + body.id);
        notifyAll();
        return body;
    }

    public synchronized boolean isEmpty() {
        return storage.isEmpty();
    }

    public synchronized int currentSize() {
        return storage.size();
    }

}