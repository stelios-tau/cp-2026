package cp.Week13.LockTestHarness;

public class SyncVSGaren {
    private static final int NUM_THREADS = 1;
    private static final int INCREMENTS_PER_THREAD = 90_000_000;
    private static int counter = 0;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("🔧 Testing SpinLock...");
        runTest(new SpinLock());

        System.out.println("🔧 Testing SynchronizedLock...");
        runTestWithSynchronized();
    }

    private static void runTest(LockStrategy lock) throws InterruptedException {
        counter = 0;
        long start = System.nanoTime();

        Thread[] threads = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    lock.lock();
                    try {
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
        System.out.printf("✅ Final counter: %d%n", counter);
        System.out.printf("⏱️ Time: %.2f ms%n%n", (end - start) / 1_000_000.0);
    }

    // Special case for synchronized, since we can't split lock/unlock
    private static void runTestWithSynchronized() throws InterruptedException {
        counter = 0;
        final Object lock = new Object();
        long start = System.nanoTime();

        Thread[] threads = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    synchronized (lock) {
                        counter++;
                    }
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) t.join();

        long end = System.nanoTime();
        System.out.printf("✅ Final counter: %d%n", counter);
        System.out.printf("⏱️ Time: %.2f ms%n%n", (end - start) / 1_000_000.0);
    }
}

