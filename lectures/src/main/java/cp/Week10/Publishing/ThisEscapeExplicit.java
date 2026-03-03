package cp.Week10.Publishing;

interface EventListenerExpl {
    void onEvent(UnsafePublisher publisher);
}

class UnsafePublisher {
    private final int data;

    public UnsafePublisher(EventListenerExpl listener) {
        listener.onEvent(this);  // ❌ `this` escapes before full initialization!
        System.out.println("🚨 `this` escaped to another thread!");

        // Simulate a long constructor (ensures observer gets time to access `this`)
        try { 
            Thread.sleep(200);  
        } catch (InterruptedException ignored) {}

        this.data = 42; // 🚨 Assigned *after* `this` is published!
        System.out.println("✅ Constructor finished, data initialized.");
    }

    public int getData() {
        return data;
    }
}

public class ThisEscapeExplicit {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("🚀 Starting test...");

        // Shared event listener that can access `UnsafePublisher`
        EventListenerExpl listener = new EventListenerExpl() {
            private UnsafePublisher instance;

            @Override
            public void onEvent(UnsafePublisher publisher) {
                this.instance = publisher; // Store reference to leaked object

                // Another thread will repeatedly try to read the object
                new Thread(() -> {
                    while (true) {
                        //try { 
                        //    Thread.sleep(1200);  
                        //} catch (InterruptedException ignored) {}  
                        if (instance != null) {
                            int value = instance.getData();
                            System.out.println("📢 Observer read data: " + value);
                            if (value == 0) {
                                System.out.println("❌ Data was read before full initialization!");
                                System.exit(0);
                            }
                        }
                        try {
                            Thread.sleep(5); // Allow time for incorrect access
                        } catch (InterruptedException ignored) {}
                    }
                }).start();
            }
        };

        // Create an instance that leaks `this`
        UnsafePublisher obj = new UnsafePublisher(listener);

        System.out.println("🏁 Test finished.");
    }
}
