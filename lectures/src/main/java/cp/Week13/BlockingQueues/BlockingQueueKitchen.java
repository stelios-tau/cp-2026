package cp.Week14.BlockingQueues;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class CookingTask {
    private final String description;

    public CookingTask(String description) {
        this.description = description;
    }

    public void execute(String workerName) {
        System.out.println("🍽️ " + workerName + " is doing: " + description);
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
    }
}

class Chef extends Thread {
    private final BlockingQueue<CookingTask> queue;
    private final String[] tasks;

    public Chef(String name, BlockingQueue<CookingTask> queue, String[] tasks) {
        super(name);
        this.queue = queue;
        this.tasks = tasks;
    }

    public void run() {
        try {
            for (String task : tasks) {
                CookingTask ct = new CookingTask(task);
                queue.put(ct); // Blocks if queue is full
                System.out.println("👨‍🍳 " + getName() + " prepared: " + task);
                Thread.sleep(300);
            }
        } catch (InterruptedException ignored) {}
    }
}

class Worker extends Thread {
    private final BlockingQueue<CookingTask> queue;

    public Worker(String name, BlockingQueue<CookingTask> queue) {
        super(name);
        this.queue = queue;
    }

    public void run() {
        try {
            while (true) {
                CookingTask task = queue.take(); // Blocks if queue is empty
                task.execute(getName());
            }
        } catch (InterruptedException e) {
            System.out.println("💤 " + getName() + " is done for the day.");
        }
    }
}

public class BlockingQueueKitchen {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<CookingTask> queue = new LinkedBlockingQueue<>(5); // Small buffer

        String[] dish1 = {"Chop tomatoes", "Boil pasta", "Plate pasta"};
        String[] dish2 = {"Chop onions", "Cook sauce", "Combine everything"};

        Chef chef1 = new Chef("Chef Alice", queue, dish1);
        Chef chef2 = new Chef("Chef Bob", queue, dish2);

        Worker worker1 = new Worker("Sous-chef Mike", queue);
        Worker worker2 = new Worker("Sous-chef Jane", queue);

        worker1.start();
        worker2.start();
        chef1.start();
        chef2.start();

        chef1.join();
        chef2.join();

        // Let workers finish their queue, then interrupt them
        Thread.sleep(3000);
        worker1.interrupt();
        worker2.interrupt();
    }
}

