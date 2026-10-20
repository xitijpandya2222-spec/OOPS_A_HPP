package exception;

public class Account {

    String name;
    long balance;

    public Account(String name, long balance) {
        this.name = name;
        this.balance = balance;
    }

    public void withdraw(long amount)
            throws InsufficientFundsException, InvalidAmountException {

        if (amount <= 0)
            throw new InvalidAmountException("Amount must be greater than 0.");

        if (amount > balance)
            throw new InsufficientFundsException(amount - balance);

        balance -= amount;
        System.out.println("Withdrawal successful.");
    }

    public void deposit(long amount)
            throws InvalidAmountException {

        if (amount <= 0)
            throw new InvalidAmountException("Amount must be greater than 0.");

        balance += amount;
        System.out.println("Deposit successful.");
    }

    public void transfer(Account to, long amount)
            throws BankException {

        try {
            withdraw(amount);
            to.deposit(amount);

            System.out.println("Transfer successful.");

        } catch (BankException e) {

            System.out.println("Transfer failed: " + e.getMessage());

            throw e;

        } finally {

            System.out.println("Transfer operation completed.");
        }
    }

    public void showBalance() {
        System.out.println(name + " Balance = " + balance);
    }
}