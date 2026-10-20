class Counter {
    int count = 0;

    void increment() {
        count++;
    }
}

class MyThread extends Thread {
    Counter c;

    MyThread(Counter c) {
        this.c = c;
    }

    public void run() {
        for (int i = 0; i < 10000; i++)
            c.increment();
    }
}

public class CounterRace {
    public static void main(String[] args) throws Exception {
        Counter c = new Counter();

        MyThread t1 = new MyThread(c);
        MyThread t2 = new MyThread(c);
        MyThread t3 = new MyThread(c);

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        System.out.println("Final count = " + c.count);
        System.out.println("Expected count = 30000");
    }
}