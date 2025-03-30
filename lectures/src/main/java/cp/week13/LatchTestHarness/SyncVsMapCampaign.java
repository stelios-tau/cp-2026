package cp.week13.LatchTestHarness;
import java.util.concurrent.CountDownLatch;
import java.util.List;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;


public class SyncVsMapCampaign {
    private static long sharedCounter = 0;
    private static final Object lock = new Object();
    private static final List<Integer> threadCounts = Arrays.asList(1, 2, 4, 8, 16, 32, 64);
    private static final List<Long> totalIncrements = Arrays.asList(1024L);
    private static final int REPS = 1;

    public static void main(String[] args) throws InterruptedException {

        System.out.println("Benchmark: GlobalCounter");
        for (int threads : threadCounts) {
            for (long total : totalIncrements) {
                double avg = benchmarkGlobalCounter(threads, total);
                System.out.printf("Global     & %2d & %8d & %.2f ms \\%n", threads, total, avg);
            }
        }

        System.out.println("\nBenchmark: ConcurrentHashMap");
        for (int threads : threadCounts) {
            for (long total : totalIncrements) {
                double avg = benchmarkPerThreadMapConcurrent(threads, total);
                System.out.printf("ConcurrentMap & %2d & %8d & %.2f ms \\%n", threads, total, avg);
            }
        }
    }

    private static double benchmarkGlobalCounter(int numThreads, long totalIncrements) throws InterruptedException {
        long totalTime = 0;
        long perThread = totalIncrements / numThreads;

        for (int r = 0; r < REPS; r++) {
            sharedCounter = 0;
            Thread[] threads = new Thread[numThreads];
            long start = System.nanoTime();

            for (int i = 0; i < numThreads; i++) {
                threads[i] = new Thread(() -> {
                    for (long j = 0; j < perThread; j++) {
                        synchronized (lock) {
                            try {
                                Thread.sleep(8); // Simulate work time
                            } catch (InterruptedException ignored) {}
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

private static double benchmarkPerThreadMapConcurrent(int numThreads, long totalIncrements) throws InterruptedException {
    long totalTime = 0;
    long perThread = totalIncrements / numThreads;

    for (int r = 0; r < REPS; r++) {
        ConcurrentHashMap<Integer, Long> map = new ConcurrentHashMap<>();
        CountDownLatch latch = new CountDownLatch(numThreads);

        long start = System.nanoTime();

        for (int threadId = 0; threadId < numThreads; threadId++) {
            final int id = threadId;
            Thread t = new Thread(() -> {
                map.put(id, 0L);
                for (long j = 0; j < perThread; j++) {
                    try {
                        Thread.sleep(8); // Simulate work time
                    } catch (InterruptedException ignored) {}
                    map.compute(id, (key,val) -> ++val);
                }
                latch.countDown();
            });
            t.start();
        }

        latch.await();

        // Aggregate result (single-threaded)
        long total = 0;
        for (long val : map.values()) {
            total += val;
        }

        long end = System.nanoTime();
        totalTime += (end - start);
    }

    return totalTime / (REPS * 1_000_000.0); // ms
}

}

