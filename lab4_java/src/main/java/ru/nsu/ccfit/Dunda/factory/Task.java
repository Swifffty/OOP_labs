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
    private final ControllerStorage controllerStorage;
    private final Object controllerObject;
    private static volatile int speed = 1000;

    public static void setSpeed(int newSpeed) {
        speed = newSpeed;
    }

    public Task(StorageBody storageBody,
                StorageEngine storageEngine,
                StorageAccessory storageAccessory,
                StorageAuto storageAuto,
                ControllerStorage controllerStorage,
                Object controllerObject) {

        this.storageBody = storageBody;
        this.storageEngine = storageEngine;
        this.storageAccessory = storageAccessory;
        this.storageAuto = storageAuto;
        this.controllerStorage = controllerStorage;
        this.controllerObject = controllerObject;
    }
    @Override
    public void run() { // сначала проверить место на складе
        try {
            Thread.sleep(speed);
            Body body;
            Engine engine;
            Accessory accessory;
            synchronized (controllerObject) {
                while (storageBody.isEmpty() || storageEngine.isEmpty() || storageAccessory.isEmpty()) {
                    controllerObject.wait();
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
        controllerStorage.taskCompleted();
    }
}
