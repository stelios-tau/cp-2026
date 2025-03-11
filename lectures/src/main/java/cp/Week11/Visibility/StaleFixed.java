package cp.Week11.Visibility;

/*
 * More involved example on visibility
 * Remember: "The visibility effects of volatile variables extend beyond the 
 * value of the volatile variable itself"!
 */

class SharedDataFixed {
    int number = 0; // Not volatile!
    boolean ready = false; // Not volatile! But what if I change just that?
    
    public synchronized boolean getR() {
        return ready;
    }

    public synchronized int getN() {
        return number;
    }

    public synchronized void publishUpdate() {
        number = 42;  // Step 1: Update number
        ready = true;  // Step 2: Indicate that the number is ready (but another thread might not see this!)
    }
}

class ReaderThreadFixed extends Thread {
    private final SharedDataFixed data;

    public ReaderThreadFixed(SharedDataFixed data) {
        this.data = data;
    }

    public void run() {
        while (!data.getR()) {
            // Loop until `ready` is set to true (might never exit!)
        }

        System.out.println("📢 Read number: " + data.number); // Could print 0 instead of 42!
    }
}

public class StaleFixed {
    public static void main(String[] args) throws InterruptedException {
        SharedDataFixed data = new SharedDataFixed();
        ReaderThreadFixed reader = new ReaderThreadFixed(data);
        
        reader.start();

        Thread.sleep(1000); // Simulate delay

        System.out.println("📝 Publishing update...");
        data.publishUpdate(); // Set number = 42 and ready = true
    }
}
