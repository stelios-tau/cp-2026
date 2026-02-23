package cp.Week9;

/*
 * Our familiar Counter.java example from Week 8
 * The final value of the private variable 'counter' is usually not equal to
 * the sum of its increments from both threads
 * 
 * This example also showcases that one can't just combine atomic actions.
 */

class SharedCounter {
    private int count = 0; // Shared among threads!

    public void increment() {
        int temp = count;  // Read (atomic)
        temp = temp + 1;   // Modify (atomic)
        count = temp;      // Write (atomic)
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
        if (counter.getCount() != 3200)
            System.out.println("You suck!");
    }
}
