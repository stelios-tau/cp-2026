package cp.Week11.Publishing;

interface EventListener {
    void onEvent();
}

class UnsafePublisher {
    private final int data;

    public UnsafePublisher(EventListener listener) {
        listener.onEvent(); // ❌ `this` escapes here!

        // Simulate delayed initialization
        try { Thread.sleep(100); } catch (InterruptedException ignored) {}

        this.data = 42; // 🚨 Assigned *after* `this` is published!
        System.out.println("✅ Constructor finished, data initialized.");
    }

    public int getData() {
        return data;
    }
}

public class ThisEscapeExample {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("🚀 Starting test...");

        // A shared event listener that accesses `UnsafePublisher`
        EventListener listener = new EventListener() {
            private UnsafePublisher instance;

            @Override
            public void onEvent() {
                if (instance != null) {
                    System.out.println("📢 Event received! Accessing data...");
                    System.out.println("Data: " + instance.getData()); // ❌ Might read uninitialized value!
                }
            }

            public void setInstance(UnsafePublisher obj) {
                this.instance = obj;
            }
        };

        // Start a thread that will try to access the escaping object
        Thread observer = new Thread(() -> {
            try { Thread.sleep(50); } catch (InterruptedException ignored) {} // Delay ensures access before full init
            listener.onEvent();
        });

        observer.start();

        // Create an instance that leaks `this`
        UnsafePublisher obj = new UnsafePublisher(listener);
        ((EventListener) listener).onEvent(); // Direct access attempt

        observer.join(); // Wait for observer to finish
        System.out.println("🏁 Test finished.");
    }
}
