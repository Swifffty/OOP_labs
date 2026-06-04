package ru.nsu.ccfit.Dunda.factory.accessory;

import java.util.concurrent.atomic.AtomicInteger;

public class Accessory {
    private static AtomicInteger countAccessory = new AtomicInteger(0);
    public final int id;

    public Accessory() {
        id = countAccessory.incrementAndGet();
    }

    public static int getTotalCount() {
        return countAccessory.get();
    }
}
