
import java.io.*;
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.*;

// Annotation used to identify the account ID
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Id {
}

// Custom exceptions
class InvalidAmountException extends RuntimeException {
    InvalidAmountException(String message) {
        super(message);
    }
}

class AccountNotFoundException extends RuntimeException {
    AccountNotFoundException(String message) {
        super(message);
    }
}

class InsufficientFundsException extends RuntimeException {
    InsufficientFundsException(String message) {
        super(message);
    }
}

// Account model
class Account implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String accountNumber;
    private String name;
    private double balance;

    Account(String accountNumber, String name, double balance) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getName() {
        return name;
    }

    public synchronized double getBalance() {
        return balance;
    }

    synchronized void deposit(double amount) {
        balance += amount;
    }

    synchronized void withdraw(double amount) {
        balance -= amount;
    }

    public String toString() {
        return "Account: " + accountNumber
                + ", Name: " + name
                + ", Balance: " + getBalance();
    }
}

// Generic repository using a thread-safe map
class Repository<T> {
    private ConcurrentHashMap<String, T> data =
            new ConcurrentHashMap<>();

    public T save(T entity) {
        try {
            for (Field field : entity.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    field.setAccessible(true);
                    Object value = field.get(entity);

                    if (value == null) {
                        throw new IllegalArgumentException("ID cannot be null");
                    }

                    data.put(value.toString(), entity);
                    return entity;
                }
            }

            throw new IllegalArgumentException("No @Id field found");

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public T findById(String id) {
        return data.get(id);
    }

    public ArrayList<T> findAll() {
        return new ArrayList<>(data.values());
    }

    public boolean delete(String id) {
        return data.remove(id) != null;
    }

    @SafeVarargs
    public final void saveAll(T... items) {
        for (T item : items) {
            save(item);
        }
    }
}

// Input validation
class Validator {
    public void validateAccount(String id, String name) {
        if (id == null || id.trim().isEmpty()
                || name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Account number and name are required");
        }
    }

    public void validateAmount(double amount) {
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new InvalidAmountException(
                    "Amount must be a positive valid number");
        }
    }
}

// Transaction model
class BankTransaction implements Serializable {
    private static final long serialVersionUID = 1L;

    String details;
    double amount;

    BankTransaction(String details, double amount) {
        this.details = details;
        this.amount = amount;
    }

    public String toString() {
        return details + " | Amount: " + amount;
    }
}

// Bank service
class BankService {
    private Repository<Account> repository;
    private Validator validator = new Validator();
    private ArrayList<BankTransaction> transactions =
            new ArrayList<>();

    BankService(Repository<Account> repository) {
        this.repository = repository;
    }

    public synchronized void openAccount(
            String id, String name, double initialBalance) {
        validator.validateAccount(id, name);

        if (repository.findById(id) != null) {
            throw new IllegalArgumentException(
                    "Account number already exists");
        }

        if (initialBalance < 0 || !Double.isFinite(initialBalance)) {
            throw new InvalidAmountException(
                    "Initial balance cannot be negative");
        }

        Account account = new Account(id, name, initialBalance);
        repository.save(account);

        transactions.add(new BankTransaction(
                "Account opened: " + id, initialBalance));
    }

    public synchronized void deposit(String id, double amount) {
        validator.validateAmount(amount);
        Account account = getAccount(id);

        account.deposit(amount);
        transactions.add(new BankTransaction(
                "Deposit to " + id, amount));
    }

    public synchronized void withdraw(String id, double amount) {
        validator.validateAmount(amount);

        if (amount > 50000) {
            throw new InvalidAmountException(
                    "Withdrawal limit is 50000 per transaction");
        }

        Account account = getAccount(id);

        if (account.getBalance() < amount) {
            throw new InsufficientFundsException(
                    "Insufficient account balance");
        }

        account.withdraw(amount);
        transactions.add(new BankTransaction(
                "Withdrawal from " + id, amount));
    }

    public synchronized void transfer(
            String fromId, String toId, double amount) {
        validator.validateAmount(amount);

        if (fromId.equals(toId)) {
            throw new IllegalArgumentException(
                    "Source and destination must differ");
        }

        Account from = getAccount(fromId);
        Account to = getAccount(toId);

        if (from.getBalance() < amount) {
            throw new InsufficientFundsException(
                    "Insufficient balance for transfer");
        }

        from.withdraw(amount);
        to.deposit(amount);

        transactions.add(new BankTransaction(
                "Transfer from " + fromId + " to " + toId, amount));
    }

    private Account getAccount(String id) {
        Account account = repository.findById(id);

        if (account == null) {
            throw new AccountNotFoundException(
                    "Account not found: " + id);
        }

        return account;
    }

    public synchronized String statement(String id) {
        return getAccount(id).toString();
    }

    public synchronized void listAccounts() {
        for (Account account : repository.findAll()) {
            System.out.println(account);
        }
    }

