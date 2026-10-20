
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class MyTask implements Runnable {
    int id;

    MyTask(int id) {
        this.id = id;
    }

    public void run() {
        System.out.println("Task " + id + " started by "
                + Thread.currentThread().getName());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Task interrupted");
        }

        System.out.println("Task " + id + " completed by "
                + Thread.currentThread().getName());
    }
}

public class ThreadPoolRunner {
    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of tasks: ");
        int n = sc.nextInt();

        ExecutorService pool = Executors.newFixedThreadPool(3);

        for (int i = 1; i <= n; i++) {
            pool.execute(new MyTask(i));
        }

        pool.shutdown();

        pool.awaitTermination(1, TimeUnit.MINUTES);

        System.out.println("All tasks completed.");
        sc.close();
    }
}
