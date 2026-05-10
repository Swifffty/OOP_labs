package ru.nsu.ccfit.Dunda.factory.engine;

public class SupplierEngine implements Runnable {
    private final int speed;
    private final StorageEngine storage;
    public SupplierEngine(StorageEngine storage, int speed) {
        this.storage = storage;
        this.speed = speed;
    }
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(speed);
                storage.supply(new Engine());
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
