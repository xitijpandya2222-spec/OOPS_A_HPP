
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.*;

// Id annotation
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Id {
}

// Account class
class Account {
    @Id
    private String accountNumber;

    String customerName;
    double balance;

    Account(String accountNumber, String customerName, double balance) {
        this.accountNumber = accountNumber;
        this.customerName = customerName;
        this.balance = balance;
    }

    public String toString() {
        return "Account Number: " + accountNumber
                + ", Name: " + customerName
                + ", Balance: " + balance;
    }
}

// Generic Repository class
class Repository<T> {
    HashMap<String, T> data = new HashMap<>();

    // Save one object using its @Id field
    T save(T entity) {
        try {
            Field[] fields = entity.getClass().getDeclaredFields();

            for (Field field : fields) {
                if (field.isAnnotationPresent(Id.class)) {
                    field.setAccessible(true);

                    String id = field.get(entity).toString();
                    data.put(id, entity);

                    return entity;
                }
            }

            throw new RuntimeException("No @Id field found");

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    // Find object by id
    T findById(String id) {
        return data.get(id);
    }

    // Return all objects
    ArrayList<T> findAll() {
        return new ArrayList<>(data.values());
    }

    // Delete object by id
    boolean delete(String id) {
        return data.remove(id) != null;
    }

    // Save multiple objects
    @SafeVarargs
    final void saveAll(T... items) {
        for (T item : items) {
            save(item);
        }
    }
}

// Main class
public class MiniBank {

    // Bounded generic method
    static <N extends Number> double average(N[] values) {
        double sum = 0;

        for (N value : values) {
            sum += value.doubleValue();
        }

        return sum / values.length;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Repository<Account> repository = new Repository<>();

        System.out.print("Enter number of accounts: ");
        int n = sc.nextInt();
        sc.nextLine();

        // Create and save accounts
        for (int i = 0; i < n; i++) {
            System.out.println("\nEnter account " + (i + 1) + " details:");

            System.out.print("Account number: ");
            String number = sc.nextLine();

            System.out.print("Customer name: ");
            String name = sc.nextLine();

            System.out.print("Balance: ");
            double balance = sc.nextDouble();
            sc.nextLine();

            Account account = new Account(number, name, balance);
            repository.save(account);
        }

        // Find account by id
        System.out.print("\nEnter account number to search: ");
        String id = sc.nextLine();

        Account found = repository.findById(id);

        if (found != null) {
            System.out.println("Account found: " + found);
        } else {
            System.out.println("Account not found.");
        }

        // Display all accounts
        System.out.println("\nAll Accounts:");

        for (Account account : repository.findAll()) {
            System.out.println(account);
        }

        // Demonstrate saveAll()
        System.out.println("\nTesting saveAll() with sample accounts:");

        Account a1 = new Account("A101", "Rahul", 1000);
        Account a2 = new Account("A102", "Priya", 2000);

        repository.saveAll(a1, a2);

        System.out.println("Total accounts after saveAll: "
                + repository.findAll().size());

        // Demonstrate average()
        Integer[] numbers = {10, 20, 30};
        System.out.println("Average: " + average(numbers));

        sc.close();
    }
}

