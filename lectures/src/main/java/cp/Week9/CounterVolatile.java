package cp.Week9;

/*
 * Qestion: What if the variable 'counter' is volatile?
 */

class SharedCounter3 {
    private volatile int count = 0; // Nothing to do with visibility

    public void increment() {
        count++;
    }

    public int getCount() {
        return count;
    }
}

public class CounterVolatile {
    public static void main(String[] args) throws InterruptedException {
        SharedCounter3 counter = new SharedCounter3(); //Observe that this instance is used in both threads.

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

/*
 * Answer: Still, the value of 'counter' might be less than 3200, because the
 * issue is not one of visibility.
 */
