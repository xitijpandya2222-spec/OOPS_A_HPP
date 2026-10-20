
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// Account class
class Account implements Comparable<Account> {
    String accountNumber;
    String customerName;
    double balance;

    Account(String accountNumber, String customerName, double balance) {
        this.accountNumber = accountNumber;
        this.customerName = customerName;
        this.balance = balance;
    }

    // Compare accounts by account number
    public int compareTo(Account other) {
        return this.accountNumber.compareTo(other.accountNumber);
    }

    public String toString() {
        return "Account Number: " + accountNumber
                + ", Name: " + customerName
                + ", Balance: " + balance;
    }
}

// AccountStore class
class AccountStore {
    HashMap<String, Account> accounts = new HashMap<>();
    ArrayList<String> transactionHistory = new ArrayList<>();

    // Add account
    void add(Account account) {
        accounts.put(account.accountNumber, account);
        transactionHistory.add("Account added: " + account.accountNumber);
    }

    // Get account by account number
    Account get(String accountNumber) {
        return accounts.get(accountNumber);
    }

    // Get all accounts
    ArrayList<Account> getAll() {
        return new ArrayList<>(accounts.values());
    }

    // Sort accounts by balance
    ArrayList<Account> sortedByBalance() {
        ArrayList<Account> list = getAll();

        Collections.sort(list, new Comparator<Account>() {
            public int compare(Account a, Account b) {
                return Double.compare(a.balance, b.balance);
            }
        });

        return list;
    }

    // Print transaction history
    void showHistory() {
        System.out.println("\nTransaction History:");
        for (String transaction : transactionHistory) {
            System.out.println(transaction);
        }
    }
}

// TransactionProcessor with thread-safe map
class TransactionProcessor {
    ConcurrentHashMap<String, Account> sharedAccounts =
            new ConcurrentHashMap<>();

    void addAccount(Account account) {
        sharedAccounts.put(account.accountNumber, account);
    }

    Account getAccount(String accountNumber) {
        return sharedAccounts.get(accountNumber);
    }
}

// Main class
public class MiniBank {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        AccountStore store = new AccountStore();
        TransactionProcessor processor = new TransactionProcessor();

        System.out.print("Enter number of accounts: ");
        int n = sc.nextInt();
        sc.nextLine();

        // Add accounts
        for (int i = 1; i <= n; i++) {
            System.out.println("\nEnter details for account " + i);

            System.out.print("Enter account number: ");
            String number = sc.nextLine();

            System.out.print("Enter customer name: ");
            String name = sc.nextLine();

            System.out.print("Enter balance: ");
            double balance = sc.nextDouble();
            sc.nextLine();

            Account account = new Account(number, name, balance);

            store.add(account);
            processor.addAccount(account);
        }

        // Display accounts sorted by balance
        System.out.println("\nAccounts sorted by balance:");

        ArrayList<Account> sortedAccounts = store.sortedByBalance();

        for (Account account : sortedAccounts) {
            System.out.println(account);
        }

        // Search for an account
        System.out.print("\nEnter account number to search: ");
        String searchNumber = sc.nextLine();

        Account found = store.get(searchNumber);

        if (found != null) {
            System.out.println("Account found: " + found);
        } else {
            System.out.println("Account not found.");
        }

        // Show transaction history
        store.showHistory();

        sc.close();
    }
}
