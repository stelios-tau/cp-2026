package cp.Week11.Publishing;

interface EventListenerLambda {
    void onEvent(String message);
}

class EventSourceLambda {
    private EventListenerLambda listener;

    public void registerListener(EventListenerLambda listener) {
        this.listener = listener;
    }

    public void fireEvent() {
        if (listener != null) {
            listener.onEvent("🔥 Event triggered!");
        }
    }
}

public class ThisEscapeLambda {
    private int data; //I removed the "final" keyword because Java complained

    public ThisEscapeLambda(EventSourceLambda source) {
        // 🔥 Lambda captures `this` implicitly—still unsafe!
        source.registerListener(message -> {
            System.out.println("📢 Event received: " + message);
            System.out.println("❌ Accessing data: " + data); // Might be uninitialized!
        });

        // Simulate a slow constructor
        try { Thread.sleep(200); } catch (InterruptedException ignored) {}

        this.data = 42; // 🚨 Assigned *after* `this` is leaked!
        System.out.println("✅ Constructor finished, data initialized.");
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("🚀 Starting test...");

        EventSourceLambda eventSource = new EventSourceLambda();

        // Run event firing in a separate thread
        new Thread(() -> {
            try { Thread.sleep(50); } catch (InterruptedException ignored) {} // Delay to allow race condition
            eventSource.fireEvent();
        }).start();

        new ThisEscapeLambda(eventSource); // 🚨 `this` escapes before full initialization!
    }
}
