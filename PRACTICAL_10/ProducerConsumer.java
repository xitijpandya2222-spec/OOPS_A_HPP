
import java.util.Scanner;

class Buffer {
    int item;
    boolean available = false;

    // Producer adds an item
    synchronized void produce(int value) throws InterruptedException {
        while (available) {
            wait();
        }

        item = value;
        available = true;

        System.out.println("Produced: " + item);

        notify();
    }

    // Consumer removes an item
    synchronized void consume() throws InterruptedException {
        while (!available) {
            wait();
        }

        System.out.println("Consumed: " + item);

        available = false;

        notify();
    }
}

class Producer implements Runnable {
    Buffer buffer;
    int count;

    Producer(Buffer buffer, int count) {
        this.buffer = buffer;
        this.count = count;
    }

    public void run() {
        try {
            for (int i = 1; i <= count; i++) {
                buffer.produce(i);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Consumer implements Runnable {
    Buffer buffer;
    int count;

    Consumer(Buffer buffer, int count) {
        this.buffer = buffer;
        this.count = count;
    }

    public void run() {
        try {
            for (int i = 1; i <= count; i++) {
                buffer.consume();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class ProducerConsumer {
    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of items: ");
        int n = sc.nextInt();

        Buffer buffer = new Buffer();

        Thread producer = new Thread(new Producer(buffer, n), "Producer");
        Thread consumer = new Thread(new Consumer(buffer, n), "Consumer");

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();

        System.out.println("All items produced and consumed successfully.");

        sc.close();
    }
}
