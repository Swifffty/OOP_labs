package ru.nsu.ccfit.Dunda.factory.auto;

public class Auto {
    private static int countAuto = 0;
    public final int id = ++countAuto;
    private final int idBody;
    private final int idEngine;
    private final int idAccessory;

    public Auto(int idBody, int idEngine, int idAccessory) {
        this.idBody = idBody;
        this.idEngine = idEngine;
        this.idAccessory = idAccessory;
    }
}
