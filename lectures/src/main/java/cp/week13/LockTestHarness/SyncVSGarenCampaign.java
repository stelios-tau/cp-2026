package cp.week13.LockTestHarness;
import java.util.List;
import java.util.Arrays;

public class SyncVSGarenCampaign {
    private static volatile int counter = 0;

    // Benchmark settings
    // private static final List<Integer> numThreadsList = Arrays.asList(1, 2, 4, 8);
    // private static final List<Integer> incrementsPerThreadList = Arrays.asList(4_000_000);
    private static final List<Integer> numThreadsList = Arrays.asList(2);
    private static final List<Integer> incrementsPerThreadList = Arrays.asList(256);
    private static final int repetitions = 1;
    private static final int innerdelay = 0;
    private static final int outdelay = 128;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Benchmark: SpinLock");
        runBenchmark("SpinLock", new SpinLock());

        System.out.println("\nBenchmark: synchronized");
        runBenchmarkSynchronized();

        //System.out.println("\nBenchmark: Nothing");
        //runBenchmark("SpinLock", new Nothing());
    }

    private static void runBenchmark(String label, LockStrategy lock) throws InterruptedException {
        for (int numThreads : numThreadsList) {
            for (int incrementsPerThread : incrementsPerThreadList) {
                long totalTime = 0;

                for (int r = 0; r < repetitions; r++) {
                    counter = 0;
                    long start = System.nanoTime();

                    Thread[] threads = new Thread[numThreads];
                    for (int i = 0; i < numThreads; i++) {
                        threads[i] = new Thread(() -> {
                            for (int j = 0; j < incrementsPerThread; j++) {
                                try {
                                    Thread.sleep((long) (Math.random() * outdelay)); // Random
                                } catch (InterruptedException ignored) {};
                                lock.lock();
                                try {
                                    if (innerdelay != 0){
                                        try {
                                            Thread.sleep(innerdelay); // Simulate work time
                                        } catch (InterruptedException ignored) {};
                                    }
                                    counter++;
                                } finally {
                                    lock.unlock();
                                }
                            }
                        });
                        threads[i].start();
                    }

                    for (Thread t : threads) t.join();

                    long end = System.nanoTime();
                    totalTime += (end - start);
                }

                double avgTimeMs = totalTime / (repetitions * 1_000_000.0);

                // Beamer/PGFPlots-style table row
                System.out.printf("%-10s & %2d threads & %8d inc/thread & %.2f ms \\\\ %n",
                        label, numThreads, incrementsPerThread, avgTimeMs);
            }
        }
    }

    private static void runBenchmarkSynchronized() throws InterruptedException {
        for (int numThreads : numThreadsList) {
            for (int incrementsPerThread : incrementsPerThreadList) {
                long totalTime = 0;
                final Object lock = new Object();

                for (int r = 0; r < repetitions; r++) {
                    counter = 0;
                    long start = System.nanoTime();

                    Thread[] threads = new Thread[numThreads];
                    for (int i = 0; i < numThreads; i++) {
                        threads[i] = new Thread(() -> {
                            for (int j = 0; j < incrementsPerThread; j++) {
                                try {
                                    Thread.sleep((long) (Math.random() * outdelay));; // Random Delay
                                } catch (InterruptedException ignored) {};
                                synchronized (lock) {
                                    if (innerdelay != 0){
                                        try {
                                            Thread.sleep(innerdelay); // Simulate work time
                                        } catch (InterruptedException ignored) {};
                                    }
                                    counter++;
                                }
                            }
                        });
                        threads[i].start();
                    }

                    for (Thread t : threads) t.join();

                    long end = System.nanoTime();
                    totalTime += (end - start);
                }

                double avgTimeMs = totalTime / (repetitions * 1_000_000.0);

                // Beamer/PGFPlots-style table row
                System.out.printf("%-10s & %2d threads & %8d inc/thread & %.2f ms \\\\ %n",
                        "synchronized", numThreads, incrementsPerThread, avgTimeMs);
            }
        }
    }

    
}


