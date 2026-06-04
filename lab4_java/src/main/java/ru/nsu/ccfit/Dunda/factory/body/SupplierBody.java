package ru.nsu.ccfit.Dunda.factory.body;

import java.lang.Runnable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SupplierBody implements Runnable {
    public final StorageBody storage;
    private volatile int speed;
    private final Logger log = LogManager.getLogger(SupplierBody.class);

    public SupplierBody(StorageBody storage, int speed) {
        log.info("Инициализация task SupplierBody");
        this.storage = storage;
        this.speed = speed;
    }

    @Override
    public void run() {
        log.info("Начало поставки Body");
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(speed);
                storage.supply(new Body());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        log.info("Конец поставки SupplierBody");
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
