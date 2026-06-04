import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import ru.nsu.ccfit.Dunda.factory.ControllerStorage;
import ru.nsu.ccfit.Dunda.factory.Dealer;
import ru.nsu.ccfit.Dunda.factory.Task;
import ru.nsu.ccfit.Dunda.factory.accessory.Accessory;
import ru.nsu.ccfit.Dunda.factory.accessory.StorageAccessory;
import ru.nsu.ccfit.Dunda.factory.auto.Auto;
import ru.nsu.ccfit.Dunda.factory.auto.StorageAuto;
import ru.nsu.ccfit.Dunda.factory.body.Body;
import ru.nsu.ccfit.Dunda.factory.body.StorageBody;
import ru.nsu.ccfit.Dunda.factory.engine.Engine;
import ru.nsu.ccfit.Dunda.factory.engine.StorageEngine;
import ru.nsu.ccfit.Dunda.threadpool.Pool;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class FactoryProjectTest {

    @Test
    void testAutoFieldsAndIncrementingId() {
        int initialCount = Auto.totalCount();

        Auto auto1 = new Auto(101, 202, 303);
        Auto auto2 = new Auto(104, 205, 306);

        assertEquals(101, auto1.idBody);
        assertEquals(202, auto1.idEngine);
        assertEquals(303, auto1.idAccessory);

        assertEquals(initialCount + 1, auto1.id);
        assertEquals(initialCount + 2, auto2.id);
        assertEquals(initialCount + 2, Auto.totalCount());
    }

    @Test
    void testStorageAutoSupplyAndGet() throws InterruptedException {
        StorageAuto storage = new StorageAuto(2, 0);
        Auto auto = new Auto(1, 1, 1);

        assertEquals(0, storage.currentSize(), "Начальный размер склада должен быть 0");

        storage.supply(auto);
        assertEquals(1, storage.currentSize(), "После supply размер должен увеличиться");

        Auto removed = storage.getAuto();
        assertEquals(auto.id, removed.id, "Извлеченный автомобиль должен совпадать по ID");
        assertEquals(0, storage.currentSize(), "После извлечения склад должен опустеть");
    }

    @Test
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void testStorageAutoListenerTriggered() throws InterruptedException {
        StorageAuto storage = new StorageAuto(2, 0);
        AtomicBoolean listenerCalled = new AtomicBoolean(false);
        storage.setSizeListener(() -> listenerCalled.set(true));

        storage.supply(new Auto(1, 2, 3));
        assertFalse(listenerCalled.get(), "Слушатель не должен вызываться при добавлении товара");

        storage.getAuto();
        assertTrue(listenerCalled.get(), "Слушатель обязан вызваться при удалении товара со склада");
    }

    @Test
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void testStorageAutoSupplyBlocksWhenFull() throws InterruptedException {
        StorageAuto storage = new StorageAuto(1, 0);
        storage.supply(new Auto(1, 1, 1));
        Thread producerThread = new Thread(() -> {
            try {
                storage.supply(new Auto(2, 2, 2));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producerThread.start();
        Thread.sleep(100);

        assertTrue(producerThread.isAlive(), "Поток должен заблокироваться в состоянии ожидания места");
        assertEquals(1, storage.currentSize(), "Размер склада не должен измениться");

        producerThread.interrupt();
    }

    @Test
    @Timeout(value = 2, unit = TimeUnit.SECONDS)
    void testPoolExecutesTasks() throws InterruptedException {
        int poolSize = 2;
        Pool pool = new Pool(poolSize);

        int taskCount = 3;
        CountDownLatch latch = new CountDownLatch(taskCount);
        AtomicInteger executedTasksCounter = new AtomicInteger(0);

        Runnable testTask = () -> {
            executedTasksCounter.incrementAndGet();
            latch.countDown();
        };

        for (int i = 0; i < taskCount; i++) {
            assertTrue(pool.addTask(testTask), "Задача должна успешно добавиться в очередь пула");
        }

        boolean allCompleted = latch.await(1500, TimeUnit.MILLISECONDS);

        assertTrue(allCompleted, "Пул потоков не успел выполнить задачи за отведенное время");
        assertEquals(taskCount, executedTasksCounter.get(), "Количество выполненных задач должно быть строго равно отправленным");
    }

    @Test
    @Timeout(value = 3, unit = TimeUnit.SECONDS)
    void testControllerAndTaskLifecycle() throws InterruptedException {
        Object testFactoryLock = new Object();

        StorageAuto storageAuto = new StorageAuto(2, 0);

        StorageBody storageBody = new StorageBody(2, testFactoryLock);
        StorageEngine storageEngine = new StorageEngine(2, testFactoryLock);
        StorageAccessory storageAccessory = new StorageAccessory(2, testFactoryLock);

        ControllerStorage controller = new ControllerStorage(
                storageAuto,
                storageBody,
                storageEngine,
                storageAccessory,
                0,
                testFactoryLock
        );

        storageBody.supply(new Body());
        storageEngine.supply(new Engine());
        storageAccessory.supply(new Accessory());

        Task.setSpeed(0);

        Task task = new Task(storageBody, storageEngine, storageAccessory, storageAuto, controller, testFactoryLock);

        int initialTasksWait = controller.getTasksInWait();

        task.run();

        assertEquals(1, storageAuto.currentSize(), "Машина должна успешно собраться и попасть на склад");
        assertTrue(storageBody.isEmpty(), "Склад кузовов должен опустеть, так как деталь ушла на сборку");
        assertEquals(initialTasksWait - 1, controller.getTasksInWait(), "Счетчик ожидания задач должен уменьшиться на 1");
    }

    @Test
    @Timeout(value = 2, unit = TimeUnit.SECONDS)
    void testDealerConsumesAuto() throws InterruptedException {
        StorageAuto storageAuto = new StorageAuto(2, 0);
        storageAuto.supply(new Auto(7, 8, 9));

        Dealer dealer = new Dealer(storageAuto, 0);
        Thread dealerThread = new Thread(dealer);
        dealerThread.start();

        Thread.sleep(100);

        assertEquals(0, storageAuto.currentSize(), "Дилер должен был обнаружить машину и забрать её со склада");

        dealerThread.interrupt();
        dealerThread.join(500);
        assertFalse(dealerThread.isAlive(), "Поток дилера должен штатно завершить работу после вызова interrupt()");
    }
}