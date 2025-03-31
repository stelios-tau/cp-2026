package cp.week14.BlockingQueues;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;

public class CleanBlockingQueueDemo {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(1);

        Thread consumer = new Thread(() -> {
            try {
                String item = queue.take(); // Waits if empty
                System.out.println("✅ Consumer got: " + item);
            } catch (InterruptedException ignored) {}
        });

        Thread producer = new Thread(() -> {
            try {
                Thread.sleep(100);
                queue.put("🍝 Pasta"); // Waits if full
                System.out.println("👨‍🍳 Producer added item.");
            } catch (InterruptedException ignored) {}
        });

        producer.start();
        Thread.sleep(500); // Still safe! BlockingQueue handles it
        consumer.start();
    }
}

