package cp.week13.LatchTestHarness;
import java.util.concurrent.CountDownLatch;
import java.util.List;
import java.util.Arrays;

public class LocalLongCampaign {
    private static long sharedCounter = 0;
    private static final Object lock = new Object();
    private static final List<Integer> threadCounts = Arrays.asList(1, 2, 4, 8, 16, 32, 64);
    private static final List<Long> totalIncrements = Arrays.asList(137438953472L);
    private static final int REPS = 5;

    public static void main(String[] args) throws InterruptedException {

        System.out.println("\nBenchmark: LocalCounter+Latch");
        for (int threads : threadCounts) {
            for (long total : totalIncrements) {
                double avg = benchmarkLocalCounterWithLatch(threads, total);
                System.out.printf("Local+Latch & %2d & %8d & %.2f ms \\%n", threads, total, avg);
            }
        }
    }

    private static double benchmarkLocalCounterWithLatch(int numThreads, long totalIncrements) throws InterruptedException {
        long totalTime = 0;
        long perThread = totalIncrements / numThreads;

        for (int r = 0; r < REPS; r++) {
            sharedCounter = 0;
            CountDownLatch latch = new CountDownLatch(numThreads);
            long start = System.nanoTime();

            for (int i = 0; i < numThreads; i++) {
                Thread t = new Thread(() -> {
                    long local = 0;
                    for (long j = 0; j < perThread; j++) {
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

