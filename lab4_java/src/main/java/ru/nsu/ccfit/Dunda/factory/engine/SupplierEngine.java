package ru.nsu.ccfit.Dunda.factory.engine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SupplierEngine implements Runnable {
    private volatile int speed;
    private final StorageEngine storage;
    private final Logger log = LogManager.getLogger(SupplierEngine.class);
    public SupplierEngine(StorageEngine storage, int speed) {
        this.storage = storage;
        this.speed = speed;
    }
    @Override
    public void run() {
        log.info("Начало поставки engine");
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(speed);
                storage.supply(new Engine());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        log.info("Конец поставки engine");
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
