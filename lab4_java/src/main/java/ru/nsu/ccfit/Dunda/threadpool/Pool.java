package ru.nsu.ccfit.Dunda.threadpool;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Pool {
    private final ArrayList<ThreadWorker> Threads = new ArrayList<>();
    private final BlockingQueue<Runnable> tasks = new LinkedBlockingQueue<>();

    public Pool(int size) {
        for (int i = 0; i < size; i++) {
            ThreadWorker thread = new ThreadWorker();
            thread.start();
            Threads.add(thread);
        }
    }

    public boolean addTask(Runnable task) {
        return tasks.offer(task);
    }

    private class ThreadWorker extends Thread {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                Runnable task = tasks.take();
                task.run();
               } catch (InterruptedException e) {
                   break;
               }
            }
        }
    }
}
