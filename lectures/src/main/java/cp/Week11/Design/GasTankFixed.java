package cp.Week11.Design;

/*
 * The GasTank example
 * The class invariant is: currentAmount <= maxCapacity
 * Note that the two state variables are interdependent!
 */

 class FixedGasTank {
    /*
     * The class invariant is: currentAmount <= maxCapacity
     */
    private int maxCapacity;
    private int currentAmount;

    private final Object lock = new Object(); // ✅ Single lock for both variables

    public FixedGasTank(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.currentAmount = 0;
    }

    public void storeGas(int amount) {
        synchronized (lock) { // ✅ Entire check-and-store is atomic
            if (currentAmount + amount > maxCapacity) {
                System.out.println(Thread.currentThread().getName() + " ❌ Cannot store " + amount + " gas. Not enough space!");
                return;
            }

            try { Thread.sleep(1000); } catch (InterruptedException ignored) {} // Simulate delay

            currentAmount += amount;
            System.out.println(Thread.currentThread().getName() + " ✅ Stored " + amount + " gas. New amount: " + currentAmount);
        }
    }

    public void printStatus() {
        synchronized (lock) {
            if (currentAmount > maxCapacity)
                System.out.println("🔥 Tank Status: " + currentAmount + "/" + maxCapacity + " OVERHEAT!");
            else
                System.out.println("📊 Tank Status: " + currentAmount + "/" + maxCapacity);   
        }
    }
}
public class GasTankFixed {
    public static void main(String[] args) throws InterruptedException {
        FixedGasTank tank = new FixedGasTank(100);

        // Two threads trying to store gas at the same time
        Thread t1 = new Thread(() -> tank.storeGas(60), "Thread-1");
        Thread t2 = new Thread(() -> tank.storeGas(50), "Thread-2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        tank.printStatus();
    }
}
