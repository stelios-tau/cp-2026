package cp.Week7;

import java.util.concurrent.*;

public class SingleThreadedAsync {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("Main thread: " + Thread.currentThread().getName());

        CompletableFuture<Void> task1 = CompletableFuture.runAsync(() -> {
            try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            System.out.println("Task 1 executed on: " + Thread.currentThread().getName());
        });

        CompletableFuture<Void> task2 = CompletableFuture.runAsync(() -> {
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
            System.out.println("Task 2 executed on: " + Thread.currentThread().getName());
        });

        CompletableFuture<Void> task3 = CompletableFuture.runAsync(() -> {
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
            System.out.println("Task 3 executed on: " + Thread.currentThread().getName());
        });

        CompletableFuture.allOf(task1, task2, task3).get();

        System.out.println("Main thread exiting.");
    }
}
