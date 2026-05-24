package ru.nsu.ccfit.Dunda.factory.engine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.Dunda.factory.ControllerStorage;
import ru.nsu.ccfit.Dunda.factory.auto.StorageAuto;
import ru.nsu.ccfit.Dunda.factory.body.StorageBody;
import ru.nsu.ccfit.Dunda.factory.body.SupplierBody;

import java.util.ArrayDeque;
import java.util.Queue;

public class StorageEngine {
    public final int capacity;
    private final Logger log = LogManager.getLogger(StorageEngine.class);
    private final Queue<Engine> storage = new ArrayDeque<>();
    private final StorageAuto controllerObject;

    public StorageEngine(int capacity, StorageAuto controllerObject) {
        this.capacity = capacity;
        this.controllerObject = controllerObject;
    }


    public void supply(Engine engine) throws InterruptedException {
        synchronized (this) {
            while (storage.size() == capacity) {
                log.info("Склад engine переполнен");
                wait();
            }
            storage.add(engine);
            log.info("Поступил новый engine на склад с id: " + engine.id);
            notifyAll();
        }
        synchronized (controllerObject) {
            controllerObject.notifyAll();
        }
    }
    public synchronized Engine getEngine() throws InterruptedException {
            while (storage.isEmpty()) {
                log.info("Склад engine пустой");
                wait();
            }
            Engine engine = storage.remove();
            log.info("забрали engine с id: " + engine.id);
            notifyAll();
            return engine;
    }

    public synchronized boolean isEmpty() {
        return storage.isEmpty();
    }

    public synchronized int currentSize() {
        return storage.size();
    }
}
