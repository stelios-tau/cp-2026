package cp.Week19;

import java.util.concurrent.*;

public class VirtualThreadSleepDemo {
    static final int TASK_COUNT = 1_000;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Platform threads:");
        runWithExecutor(Executors.newFixedThreadPool(100));

        System.out.println("\nVirtual threads:");
        runWithExecutor(Executors.newVirtualThreadPerTaskExecutor());
    }

    static void runWithExecutor(ExecutorService executor) throws InterruptedException {
        long start = System.currentTimeMillis();

        CountDownLatch latch = new CountDownLatch(TASK_COUNT);

        for (int i = 0; i < TASK_COUNT; i++) {
            executor.submit(() -> {
                try {
                    Thread.sleep(2000); // Simulate blocking work
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        long end = System.currentTimeMillis();
        System.out.printf("✅ Completed in %.2f seconds%n", (end - start) / 1000.0);
    }
}
