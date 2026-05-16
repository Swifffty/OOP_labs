package ru.nsu.ccfit.Dunda.factory.auto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.Dunda.factory.StorageSizeListener;

import java.util.ArrayDeque;
import java.util.Queue;

public class StorageAuto {
    public final int capacity;
    private Queue<Auto> storage = new ArrayDeque<>();
    private final Logger log = LogManager.getLogger(StorageAuto.class);
    private StorageSizeListener listener;
    private final int flagForLog;

    public void setSizeListener(StorageSizeListener listener) {
        this.listener = listener;
    }

    public StorageAuto(int capacity, int flag) {
        log.info("Создание скалада машин");
        this.capacity = capacity;
        flagForLog = flag;
    }
    public synchronized void supply(Auto newAuto) throws InterruptedException {
        while (storage.size() == capacity) {
            log.info("Ожиадние осовбождения скада машин");
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
        notifyAll();
        if (listener != null) {
            listener.onStorageItemRemoved();
        }
        return auto;
    }

    public synchronized int currentSize() {
        return storage.size();
    }
}
