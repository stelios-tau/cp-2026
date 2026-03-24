package cp.Week13.WaitNotify;

import java.util.LinkedList;
import java.util.Queue;

public class WaitNotifyDemo {
    private static final Queue<String> buffer = new LinkedList<>();

    public static void main(String[] args) throws InterruptedException {
        Thread consumer = new Thread(() -> {
            synchronized (buffer) {
                // A loop like the one below might hide the bug.
                while (buffer.isEmpty()) {
                    System.out.println("🚫 Consumer waiting...");
                    try {
                        buffer.wait(); // Will wait forever if notify() happens too early
                    } catch (InterruptedException ignored) {}
                }
                /*do  {
                    System.out.println("🚫 Consumer waiting...");
                    try {
                        buffer.wait(); // Will wait forever if notify() happens too early
                    } catch (InterruptedException ignored) {}
                } while (buffer.isEmpty());*/

                String item = buffer.remove();
                System.out.println("✅ Consumer got: " + item);
            }
        });

        Thread producer = new Thread(() -> {
            try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            synchronized (buffer) {
                buffer.add("🥗 Salad");
                System.out.println("👨‍🍳 Producer added item and notifies...");
                buffer.notify(); // Too early?
            }
        });

        // Reverse the start order to show race
        consumer.start();
        //Thread.sleep(100); // Let producer finish first
        producer.start();
    }
}
