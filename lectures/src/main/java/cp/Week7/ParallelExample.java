package cp.Week7;

// The execution is deterministic, at least assuming true parallel execution under the hood.
// Another deterministic perspective is that the output of the program is *always* "Done!\nDone!".

class ParTask1 extends Thread {
    public void run() {
        for (int i = 1; i <= 7; i++) {
           try { Thread.sleep((long)(Math.random() * 500)); } catch (InterruptedException ignored) {} // Simulate random delay
        }
        System.out.println("Done!");
    }
}

class ParTask2 extends Thread {
    public void run() {
        for (int i = 1; i <= 7; i++) {
            try { Thread.sleep((long)(Math.random() * 500)); } catch (InterruptedException ignored) {} // Simulate random delay
        }
        System.out.println("Done!");
    }
}

public class ParallelExample {
    public static void main(String[] args) {
        ParTask1 t1 = new ParTask1();
        ParTask2 t2 = new ParTask2();
        t1.start();
        t2.start();
    }
}
