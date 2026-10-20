
import java.util.Scanner;
import java.util.concurrent.*;

// Bank Account
class Account {
    int accountNumber;
    int balance;

    Account(int accountNumber, int balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    synchronized void deposit(int amount) {
        balance = balance + amount;
        System.out.println("Deposited " + amount
                + ", Balance = " + balance);
    }

    synchronized void withdraw(int amount) {
        if (balance >= amount) {
            balance = balance - amount;
            System.out.println("Withdrawn " + amount
                    + ", Balance = " + balance);
        } else {
            System.out.println("Insufficient balance");
        }
    }

    // Safe transfer: lock accounts in ascending order
    static void transferSafe(Account a, Account b, int amount) {
        Account first = a.accountNumber < b.accountNumber ? a : b;
        Account second = a.accountNumber < b.accountNumber ? b : a;

        synchronized (first) {
            synchronized (second) {
                if (a.balance >= amount) {
                    a.balance = a.balance - amount;
                    b.balance = b.balance + amount;

                    System.out.println("Safe transfer completed: "
                            + amount);
                }
            }
        }
    }

    // Unsafe transfer: locks accounts in the given order
    static void transferUnsafe(Account a, Account b,
                               CountDownLatch latch) {
        synchronized (a) {
            latch.countDown();

            try {
                latch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            synchronized (b) {
                System.out.println("Unsafe transfer completed");
            }
        }
    }
}

// Transaction task
class Transaction implements Runnable {
    Account account;
    int id;

    Transaction(Account account, int id) {
        this.account = account;
        this.id = id;
    }

    public void run() {
        System.out.println("Task " + id + " running on "
                + Thread.currentThread().getName());

        if (id % 2 == 0) {
            account.deposit(100);
        } else {
            account.withdraw(50);
        }
    }
}

// Thread pool manager
class TransactionProcessor {
    ExecutorService service =
            Executors.newFixedThreadPool(4);

    void submit(Runnable task) {
        service.execute(task);
    }

    void stop() throws InterruptedException {
        service.shutdown();
        service.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("All transactions completed.");
    }
}

// Shared array buffer
class Buffer {
    int[] arr = new int[5];
    int in = 0, out = 0, count = 0;

    synchronized void put(int value) throws InterruptedException {
        while (count == arr.length) {
            wait();
        }

        arr[in] = value;
        in = (in + 1) % arr.length;
        count++;

        System.out.println("Produced: " + value);
        notify();
    }

    synchronized int get() throws InterruptedException {
        while (count == 0) {
            wait();
        }

        int value = arr[out];
        out = (out + 1) % arr.length;
        count--;

        notify();
        return value;
    }
}

// Producer thread
class Producer implements Runnable {
    Buffer buffer;
    int n;

    Producer(Buffer buffer, int n) {
        this.buffer = buffer;
        this.n = n;
    }

    public void run() {
        try {
            for (int i = 1; i <= n; i++) {
                buffer.put(i);
            }

            buffer.put(-1); // End signal
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// Consumer thread
class Consumer implements Runnable {
    Buffer buffer;

    Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    public void run() {
        try {
            while (true) {
                int value = buffer.get();

                if (value == -1) {
                    break;
                }

                System.out.println("Consumed: " + value);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// Main class
public class MiniBank {
    public static void main(String[] args)
            throws InterruptedException {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter initial balance: ");
        int balance = sc.nextInt();

        System.out.print("Enter number of transactions: ");
        int n = sc.nextInt();

        System.out.print("Enter number of buffer items: ");
        int items = sc.nextInt();

        // 1. Thread pool transactions
        Account account = new Account(1, balance);
        TransactionProcessor processor =
                new TransactionProcessor();

        System.out.println("\n--- Transaction Processing ---");

        for (int i = 1; i <= n; i++) {
            processor.submit(new Transaction(account, i));
        }

        processor.stop();
        System.out.println("Final balance: " + account.balance);

        // 2. Producer-consumer
        System.out.println("\n--- Producer Consumer ---");

        Buffer buffer = new Buffer();

        Thread producer = new Thread(
                new Producer(buffer, items), "Producer");

        Thread consumer = new Thread(
                new Consumer(buffer), "Consumer");

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();

        System.out.println("Producer-consumer completed.");

        // 3. Deadlock demonstration
        System.out.println("\n--- Deadlock Demonstration ---");

        Account a = new Account(10, 1000);
        Account b = new Account(20, 1000);

        CountDownLatch latch = new CountDownLatch(2);

        Thread t1 = new Thread(() ->
                Account.transferUnsafe(a, b, latch));

        Thread t2 = new Thread(() ->
                Account.transferUnsafe(b, a, latch));

        // Daemon threads will not prevent program exit
        t1.setDaemon(true);
        t2.setDaemon(true);

        t1.start();
        t2.start();

        System.out.println("Opposite-order locking started.");
        System.out.println("Deadlock may occur in these threads.");

        // 4. Deadlock prevention
        System.out.println("\n--- Deadlock Prevention ---");

        Account x = new Account(1, 1000);
        Account y = new Account(2, 1000);

        Thread t3 = new Thread(() ->
                Account.transferSafe(x, y, 100));

        Thread t4 = new Thread(() ->
                Account.transferSafe(y, x, 100));

        t3.start();
        t4.start();

        t3.join();
        t4.join();

        System.out.println("Safe transfers completed.");
        System.out.println("Account X balance: " + x.balance);
        System.out.println("Account Y balance: " + y.balance);

        sc.close();
    }
}
