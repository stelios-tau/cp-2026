package cp.Week11.Design;

/*
 * The GasTank example
 * Why does delegation fail?
 * What is missing from the code?
 */

class GasTank {
    private int maxCapacity;
    private int currentAmount;
    
    private final Object maxLock = new Object(); // Lock for maxCapacity
    private final Object amountLock = new Object(); // Lock for currentAmount

    public GasTank(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.currentAmount = 0;
    }

    public void storeGas(int amount) {
        // Step 1: Check available space (uses maxLock)
        synchronized (maxLock) {
            if (currentAmount + amount > maxCapacity) {
                System.out.println(Thread.currentThread().getName() + " ❌ Cannot store " + amount + " gas. Not enough space!");
                return;
            }
        }

        // Simulate delay (allowing another thread to interfere)
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}

        // Step 2: Add gas to currentAmount (uses amountLock)
        synchronized (amountLock) {
            currentAmount += amount;
            System.out.println(Thread.currentThread().getName() + " ✅ Stored " + amount + " gas. New amount: " + currentAmount);
        }
    }

    public void withdrawGas(int amount) {
        synchronized (amountLock) {
            if (currentAmount >= amount) {
                currentAmount -= amount;
                System.out.println(Thread.currentThread().getName() + " 🔽 Withdrew " + amount + " gas. Remaining: " + currentAmount);
            } else {
                System.out.println(Thread.currentThread().getName() + " ❌ Not enough gas to withdraw " + amount + ".");
            }
        }
    }

    public void printStatus() {
        synchronized (amountLock) {
            synchronized (maxLock) {
                if (currentAmount > maxCapacity)
                    System.out.println("🔥 Tank Status: " + currentAmount + "/" + maxCapacity + " OVERHEAT!");
                else
                    System.out.println("📊 Tank Status: " + currentAmount + "/" + maxCapacity);   
            }
        }
    }
}

public class GasTankRaceDemo {
    public static void main(String[] args) throws InterruptedException {
        GasTank tank = new GasTank(100);

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
