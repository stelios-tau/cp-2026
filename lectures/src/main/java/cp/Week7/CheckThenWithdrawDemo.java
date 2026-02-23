package cp.Week7;

class BankAccount {
    private int balance;

    public BankAccount(int balance) {
        this.balance = balance;
    }

    public void withdraw(int amount) {
        if (balance >= amount) { // Check
            try { Thread.sleep((long)(Math.random() * 100)); } catch (InterruptedException ignored) {} // Simulate random delay
            balance -= amount; // Write (another way to see the problem is add some breakpoints here)
            System.out.println(Thread.currentThread().getName() + " withdrew " + amount);
        } else {
            System.out.println(Thread.currentThread().getName() + " insufficient funds.");
        }
    }

    public int getBalance() {
        return balance;
    }
}

public class CheckThenWithdrawDemo {
    public static void main(String[] args) throws InterruptedException {
        BankAccount account = new BankAccount(100); // Initial balance: 100

        // Two threads trying to withdraw 70 each (should fail on second withdrawal)
        Thread t1 = new Thread(() -> account.withdraw(70), "ATM 1");
        Thread t2 = new Thread(() -> account.withdraw(70), "ATM 2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Final Balance: " + account.getBalance()); // Should be 30, but might be incorrect!
    }
}

