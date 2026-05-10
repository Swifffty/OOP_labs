package ru.nsu.ccfit.Dunda.factory.auto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.Dunda.factory.body.SupplierBody;

import java.util.ArrayDeque;
import java.util.Queue;

public class StorageAuto {
    public final int capacity;
    private Queue<Auto> storage = new ArrayDeque<>();
    private final Logger log = LogManager.getLogger(StorageAuto.class);

    public StorageAuto(int capacity) {
        log.info("Создание скалада машин");
        this.capacity = capacity;
    }
    public synchronized void supply(Auto newAuto) throws InterruptedException {
        log.info("Создание машины ID: " + newAuto.id);
        if (storage.size() == capacity) {
            log.info("Ожиадние осовбождения скада машин");
            wait();
        }
        storage.add(newAuto);
        log.info("Снятие блокировки ожидания добавляения новых машин");
        notifyAll();
    }

    public synchronized Auto getAuto() throws InterruptedException {
        if (storage.isEmpty()) {
            wait();
        }
        Auto auto = storage.remove();
        notifyAll();
        return auto;
    }

    public int currentSize() {
        return storage.size();
    }
}
