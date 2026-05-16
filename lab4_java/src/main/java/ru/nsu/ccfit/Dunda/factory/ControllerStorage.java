package ru.nsu.ccfit.Dunda.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.Dunda.factory.accessory.StorageAccessory;
import ru.nsu.ccfit.Dunda.factory.auto.StorageAuto;
import ru.nsu.ccfit.Dunda.factory.body.StorageBody;
import ru.nsu.ccfit.Dunda.factory.engine.StorageEngine;
import ru.nsu.ccfit.Dunda.threadpool.Pool;

import java.util.concurrent.atomic.AtomicInteger;

public class ControllerStorage implements StorageSizeListener {
    private final StorageAuto storageAuto;
    private final StorageBody storageBody;
    private final StorageEngine storageEngine;
    private final StorageAccessory storageAccessory;
    private final Pool pool;
    private final Object controllerObject;

    private final AtomicInteger tasksInWait = new AtomicInteger(0);

    private final Logger log = LogManager.getLogger(ControllerStorage.class);

    public ControllerStorage(StorageAuto storageAuto,
                             StorageBody storageBody,
                             StorageEngine storageEngine,
                             StorageAccessory storageAccessory,
                             int sizePool,
                             Object controllerObject) {

        this.storageAuto = storageAuto;
        this.storageBody = storageBody;
        this.storageEngine = storageEngine;
        this.storageAccessory = storageAccessory;
        this.pool = new Pool(sizePool);
        this.controllerObject = controllerObject;
        this.storageAuto.setSizeListener(this);
    }

    public void initStorage() {
        log.info("Контроллер: начальное заполнение склада");
        for (int i = 0; i < storageAuto.capacity; i++) {
            addNewTask();
        }
    }

    public void setWorkerSpeed(int speed) {
        Task.setSpeed(speed);
    }

    @Override
    public synchronized void onStorageItemRemoved() {
        log.info("Контроллер: место на складе освободилось, добавляю задачу");
        addNewTask();
    }

    private void addNewTask() {
        tasksInWait.incrementAndGet();
        pool.addTask(new Task(storageBody, storageEngine, storageAccessory, storageAuto, this, controllerObject));
    }

    public void taskCompleted() {
        tasksInWait.decrementAndGet();
    }

    public int getTasksInWait() {
        return tasksInWait.get();
    }
}