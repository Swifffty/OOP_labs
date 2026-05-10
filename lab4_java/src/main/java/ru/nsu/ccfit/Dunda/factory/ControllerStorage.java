package ru.nsu.ccfit.Dunda.factory;

import ru.nsu.ccfit.Dunda.factory.accessory.StorageAccessory;
import ru.nsu.ccfit.Dunda.factory.auto.StorageAuto;
import ru.nsu.ccfit.Dunda.factory.body.StorageBody;
import ru.nsu.ccfit.Dunda.factory.engine.StorageEngine;
import ru.nsu.ccfit.Dunda.threadpool.Pool;

public class ControllerStorage implements Runnable{
    private final StorageAuto storageAuto;
    private final int capacity;
    private final Pool pool;
    private final StorageBody storageBody;
    private final StorageEngine storageEngine;
    private final StorageAccessory storageAccessory;
    public ControllerStorage(StorageAuto storageAuto, StorageBody storageBody, StorageEngine storageEngine, StorageAccessory storageAccessory, int sizePool) {
        this.storageAuto = storageAuto;
        capacity = storageAuto.capacity;
        pool = new Pool(sizePool);
        this.storageBody = storageBody;
        this.storageEngine = storageEngine;
        this.storageAccessory = storageAccessory;
    }
    @Override
    public void run() {
        while (true) {
            if ((double) storageAuto.currentSize() / capacity < 0.8) {
                pool.addTask(new Worker(storageBody, storageEngine, storageAccessory, storageAuto));
            }
        }
    }
}
