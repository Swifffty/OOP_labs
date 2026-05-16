package ru.nsu.ccfit.Dunda.factory.accessory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SupplierAccessory implements Runnable {
    public final StorageAccessory storage;
    private volatile int speed;
    private final Logger log = LogManager.getLogger(SupplierAccessory.class);

    public SupplierAccessory(StorageAccessory storage, int speed) {
        this.storage = storage;
        this.speed = speed;
    }

    @Override
    public void run() {
        log.info("начало поставки аксессуаров");
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(speed);
                storage.supply(new Accessory());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        log.info("Конец поставки accessory");
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
