package cp.week13.LatchTestHarness;
import java.util.concurrent.CountDownLatch;

public class LatchTestHarness {
    private static final int NUM_THREADS = 12;
    private static final long TOTAL_INCREMENTS = 5_000_000_000L;
    // 10_000_000_000_000L for the optimized case in about 15 seconds on my PC
    // 2_000_000_000L for the unoptimized case in 8 seconds on my PC
    private static long sharedCounter = 0L;
    private static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Scenario 1: Global counter (synchronized only)");
        runGlobalCounter();

        System.out.println("\nScenario 2: Local counter + Latch");
        runLocalCounterWithLatch();
    }

    private static void runGlobalCounter() throws InterruptedException {
        sharedCounter = 0L;
        Thread[] threads = new Thread[NUM_THREADS];
        long incrementsPerThread = TOTAL_INCREMENTS / (long) NUM_THREADS;

        long start = System.nanoTime();

        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new Thread(() -> {
                for (long j = 0L; j < incrementsPerThread; j++) {
                    synchronized (lock) {
                        sharedCounter++;
                    }
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) t.join();

        long end = System.nanoTime();
        System.out.printf("Final counter: %d | Time: %.2f ms\n",
                sharedCounter, (end - start) / 1_000_000.0);
    }

    private static void runLocalCounterWithLatch() throws InterruptedException {
        sharedCounter = 0L;
        CountDownLatch latch = new CountDownLatch(NUM_THREADS);
        long incrementsPerThread = TOTAL_INCREMENTS / (long)NUM_THREADS;

        long start = System.nanoTime();
        for (int i = 0; i < NUM_THREADS; i++) {
            Thread t = new Thread(() -> {
                long local = 0L;
                for (long j = 0L; j < incrementsPerThread; j++) {
                    local++;
                }
                synchronized (lock) {
                    sharedCounter += local;
                }
                latch.countDown();
            });
            t.start();
        }

        latch.await();
        long end = System.nanoTime();

        System.out.printf("Final counter: %d | Time: %.2f ms\n",
                sharedCounter, (end - start) / 1_000_000.0);
    }
}
