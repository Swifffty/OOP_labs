import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.Dunda.factory.accessory.StorageAccessory;
import ru.nsu.ccfit.Dunda.factory.accessory.SupplierAccessory;
import ru.nsu.ccfit.Dunda.factory.body.StorageBody;
import ru.nsu.ccfit.Dunda.factory.body.SupplierBody;
import ru.nsu.ccfit.Dunda.factory.engine.StorageEngine;
import ru.nsu.ccfit.Dunda.factory.engine.SupplierEngine;

import java.util.ArrayList;

public class Main {
    private final static Logger log = LogManager.getLogger(Main.class);
    public static void main(String[] args) {
        log.info("Начало программы");
        StorageBody storageBody = new StorageBody(100);
        Thread BodyThread = new Thread(new SupplierBody(storageBody, 100));
        BodyThread.start();
        StorageEngine storageEngine = new StorageEngine(100);
        Thread EngineThread = new Thread(new SupplierEngine(storageEngine, 100));
        EngineThread.start();
        StorageAccessory storageAccessory = new StorageAccessory(100);
        ArrayList<Thread> listAccessory = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            listAccessory.add(new Thread(new SupplierAccessory(storageAccessory, 100)));
            listAccessory.get(i).start();
        }



    }
}
