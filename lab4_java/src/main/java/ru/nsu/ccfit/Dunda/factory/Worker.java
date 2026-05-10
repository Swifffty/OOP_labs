package ru.nsu.ccfit.Dunda.factory;

import ru.nsu.ccfit.Dunda.factory.accessory.Accessory;
import ru.nsu.ccfit.Dunda.factory.accessory.StorageAccessory;
import ru.nsu.ccfit.Dunda.factory.auto.Auto;
import ru.nsu.ccfit.Dunda.factory.auto.StorageAuto;
import ru.nsu.ccfit.Dunda.factory.body.Body;
import ru.nsu.ccfit.Dunda.factory.body.StorageBody;
import ru.nsu.ccfit.Dunda.factory.engine.Engine;
import ru.nsu.ccfit.Dunda.factory.engine.StorageEngine;

public class Worker implements Runnable {
    private final StorageBody storageBody;
    private final StorageEngine storageEngine;
    private final StorageAccessory storageAccessory;
    private final StorageAuto storageAuto;

    public Worker(StorageBody storageBody, StorageEngine storageEngine, StorageAccessory storageAccessory, StorageAuto storageAuto) {
        this.storageBody = storageBody;
        this.storageEngine = storageEngine;
        this.storageAccessory = storageAccessory;
        this.storageAuto = storageAuto;
    }
    @Override
    public void run() {
        try {
            Body body = storageBody.getBody();
            Engine engine = storageEngine.getEngine();
            Accessory accessory = storageAccessory.getAccessory();
            Auto newAuto = new Auto(body.id, engine.id, accessory.id);
            storageAuto.supply(newAuto);
        } catch (InterruptedException e) {
            Thread.currentThread().isInterrupted();
        }
    }
}
