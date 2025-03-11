package cp.Week11.Publishing;

/*
 * Safe publication via factory. Immutability is at play here.
 */

interface EventListenerSafe {
    void onEvent(String message);
}

class EventSourceSafe {
    private EventListenerSafe listener;

    public void registerListener(EventListenerSafe listener) {
        this.listener = listener;
    }

    public void fireEvent() {
        if (listener != null) {
            listener.onEvent("🔥 Event triggered!");
        }
    }
}

public class SafeThisEscape {
    private final int data; //Final data are important

    private SafeThisEscape(EventSourceSafe source) { // ✅ Private constructor prevents direct instantiation
        this.data = 42; // ✅ Fully initialized before registering the listener
    }

    public static SafeThisEscape newInstance(EventSourceSafe source) {
        SafeThisEscape obj = new SafeThisEscape(source); // ✅ Full initialization first
        source.registerListener(message -> obj.onEvent(message)); // ✅ Uses fully initialized object
        return obj;
    }

    public void onEvent(String event) {
        System.out.println("📢 Event received: " + event);
        System.out.println("✅ Accessing data: " + data); // Always correct!
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("🚀 Starting test...");

        EventSourceSafe eventSource = new EventSourceSafe();

        // Run event firing in a separate thread
        new Thread(() -> {
            try { Thread.sleep(50); } catch (InterruptedException ignored) {}
            eventSource.fireEvent();
        }).start();

        SafeThisEscape obj = SafeThisEscape.newInstance(eventSource); // ✅ Safe instantiation
    }
}
