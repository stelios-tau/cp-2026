package cp.week13.LatchTestHarness;
import java.util.concurrent.CountDownLatch;
import java.util.List;
import java.util.Arrays;

public class GlobalVSLocalCampaign {
    private static int sharedCounter = 0;
    private static final Object lock = new Object();
    private static final List<Integer> threadCounts = Arrays.asList(1, 2, 4, 8, 16, 32);
    private static final List<Integer> totalIncrements = Arrays.asList(134_217_728, 1_073_741_824);
    private static final int REPS = 5;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Benchmark: GlobalCounter");
        for (int threads : threadCounts) {
            for (int total : totalIncrements) {
                double avg = benchmarkGlobalCounter(threads, total);
                System.out.printf("Global     & %2d & %8d & %.2f ms \\%n", threads, total, avg);
            }
        }

        System.out.println("\nBenchmark: LocalCounter+Latch");
        for (int threads : threadCounts) {
            for (int total : totalIncrements) {
                double avg = benchmarkLocalCounterWithLatch(threads, total);
                System.out.printf("Local+Latch & %2d & %8d & %.2f ms \\%n", threads, total, avg);
            }
        }
    }

    private static double benchmarkGlobalCounter(int numThreads, int totalIncrements) throws InterruptedException {
        long totalTime = 0;
        int perThread = totalIncrements / numThreads;

        for (int r = 0; r < REPS; r++) {
            sharedCounter = 0;
            Thread[] threads = new Thread[numThreads];
            long start = System.nanoTime();

            for (int i = 0; i < numThreads; i++) {
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < perThread; j++) {
                        synchronized (lock) {
                            sharedCounter++;
                        }
                    }
                });
                threads[i].start();
            }

            for (Thread t : threads) t.join();

            long end = System.nanoTime();
            totalTime += (end - start);
        }

        return totalTime / (REPS * 1_000_000.0); // ms
    }

    private static double benchmarkLocalCounterWithLatch(int numThreads, int totalIncrements) throws InterruptedException {
        long totalTime = 0;
        int perThread = totalIncrements / numThreads;

        for (int r = 0; r < REPS; r++) {
            sharedCounter = 0;
            CountDownLatch latch = new CountDownLatch(numThreads);
            long start = System.nanoTime();

            for (int i = 0; i < numThreads; i++) {
                Thread t = new Thread(() -> {
                    int local = 0;
                    for (int j = 0; j < perThread; j++) {
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
            totalTime += (end - start);
        }

        return totalTime / (REPS * 1_000_000.0); // ms
    }
}