    public synchronized void printReport() {
        System.out.println("\n--- Bank Report ---");
        System.out.println("Total accounts: "
                + repository.findAll().size());

        double total = 0;

        for (Account account : repository.findAll()) {
            total += account.getBalance();
        }

        System.out.println("Total bank balance: " + total);
        System.out.println("Transactions: " + transactions.size());

        for (BankTransaction transaction : transactions) {
            System.out.println(transaction);
        }

        try (PrintWriter writer =
                     new PrintWriter("bank-report.txt")) {
            writer.println("--- Bank Report ---");
            writer.println("Total accounts: "
                    + repository.findAll().size());
            writer.println("Total bank balance: " + total);
            writer.println("Transactions: " + transactions.size());

            for (BankTransaction transaction : transactions) {
                writer.println(transaction);
            }

            System.out.println("Report saved to bank-report.txt");
        } catch (IOException e) {
            System.out.println("Could not save report: "
                    + e.getMessage());
        }
    }

    public synchronized void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream("accounts.dat"))) {
            out.writeObject(repository.findAll());
        } catch (IOException e) {
            System.out.println("Could not save accounts: "
                    + e.getMessage());
        }

        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream("transactions.dat"))) {
            out.writeObject(transactions);
        } catch (IOException e) {
            System.out.println("Could not save transactions: "
                    + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public synchronized void loadData() {
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream("accounts.dat"))) {
            ArrayList<Account> accounts =
                    (ArrayList<Account>) in.readObject();

            for (Account account : accounts) {
                repository.save(account);
            }
            System.out.println("Saved accounts loaded.");
        } catch (FileNotFoundException e) {
            System.out.println("No previous account data found.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Could not load accounts: "
                    + e.getMessage());
        }

        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream("transactions.dat"))) {
            transactions =
                    (ArrayList<BankTransaction>) in.readObject();
        } catch (FileNotFoundException e) {
            // No previous transaction history
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Could not load transaction history.");
        }
    }
}

// Safe batch processor
class TransactionProcessor {
    public void processBatch(
            BankService service, List<Runnable> tasks) {
        ExecutorService pool = Executors.newFixedThreadPool(4);
        ArrayList<Future<?>> results = new ArrayList<>();

        try {
            for (Runnable task : tasks) {
                results.add(pool.submit(task));
            }

            for (Future<?> result : results) {
                try {
                    result.get();
                } catch (ExecutionException e) {
                    System.out.println("Batch transaction failed: "
                            + e.getCause().getMessage());
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Batch processing interrupted.");
        } finally {
            pool.shutdown();
        }
    }
}

// Main menu
public class MiniBank {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Repository<Account> repository = new Repository<>();
        BankService service = new BankService(repository);
        service.loadData();

        TransactionProcessor processor = new TransactionProcessor();

        while (true) {
            System.out.println("\n===== MINIBANK =====");
            System.out.println("1. Open Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Statement");
            System.out.println("6. List Accounts");
            System.out.println("7. Batch Deposits");
            System.out.println("8. Bank Report");
            System.out.println("9. Save and Exit");
            System.out.print("Enter choice: ");

            try {
                int choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1:
                        System.out.print("Account number: ");
                        String id = sc.nextLine();

                        System.out.print("Customer name: ");
                        String name = sc.nextLine();

                        System.out.print("Initial balance: ");
                        double initial = Double.parseDouble(sc.nextLine());

                        service.openAccount(id, name, initial);
                        System.out.println("Account opened.");
                        break;

                    case 2:
                        System.out.print("Account number: ");
                        id = sc.nextLine();

                        System.out.print("Deposit amount: ");
                        double amount = Double.parseDouble(sc.nextLine());

                        service.deposit(id, amount);
                        System.out.println("Deposit successful.");
                        break;

                    case 3:
                        System.out.print("Account number: ");
                        id = sc.nextLine();

                        System.out.print("Withdrawal amount: ");
                        amount = Double.parseDouble(sc.nextLine());

                        service.withdraw(id, amount);
                        System.out.println("Withdrawal successful.");
                        break;

                    case 4:
                        System.out.print("From account: ");
                        String from = sc.nextLine();

                        System.out.print("To account: ");
                        String to = sc.nextLine();

                        System.out.print("Transfer amount: ");
                        amount = Double.parseDouble(sc.nextLine());

                        service.transfer(from, to, amount);
                        System.out.println("Transfer successful.");
                        break;

                    case 5:
                        System.out.print("Account number: ");
                        id = sc.nextLine();
                        System.out.println(service.statement(id));
                        break;

                    case 6:
                        service.listAccounts();
                        break;

                    case 7:
                        System.out.print("Account number: ");
                        id = sc.nextLine();

                        System.out.print("Number of deposits: ");
                        int n = Integer.parseInt(sc.nextLine());

                        ArrayList<Runnable> tasks = new ArrayList<>();

                        for (int i = 0; i < n; i++) {
                            System.out.print("Deposit amount "
                                    + (i + 1) + ": ");
                            double value =
                                    Double.parseDouble(sc.nextLine());

                            final String accountId = id;
                            final double depositValue = value;

                            tasks.add(() ->
                                    service.deposit(accountId, depositValue));
                        }

                        processor.processBatch(service, tasks);
                        System.out.println("Batch processing finished.");
                        break;

                    case 8:
                        service.printReport();
                        break;

                    case 9:
                        service.saveData();
                        System.out.println("Data saved. Goodbye!");
                        sc.close();
                        return;

                    default:
                        System.out.println("Invalid menu choice.");
                }

            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
