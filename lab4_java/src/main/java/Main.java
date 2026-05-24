import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.Dunda.factory.ControllerStorage;
import ru.nsu.ccfit.Dunda.factory.Dealer;
import ru.nsu.ccfit.Dunda.factory.accessory.StorageAccessory;
import ru.nsu.ccfit.Dunda.factory.accessory.SupplierAccessory;
import ru.nsu.ccfit.Dunda.factory.accessory.Accessory;
import ru.nsu.ccfit.Dunda.factory.auto.Auto;
import ru.nsu.ccfit.Dunda.factory.auto.StorageAuto;
import ru.nsu.ccfit.Dunda.factory.body.Body;
import ru.nsu.ccfit.Dunda.factory.body.StorageBody;
import ru.nsu.ccfit.Dunda.factory.body.SupplierBody;
import ru.nsu.ccfit.Dunda.factory.engine.Engine;
import ru.nsu.ccfit.Dunda.factory.engine.StorageEngine;
import ru.nsu.ccfit.Dunda.factory.engine.SupplierEngine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends Application {
    private final static Logger log = LogManager.getLogger(Main.class);
    private final static Map<String, Integer> configMap = new HashMap<>();

    private Label bodyStorageInfo = new Label();
    private Label engineStorageInfo = new Label();
    private Label accessoryStorageInfo = new Label();
    private Label autoStorageInfo = new Label();

    private Label totalStatsLabel = new Label();
    private Label queueLabel = new Label();

    private Slider bodySlider = createSlider();
    private Slider engineSlider = createSlider();
    private Slider accessorySlider = createSlider();
    private Slider dealerSlider = createSlider();
    private Slider workerSlider = createSlider();

    private Slider createSlider() {
        Slider s = new Slider(0, 10000, 1000);
        s.setShowTickLabels(true);
        s.setShowTickMarks(true);
        s.setMajorTickUnit(2000);
        return s;
    }

    private static void ConfigPars() {
        InputStream is = Main.class.getResourceAsStream("/Config");
        if (is == null) throw new RuntimeException("No config file!");
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = bf.readLine()) != null) {
                String[] pars = line.split("=");
                if (pars.length == 2) configMap.put(pars[0].trim(), Integer.valueOf(pars[1].trim()));
            }
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    @Override
    public void start(Stage primaryStage) {
        ConfigPars();
        log.info("Запуск интерфейса фабрики");

        Object controllerObject = new Object(); // избавиться от контроллер обжект и пересмотреть контроллер сторедж

        StorageBody storageBody = new StorageBody(configMap.get("StorageBodySize"), controllerObject);
        StorageEngine storageEngine = new StorageEngine(configMap.get("StorageEngineSize"), controllerObject);
        StorageAccessory storageAccessory = new StorageAccessory(configMap.get("StorageAccessorySize"), controllerObject);
        StorageAuto storageAuto = new StorageAuto(configMap.get("StorageAutoSize"), configMap.get("LogSale"));

        SupplierBody bSup = new SupplierBody(storageBody, 1000);
        new Thread(bSup).start();

        SupplierEngine eSup = new SupplierEngine(storageEngine, 1000);
        new Thread(eSup).start();

        List<SupplierAccessory> accSups = new ArrayList<>();
        for (int i = 0; i < configMap.get("SupplierAccessory"); i++) {
            SupplierAccessory s = new SupplierAccessory(storageAccessory, 1000);
            accSups.add(s);
            new Thread(s).start();
        }

        ControllerStorage controller = new ControllerStorage(storageAuto, storageBody, storageEngine, storageAccessory, configMap.get("Workers"));
        controller.initStorage();

        List<Dealer> dealers = new ArrayList<>();
        for (int i = 0; i < configMap.get("Dealers"); i++) {
            Dealer d = new Dealer(storageAuto, 1000);
            dealers.add(d);
            new Thread(d).start();
        }

        bodySlider.valueProperty().addListener((o, old, v) -> bSup.setSpeed(v.intValue()));
        engineSlider.valueProperty().addListener((o, old, v) -> eSup.setSpeed(v.intValue()));
        accessorySlider.valueProperty().addListener((o, old, v) -> accSups.forEach(s -> s.setSpeed(v.intValue())));
        dealerSlider.valueProperty().addListener((o, old, v) -> dealers.forEach(d -> d.setSpeed(v.intValue())));
        workerSlider.valueProperty().addListener((o, old, v) -> controller.setWorkerSpeed(v.intValue()));

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(
                new Label("--- ЗАПОЛНЕННОСТЬ СКЛАДОВ ---"),
                bodyStorageInfo, engineStorageInfo, accessoryStorageInfo, autoStorageInfo,
                new Label("\n--- ВСЕГО ПРОИЗВЕДЕНО (Статистика) ---"),
                totalStatsLabel, queueLabel,
                new Label("\n--- УПРАВЛЕНИЕ ЗАДЕРЖКОЙ (мс) ---"),
                new Label("Кузова:"), bodySlider,
                new Label("Двигатели:"), engineSlider,
                new Label("Аксессуары:"), accessorySlider,
                new Label("Дилеры:"), dealerSlider,
                new Label("Скорость сборки (Workers):"),
                workerSlider
        );

        startUiUpdater(storageAuto, storageBody, storageEngine, storageAccessory, controller);

        primaryStage.setTitle("Factory Emulator");
        primaryStage.setScene(new Scene(layout, 500, 800));
        primaryStage.setOnCloseRequest(e -> System.exit(0));
        primaryStage.show();
    }

    private void startUiUpdater(StorageAuto auto, StorageBody body, StorageEngine eng, StorageAccessory acc, ControllerStorage ctrl) {
        Thread updater = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(200);
                    Platform.runLater(() -> {
                        bodyStorageInfo.setText("Кузова: " + body.currentSize() + " / " + body.capacity);
                        engineStorageInfo.setText("Двигатели: " + eng.currentSize() + " / " + eng.capacity);
                        accessoryStorageInfo.setText("Аксессуары: " + acc.currentSize() + " / " + acc.capacity);
                        autoStorageInfo.setText("Машины: " + auto.currentSize() + " / " + auto.capacity);

                        totalStatsLabel.setText(String.format(
                                "Всего выпущено:\nКузовов: %d | Моторов: %d | Аксессуаров: %d\nМАШИН СОБРАНО: %d",
                                Body.getTotalCount(), Engine.getTotalCount(), Accessory.getTotalCount(), Auto.totalCount()
                        ));

                        queueLabel.setText("Задач в очереди сборки: " + ctrl.getTasksInWait());
                    });
                } catch (InterruptedException e) { break; }
            }
        });
        updater.setDaemon(true);
        updater.start();
    }

    public static void main(String[] args) { launch(args); }
}