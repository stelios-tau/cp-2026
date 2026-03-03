package cp.Week10.Visibility;

/*
 * Visibility issue example
 * Fixed with locking (not optimal, there is a simpler way)
 */

class KeepsGoingFixed extends Thread {
    boolean keepRunning = true; // Not volatile!
    final static Object asdf = new Object();

    public void run() {
        System.out.println("🟢 " + Thread.currentThread().getName() + " started...");
        boolean localFlag;
        
        do {
            synchronized(asdf){
                localFlag = keepRunning;
            }
        } while (localFlag);

        System.out.println("🔴 " + Thread.currentThread().getName() + " stopped.");
    }

    public static void main(String[] args) throws InterruptedException {
        
        KeepsGoingFixed thread = new KeepsGoingFixed();
        thread.start();

        Thread.sleep(1000); // Simulate delay

        System.out.println("⏳ " + System.currentTimeMillis() + " | Changing keepRunning to false...");
        synchronized (asdf) {thread.keepRunning = false;}
        //thread.keepRunning = false; // The thread might not see this change!

        System.out.println("✅ " + System.currentTimeMillis() + " | keepRunning is now false.");
    }
}