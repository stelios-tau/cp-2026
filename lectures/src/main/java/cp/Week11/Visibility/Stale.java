package cp.Week11.Visibility;

/*
 * More involved example on visibility
 * Remember: "The visibility effects of volatile variables extend beyond the 
 * value of the volatile variable itself"!
 */

class SharedData {
    int number = 0; // Not volatile!
    volatile boolean ready = false; // Not volatile! 
    
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
