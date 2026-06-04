package ru.nsu.ccfit.Dunda.factory.auto;

import java.util.concurrent.atomic.AtomicInteger;

public class Auto {
    private final static AtomicInteger countAuto = new AtomicInteger(0);
    public final int id;
    public final int idBody;
    public final int idEngine;
    public final int idAccessory;

    public Auto(int idBody, int idEngine, int idAccessory) {
        this.idBody = idBody;
        this.idEngine = idEngine;
        this.idAccessory = idAccessory;
        id = countAuto.incrementAndGet();
    }
    public static int totalCount() {
        return countAuto.get();
    }
}
