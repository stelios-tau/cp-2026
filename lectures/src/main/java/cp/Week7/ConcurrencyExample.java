package cp.Week7;

// Simplified example. Tasks are not a sequence of sub-tasks but, rather, an infinite list. 

class Task1 extends Thread {
    public void run() {
        for (int i = 1; i <= 7; i++) {
            int res = (int)Math.round( Math.random());
            String dish;
            if (res == 0)
                dish = "Pasta";
            else
                dish = "Steak";

            //System.out.printf("↓ %s - %d%n", dish,i); 
            System.out.printf("↓ %s%n", dish); 
           try { Thread.sleep((long)(Math.random() * 500)); } catch (InterruptedException ignored) {} // Simulate random delay
        }
    }
}

class Task2 extends Thread {
    public void run() {
        
        for (int i = 1; i <= 7; i++) {
            int res = (int)Math.round( Math.random());
            String dish;
            if (res == 0)
                dish = "Pasta";
            else
                dish = "Steak";
            
            //System.out.printf("%30s↓ %s - %c%n", "", dish,i+96);
            System.out.printf("%30s↓ %s%n", "", dish);
            try { Thread.sleep((long)(Math.random() * 500)); } catch (InterruptedException ignored) {} // Simulate random delay
        }
    }
}

public class ConcurrencyExample {
    public static void main(String[] args) {
        Task1 t1 = new Task1();
        Task2 t2 = new Task2();
        t1.start();
        t2.start();
    }
}
