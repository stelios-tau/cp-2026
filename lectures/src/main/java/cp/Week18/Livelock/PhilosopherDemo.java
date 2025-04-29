package cp.Week18.Livelock;

public class PhilosopherDemo {

    static class Fork {
        private boolean isTaken = false;

        synchronized boolean pickUp() {
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            if (!isTaken) {
                isTaken = true;
                return true;
            }
            return false;
        }

        synchronized void putDown() {
            isTaken = false;
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
        }
    }

    static class Philosopher extends Thread {
        private final Fork left, right;
        private final String name;

        Philosopher(String name, Fork left, Fork right) {
            this.name = name;
            this.left = left;
            this.right = right;
        }

        public void run() {
            while (true) {
                // Try to pick up left fork
                if (left.pickUp()) {
                    System.out.println(name + " picked up left fork.");

                    // Try to pick up right fork
                    if (right.pickUp()) {
                        System.out.println(name + " picked up right fork.");
                        System.out.println(name + " starts eating!");
                        break;
                    } else {
                        // Failed to pick up right — put down left and retry
                        left.putDown();
                        System.out.println(name + " put down left fork (retrying)...");
                    }
                }
                try { Thread.sleep(50); } catch (InterruptedException ignored) {}
            }
        }
    }

    public static void main(String[] args) {
        Fork forkA = new Fork();
        Fork forkB = new Fork();

        Philosopher p1 = new Philosopher("Philosopher 1", forkA, forkB);
        Philosopher p2 = new Philosopher("Philosopher 2", forkB, forkA);

        p1.start();
        p2.start();
    }
}

