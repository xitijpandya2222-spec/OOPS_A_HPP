package exception;

import java.util.Scanner;

public class MiniBank {

    static Account findAccount(Account a1, Account a2, String name)
            throws AccountNotFoundException {

        if (a1.name.equalsIgnoreCase(name))
            return a1;

        if (a2.name.equalsIgnoreCase(name))
            return a2;

        throw new AccountNotFoundException("Account not found.");
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Account 1 name: ");
        String name1 = sc.nextLine();

        System.out.print("Enter Account 1 balance: ");
        long balance1 = sc.nextLong();

        sc.nextLine();

        System.out.print("Enter Account 2 name: ");
        String name2 = sc.nextLine();

        System.out.print("Enter Account 2 balance: ");
        long balance2 = sc.nextLong();

        Account a1 = new Account(name1, balance1);
        Account a2 = new Account(name2, balance2);

        try {

            System.out.print("\nEnter withdrawal amount: ");
            long amount = sc.nextLong();

            a1.withdraw(amount);

        } catch (BankException e) {

            System.out.println("Error: " + e.getMessage());

        } finally {

            System.out.println("Withdrawal operation finished.");
        }

        try {

            System.out.print("\nEnter deposit amount: ");
            long amount = sc.nextLong();

            a2.deposit(amount);

        } catch (BankException e) {

            System.out.println("Error: " + e.getMessage());

        } finally {

            System.out.println("Deposit operation finished.");
        }

        try {

            System.out.print("\nEnter transfer amount from Account 1 to Account 2: ");
            long amount = sc.nextLong();

            a1.transfer(a2, amount);

        } catch (BankException e) {

            System.out.println("Transfer error: " + e.getMessage());

        } finally {

            System.out.println("Transfer process finished.");
        }

        try {

            System.out.print("\nEnter account name to search: ");
            sc.nextLine();

            String name = sc.nextLine();

            Account account = findAccount(a1, a2, name);

            account.showBalance();

        } catch (BankException e) {

            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\nFinal Balances:");

        a1.showBalance();
        a2.showBalance();

        System.out.println("\nTry-with-resources example:");

        try (BankResource r = new BankResource()) {

            r.use();

        }

        sc.close();
    }
}