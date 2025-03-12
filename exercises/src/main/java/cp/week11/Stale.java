package cp.Week11.Visibility;

/*
 * This is the stale data example from the lecture. You have the
 * following objectives. Recall that currently, the ReaderThread cannot see
 * the writes in the publishUpdate() method from the main thread.
 * 1. Fix the synchronization issue in the code without using "volatile" variables.
 * 2. How many different fixes via the "volatile" can you find?
 * 3. If possible, make sure that *only* the ready=true update is visible to the
 * ReaderThread. Again, if possible.
 */

class SharedData {
    int number = 0; // Not volatile!
    boolean ready = false; // Not volatile!

    public void publishUpdate() {
        number = 42;  // Step 1: Update number
        ready = true;  // Step 2: Indicate that the number is ready (but another thread might not see this!)
    }
}

class ReaderThread extends Thread {
    private final SharedData data;

    public ReaderThread(SharedData data) {
        this.data = data;
    }

    public void run() {
        while (!data.ready) {
            // Loop until `ready` is set to true (might never exit!)
        }

        System.out.println("📢 Read number: " + data.number); // Could print 0 instead of 42!
    }
}

public class Stale {
    public static void main(String[] args) throws InterruptedException {
        SharedData data = new SharedData();
        ReaderThread reader = new ReaderThread(data);

        reader.start();

        Thread.sleep(1000); // Simulate delay

        System.out.println("📝 Publishing update...");
        data.publishUpdate(); // Set number = 42 and ready = true
    }
}
