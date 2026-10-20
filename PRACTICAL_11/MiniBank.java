
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Scanner;

// Parent Account class
class Account implements Serializable {
    private static final long serialVersionUID = 1L;

    int accountNumber;
    String customerName;
    long balance;

    Account(int accountNumber, String customerName, long balance) {
        this.accountNumber = accountNumber;
        this.customerName = customerName;
        this.balance = balance;
    }

    void deposit(long amount) {
        balance = balance + amount;
    }

    void withdraw(long amount) {
        if (balance >= amount) {
            balance = balance - amount;
        }
    }

    public String toString() {
        return "Account No: " + accountNumber
                + ", Customer: " + customerName
                + ", Balance: " + balance;
    }
}

// Subclass of Account
class SavingsAccount extends Account {
    private static final long serialVersionUID = 1L;

    SavingsAccount(int accountNumber, String name, long balance) {
        super(accountNumber, name, balance);
    }
}

// Customer class
class Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    String name;

    Customer(String name) {
        this.name = name;
    }
}

// Saves and loads accounts
class StatePersister {

    public static void save(Account[] accounts, Path file)
            throws IOException {

        try (ObjectOutputStream out = new ObjectOutputStream(
                Files.newOutputStream(file))) {
            out.writeObject(accounts);
        }

        System.out.println("Accounts saved successfully.");
    }

    public static Account[] load(Path file)
            throws IOException, ClassNotFoundException {

        try (ObjectInputStream in = new ObjectInputStream(
                Files.newInputStream(file))) {
            return (Account[]) in.readObject();
        }
    }
}

// Appends transactions to a log file
class TransactionLog {

    public static void append(Path file, String line)
            throws IOException {

        Files.write(file,
                (line + System.lineSeparator()).getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
    }
}

// Generates report from log files
class ReportGenerator {

    public static void generate(Path logFolder, Path reportFile)
            throws IOException {

        long totalDeposits = 0;
        long totalWithdrawals = 0;

        StringBuilder report = new StringBuilder();

        report.append("MINIBANK TRANSACTION REPORT\n");
        report.append("===========================\n");

        try (DirectoryStream<Path> files =
                Files.newDirectoryStream(logFolder)) {

            for (Path file : files) {
                if (!Files.isRegularFile(file)) {
                    continue;
                }

                report.append("\nFile: ")
                        .append(file.getFileName()).append("\n");

                // Read file details
                BasicFileAttributes attr =
                        Files.readAttributes(file,
                                BasicFileAttributes.class);

                report.append("Size: ")
                        .append(attr.size()).append(" bytes\n");

                report.append("Last Modified: ")
                        .append(attr.lastModifiedTime()).append("\n");

                // Read log file line by line
                try (BufferedReader reader =
                        Files.newBufferedReader(file)) {

                    String line;

                    while ((line = reader.readLine()) != null) {
                        report.append(line).append("\n");

                        String[] parts = line.split(" ");

                        if (parts.length >= 2) {
                            long amount;

                            try {
                                amount = Long.parseLong(parts[1]);
                            } catch (NumberFormatException e) {
                                continue;
                            }

                            if (parts[0].equalsIgnoreCase("DEPOSIT")) {
                                totalDeposits += amount;
                            } else if (parts[0].equalsIgnoreCase("WITHDRAW")) {
                                totalWithdrawals += amount;
                            }
                        }
                    }
                }
            }
        }

        report.append("\nTotal Deposits: ").append(totalDeposits);
        report.append("\nTotal Withdrawals: ").append(totalWithdrawals);

        // Write report to file
        Files.write(reportFile, report.toString().getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);

        System.out.println("\n" + report);
        System.out.println("\nReport saved successfully.");
    }
}

// Main class
public class MiniBank {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            System.out.print("Enter number of accounts: ");
            int n = sc.nextInt();
            sc.nextLine();

            Account[] accounts = new Account[n];

            for (int i = 0; i < n; i++) {
                System.out.println("\nAccount " + (i + 1));

                System.out.print("Enter account number: ");
                int number = sc.nextInt();
                sc.nextLine();

                System.out.print("Enter customer name: ");
                String name = sc.nextLine();

                System.out.print("Enter balance: ");
                long balance = sc.nextLong();
                sc.nextLine();

                accounts[i] = new SavingsAccount(number, name, balance);
            }

            Path stateFile = Paths.get("accounts.dat");
            Path logFolder = Paths.get("logs");
            Path reportFile = Paths.get("report.txt");

            // Create logs folder if needed
            Files.createDirectories(logFolder);

            // Save and reload accounts
            StatePersister.save(accounts, stateFile);

            Account[] loadedAccounts = StatePersister.load(stateFile);

            System.out.println("\nAccounts after loading:");

            for (Account account : loadedAccounts) {
                System.out.println(account);
            }

            // Add sample transactions to the log
            Path logFile = logFolder.resolve("transactions.txt");

            System.out.println("\nEnter number of transactions: ");
            int count = sc.nextInt();

            for (int i = 0; i < count; i++) {
                System.out.print("Enter type (DEPOSIT/WITHDRAW): ");
                String type = sc.next();

                System.out.print("Enter amount: ");
                long amount = sc.nextLong();

                if (amount < 0 ||
                        !(type.equalsIgnoreCase("DEPOSIT")
                        || type.equalsIgnoreCase("WITHDRAW"))) {
                    System.out.println("Invalid transaction. Try again.");
                    i--;
                    continue;
                }

                TransactionLog.append(logFile,
                        type.toUpperCase() + " " + amount);
            }

            // Generate and print report
            ReportGenerator.generate(logFolder, reportFile);

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }

        sc.close();
    }
}
