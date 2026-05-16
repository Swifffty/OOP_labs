package ru.nsu.ccfit.Dunda.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.Dunda.factory.auto.Auto;
import ru.nsu.ccfit.Dunda.factory.auto.StorageAuto;

public class Dealer implements Runnable {
    private volatile int speed;
    private final StorageAuto storageAuto;
    private final Logger log = LogManager.getLogger(Dealer.class);

    public Dealer(StorageAuto storageAuto, int speed) {
        this.speed = speed;
        this.storageAuto = storageAuto;
    }

    @Override public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(speed);
                Auto auto = storageAuto.getAuto();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        log.info("Dealer закончил работу");
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

}
