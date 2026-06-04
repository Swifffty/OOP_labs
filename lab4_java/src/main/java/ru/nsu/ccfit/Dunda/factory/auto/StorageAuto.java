package ru.nsu.ccfit.Dunda.factory.auto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.Dunda.factory.Task;
import ru.nsu.ccfit.Dunda.factory.accessory.StorageAccessory;
import ru.nsu.ccfit.Dunda.factory.body.StorageBody;
import ru.nsu.ccfit.Dunda.factory.engine.StorageEngine;
import ru.nsu.ccfit.Dunda.threadpool.Pool;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class StorageAuto {
    public final int capacity;
    private final Queue<Auto> storage = new ArrayDeque<>();
    private final Logger log = LogManager.getLogger(StorageAuto.class);
    private final int flagForLog;
    private StorageBody storageBody;
    private StorageEngine storageEngine;
    private StorageAccessory storageAccessory;
    private Pool pool;
    private final AtomicInteger tasksInWait = new AtomicInteger(0);

    public StorageAuto(int capacity, int flag) {
        log.info("Создание склада машин");
        this.capacity = capacity;
        this.flagForLog = flag;
    }

    public void attachDependencies(StorageBody storageBody,
                                   StorageEngine storageEngine,
                                   StorageAccessory storageAccessory,
                                   int sizePool) {
        this.storageBody = storageBody;
        this.storageEngine = storageEngine;
        this.storageAccessory = storageAccessory;
        this.pool = new Pool(sizePool);
    }

    public void initStorage() {
        log.info("Склад авто: начальное заполнение фабрики задачами сборки");
        for (int i = 0; i < capacity; i++) {
            addNewTask();
        }
    }

    public synchronized void supply(Auto newAuto) throws InterruptedException {
        while (storage.size() == capacity) {
            log.info("Ожидание освобождения склада машин");
            wait();
        }
        storage.add(newAuto);
        log.info("На склад auto добавили машину с id: " + newAuto.id);
        notifyAll();
    }

    public synchronized Auto getAuto() throws InterruptedException {
        while (storage.isEmpty()) {
            log.info("Ожидание пополнения машин на складе");
            wait();
        }
        Auto auto = storage.remove();
        if (flagForLog == 1) {
            log.info("Дилер забрал машину ID: " + auto.id + " BodyId: " + auto.idBody + " EngineId: " + auto.idEngine + " AccessoryId: " + auto.idAccessory);
        }

        log.info("Склад авто: место освободилось, добавляю задачу на сборку");
        addNewTask();

        notifyAll();
        return auto;
    }

    private void addNewTask() {
        tasksInWait.incrementAndGet();
        pool.addTask(new Task(storageBody, storageEngine, storageAccessory, this));
    }

    public void taskCompleted() {
        tasksInWait.decrementAndGet();
    }

    public int getTasksInWait() {
        return tasksInWait.get();
    }

    public void setWorkerSpeed(int speed) {
        Task.setSpeed(speed);
    }

    public synchronized int currentSize() {
        return storage.size();
    }
}