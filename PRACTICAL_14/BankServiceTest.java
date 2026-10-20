
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class BankServiceTest {

    @Test
    void depositIncreasesBalance() {
        Repository<Account> repository = new Repository<>();
        BankService service = new BankService(repository);

        service.openAccount("A101", "Rahul", 1000);
        service.deposit("A101", 500);

        assertEquals(
            1500.0,
            repository.findById("A101").getBalance(),
            0.001
        );
    }

    @Test
    void withdrawalOverLimitThrowsException() {
        Repository<Account> repository = new Repository<>();
        BankService service = new BankService(repository);

        service.openAccount("A101", "Rahul", 100000);

        assertThrows(
            InvalidAmountException.class,
            () -> service.withdraw("A101", 60000)
        );
    }

    @Test
    void concurrentDepositsKeepCorrectBalance()
            throws InterruptedException, ExecutionException {

        Repository<Account> repository = new Repository<>();
        BankService service = new BankService(repository);

        service.openAccount("A101", "Rahul", 1000);

        ExecutorService pool = Executors.newFixedThreadPool(10);
        List<Future<?>> tasks = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            tasks.add(pool.submit(() -> service.deposit("A101", 10)));
        }

        for (Future<?> task : tasks) {
            task.get();
        }

        pool.shutdown();

        assertEquals(
            2000.0,
            repository.findById("A101").getBalance(),
            0.001
        );
    }
}
