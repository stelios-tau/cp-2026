package cp.Week9;

/*
 * Qestion: What if the increment method uses '++' instead of a temporary variable?
 */

class SharedCounter2 {
    private int count = 0; // Shared among threads!

    public void increment() {
        count++; //How about this?
    }

    public int getCount() {
        return count;
    }
}

public class CounterPlus {
    public static void main(String[] args) throws InterruptedException {
        SharedCounter2 counter = new SharedCounter2(); //Observe that this instance is used in both threads.

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
            
        System.out.println("Final counter value: " + counter.getCount());
    }
}

/*
 * Answer: The final value might stil be less than 3200, because ++ is not 
 * atomic.
 */
