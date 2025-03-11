package cp.Week11.Visibility;


/*
 * Visibility issue example
 * JVM performs optimizations in the Thread "thread"
 */

class KeepsGoing extends Thread {
    boolean keepRunning = true;

    public void run() {
        System.out.println("🟢 " + Thread.currentThread().getName() + " started...");

        while (keepRunning) {
            // Sleep might trigger memory synchronization!
            //try { Thread.sleep(10); } catch (InterruptedException ignored) {}
            ;
        }

        System.out.println("🔴 " + Thread.currentThread().getName() + " stopped.");
    }

    public static void main(String[] args) throws InterruptedException {
        KeepsGoing thread = new KeepsGoing();
        thread.start();

        Thread.sleep(1000); // Simulate delay

        System.out.println("⏳ " + System.currentTimeMillis() + " | Changing keepRunning to false...");
        thread.keepRunning = false; // The thread might not see this change!

        System.out.println("✅ " + System.currentTimeMillis() + " | keepRunning is now false.");
    }
}
