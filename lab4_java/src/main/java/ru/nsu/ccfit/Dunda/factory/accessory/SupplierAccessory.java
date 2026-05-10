package ru.nsu.ccfit.Dunda.factory.accessory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SupplierAccessory implements Runnable {
    public final StorageAccessory storage;
    private final int speed;
    private final Logger log = LogManager.getLogger(SupplierAccessory.class);

    public SupplierAccessory(StorageAccessory storage, int speed) {
        this.storage = storage;
        this.speed = speed;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(speed);
                storage.supply(new Accessory());
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
