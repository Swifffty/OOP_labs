package ru.nsu.ccfit.Dunda.factory.body;

import java.util.concurrent.atomic.AtomicInteger;

public class Body {
    private final static AtomicInteger countBody = new AtomicInteger(0);
    public final int id;

    public Body() {
        id = countBody.incrementAndGet();
    }

    public static int getTotalCount() {
        return countBody.get();
    }

}
