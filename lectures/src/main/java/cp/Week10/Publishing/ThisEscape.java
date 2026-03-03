package cp.Week10.Publishing;

interface EventListener {
    void onEvent(String message);
}

class EventSource {
    private EventListener listener;

    public void registerListener(EventListener listener) {
        this.listener = listener;
    }

    public void fireEvent() {
        if (listener != null) {
            listener.onEvent("🔥 Event triggered!");
        }
    }
}

public class ThisEscape {
    private final int data;

    public ThisEscape(EventSource source) {
        //Anonymous class! Unsafe in this case.
        source.registerListener(new EventListener() {
            public void onEvent(String message) {
                System.out.println("📢 Event received: " + message);
                System.out.println("❌ Accessing data: " + data); // Might be uninitialized!
            }
        });

        // Simulate a slow constructor
        try { Thread.sleep(200); } catch (InterruptedException ignored) {}

        this.data = 42; // 🚨 Assigned *after* `this` is leaked!
        System.out.println("✅ Constructor finished, data initialized.");
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("🚀 Starting test...");

        EventSource eventSource = new EventSource();

        // Run event firing in a separate thread
        new Thread(() -> {
             // Delay to allow race condition (comment out to avoid it)
            try { Thread.sleep(50); } catch (InterruptedException ignored) {}
            eventSource.fireEvent();
        }).start();

        new ThisEscape(eventSource); // 🚨 `this` escapes to `eventSource` before full initialization!
    }
}
