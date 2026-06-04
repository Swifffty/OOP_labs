package ru.nsu.ccfit.Dunda.factory.engine;

import java.util.concurrent.atomic.AtomicInteger;

public class Engine {
    private static AtomicInteger countEngine = new AtomicInteger(0);
    public final int id;

    public Engine() {
        id = countEngine.incrementAndGet();
    }

    public static int getTotalCount() {
        return countEngine.get();
    }
}
