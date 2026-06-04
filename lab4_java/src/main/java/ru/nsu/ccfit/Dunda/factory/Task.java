package ru.nsu.ccfit.Dunda.factory;

import ru.nsu.ccfit.Dunda.factory.accessory.Accessory;
import ru.nsu.ccfit.Dunda.factory.accessory.StorageAccessory;
import ru.nsu.ccfit.Dunda.factory.auto.Auto;
import ru.nsu.ccfit.Dunda.factory.auto.StorageAuto;
import ru.nsu.ccfit.Dunda.factory.body.Body;
import ru.nsu.ccfit.Dunda.factory.body.StorageBody;
import ru.nsu.ccfit.Dunda.factory.engine.Engine;
import ru.nsu.ccfit.Dunda.factory.engine.StorageEngine;

public class Task implements Runnable {
    private final StorageBody storageBody;
    private final StorageEngine storageEngine;
    private final StorageAccessory storageAccessory;
    private final StorageAuto storageAuto;
    private static volatile int speed = 1000;

    public static void setSpeed(int newSpeed) {
        speed = newSpeed;
    }

    public Task(StorageBody storageBody,
                StorageEngine storageEngine,
                StorageAccessory storageAccessory,
                StorageAuto storageAuto) {

        this.storageBody = storageBody;
        this.storageEngine = storageEngine;
        this.storageAccessory = storageAccessory;
        this.storageAuto = storageAuto;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(speed);
            Body body;
            Engine engine;
            Accessory accessory;

            synchronized (storageAuto) {
                while (storageBody.isEmpty() || storageEngine.isEmpty() || storageAccessory.isEmpty()) {
                    storageAuto.wait();
                }
                body = storageBody.getBody();
                engine = storageEngine.getEngine();
                accessory = storageAccessory.getAccessory();
            }

            Auto newAuto = new Auto(body.id, engine.id, accessory.id);
            storageAuto.supply(newAuto);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        storageAuto.taskCompleted();
    }
}