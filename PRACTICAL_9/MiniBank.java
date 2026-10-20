
import java.util.Scanner;

class Account {
    int balance;

    Account(int balance) {
        this.balance = balance;
    }

    // Deposit money
    synchronized void deposit(int amount) {
        balance = balance + amount;
    }

    // Withdraw money
    synchronized void withdraw(int amount) {
        balance = balance - amount;
    }

    // Get final balance
    synchronized int getBalance() {
        return balance;
    }
}

class AccountWorker implements Runnable {
    Account account;
    int times;
    long amount;

    AccountWorker(Account account, int times, long amount) {
        this.account = account;
        this.times = times;
        this.amount = amount;
    }

    public void run() {
        System.out.println(Thread.currentThread().getName() + " started");

        for (int i = 1; i <= times; i++) {
            account.deposit((int) amount);
        }

        System.out.println(Thread.currentThread().getName() + " finished");
    }
}

public class MiniBank {
    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter initial balance: ");
        int balance = sc.nextInt();

        System.out.print("Enter deposit amount per thread: ");
        long amount = sc.nextLong();

        System.out.print("Enter number of deposits per thread: ");
        int times = sc.nextInt();

        System.out.print("Enter number of threads (maximum 10): ");
        int n = sc.nextInt();

        Account account = new Account(balance);

        Thread[] threads = new Thread[n];

        // Create and start threads
        for (int i = 0; i < n; i++) {
            AccountWorker worker = new AccountWorker(account, times, amount);

            threads[i] = new Thread(worker, "Worker-" + (i + 1));
            threads[i].start();
        }

        // Wait for all threads to finish
        for (int i = 0; i < n; i++) {
            threads[i].join();
        }

        System.out.println("Final Balance: " + account.getBalance());

        sc.close();
    }
}
