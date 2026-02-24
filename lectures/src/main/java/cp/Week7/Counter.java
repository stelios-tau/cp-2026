package cp.Week7;

class SharedCounter {
    private int count = 0; // Shared among threads!

    public void increment() {
        int temp = count;  // Read
        temp = temp + 1;   // Modify
        count = temp;      // Write
    }

    public int getCount() {
        return count;
    }
}

public class Counter {
    public static void main(String[] args) throws InterruptedException {
        SharedCounter counter = new SharedCounter(); //Observe that this instance is used in both threads.

        // Create two threads that increment the counter
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1600; i++) {
                counter.increment();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1600; i++) {
                counter.increment();
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

         // Expected: 3200, but usually less
        System.out.println("Final counter value: " + counter.getCount());
    }
}
