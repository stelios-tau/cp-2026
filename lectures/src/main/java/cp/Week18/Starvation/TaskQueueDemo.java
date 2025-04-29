package cp.Week18.Starvation;

import java.util.concurrent.*;

public class TaskQueueDemo {
    static BlockingDeque<Runnable> taskQueue = new LinkedBlockingDeque<>();

    static boolean flag = true;

    public static void main(String[] args) {
        // Populate queue with tasks
        taskQueue.add(() -> doTask("A", false, false));
        taskQueue.add(() -> doTask("B", true, false)); 
        taskQueue.add(() -> doTask("C", true, true));

        // Worker thread
        new Thread(() -> {
            while (true) {
                try {
                    Runnable task = taskQueue.take();
                    task.run();
                } catch (InterruptedException ignored) {}
            }
        }).start();
    }

    static void doTask(String name, boolean failFlag, boolean override) {
        System.out.println("🔄 Executing task " + name);

        if (!flag && !override) {
            System.out.println("❌ Task " + name + " failed! Requeuing...");

             // Bad! Infinite retry! Task C is starved! Task B is in retry-storm!
             // By Goetz' definition, Task B is also in a livelock.
            taskQueue.addFirst(() -> doTask(name, failFlag, override));
        } else {
            flag = failFlag;
            System.out.println("✅ Task " + name + " completed.");
        }
    }
}
