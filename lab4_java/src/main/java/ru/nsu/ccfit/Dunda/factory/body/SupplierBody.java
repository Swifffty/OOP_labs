package ru.nsu.ccfit.Dunda.factory.body;

import java.lang.Runnable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SupplierBody implements Runnable {
    public final StorageBody storage;
    private final int speed;
    private final Logger log = LogManager.getLogger(SupplierBody.class);

    public SupplierBody(StorageBody storage, int speed) {
        log.info("Инициализация task SupplierBody");
        this.storage = storage;
        this.speed = speed;
    }

    @Override
    public void run() {
        log.info("Run SupplierBody");
        while (true) {
            try {
                Thread.sleep(speed);
                log.info("Отправка newBody on storage");
                storage.supply(new Body());
            } catch (InterruptedException e) {
                log.info("End SupplierBody");
                return;
            }
        }
    }
}
