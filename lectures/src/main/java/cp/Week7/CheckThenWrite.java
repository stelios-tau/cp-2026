package cp.Week7;
import java.util.*;

class SharedList {
    private final List<String> list = new ArrayList<>(); // Shared among threads!

    public void addIfNotPresent(String item) {
        if (!list.contains(item)) {  // Check
            try { Thread.sleep(1); } catch (InterruptedException ignored) {} // Simulating delay
            list.add(item);           // Write, you can insert breakpoint here to catch both threads
        }
    }

    public List<String> getList() {
        return list;
    }
}

public class CheckThenWrite {
    public static void main(String[] args) throws InterruptedException {
        SharedList sharedList = new SharedList();

        Thread t1 = new Thread(() -> sharedList.addIfNotPresent("Entry1"));
        Thread t2 = new Thread(() -> sharedList.addIfNotPresent("Entry1"));

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Final List: " + sharedList.getList());
    }
}
