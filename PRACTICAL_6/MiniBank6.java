interface Transactable {
    void deposit(long amount);
    boolean withdraw(long amount);
}

interface InterestBearing {
    double interestRate();
    long getBalance();

    default double yearlyInterest() {
        return getBalance() * interestRate();
    }
}

@FunctionalInterface
interface WithdrawRule {
    boolean allow(Account account, long amount);
}

interface Premium {
}

class Account implements Transactable, InterestBearing, Premium {

    private String name;
    private long balance;

    Account(String name, long balance) {
        this.name = name;
        this.balance = balance;
    }

    public void deposit(long amount) {
        balance += amount;
    }

    public boolean withdraw(long amount) {
        if (amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public double interestRate() {
        return 0.05;
    }

    public long getBalance() {
        return balance;
    }

    public void display() {
        System.out.println("Name: " + name);
        System.out.println("Balance: " + balance);
    }
}

public class MiniBank6 {

    public static void main(String[] args) {

        Account account = new Account("Kshitij", 10000);

        System.out.println("MINI BANK");
        System.out.println("----------------");

        account.display();

        account.deposit(2000);
        System.out.println("After Deposit: " + account.getBalance());

        System.out.println("Withdraw: " +
                account.withdraw(3000));

        System.out.println("Balance: " +
                account.getBalance());

        System.out.println("----------------");

        
        WithdrawRule rule1 = new WithdrawRule() {

            public boolean allow(Account account, long amount) {
                return amount <= account.getBalance();
            }
        };

        System.out.println("Anonymous Class: " +
                rule1.allow(account, 5000));

        
        WithdrawRule rule2 =
                (acc, amount) -> amount <= acc.getBalance();

        System.out.println("Lambda: " +
                rule2.allow(account, 5000));

        System.out.println("----------------");

        System.out.println("Yearly Interest: " +
                account.yearlyInterest());
    }
}
