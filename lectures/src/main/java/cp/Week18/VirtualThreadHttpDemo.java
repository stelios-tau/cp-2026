package cp.Week19;

import java.net.URI;
import java.net.http.*;
import java.util.concurrent.*;

public class VirtualThreadHttpDemo {
    static final int TASK_COUNT = 1_000;
    static final String TEST_URL = "https://httpbin.org/delay/1"; // 1-second delay

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Platform threads:");
        runWithExecutor(Executors.newFixedThreadPool(100));

        System.out.println("\nVirtual threads:");
        runWithExecutor(Executors.newVirtualThreadPerTaskExecutor());
    }

    static void runWithExecutor(ExecutorService executor) throws InterruptedException {
        long start = System.currentTimeMillis();

        CountDownLatch latch = new CountDownLatch(TASK_COUNT);
        HttpClient client = HttpClient.newHttpClient();

        for (int i = 0; i < TASK_COUNT; i++) {
            executor.submit(() -> {
                try {
                    HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(TEST_URL))
                        .build();

                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    System.out.println(Thread.currentThread() + " → " + response.statusCode());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        long end = System.currentTimeMillis();
        System.out.printf("✅ Completed in %.2f seconds%n", (end - start) / 1000.0);
    }
}

