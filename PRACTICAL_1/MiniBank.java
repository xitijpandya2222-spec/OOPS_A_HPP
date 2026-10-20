import java.util.Scanner;

record BankInfo(String name, String branch) {}

enum MenuOption
{
    OPEN_ACCOUNT,
    DEPOSIT,
    WITHDRAW,
    TRANSFER,
    EXIT
}

public class MiniBank
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);

        BankInfo bank = new BankInfo("ABC Bank", "Ahmedabad");

        System.out.println("=================================");
        System.out.println("        MINI BANK");
        System.out.println("Bank Name : " + bank.name());
        System.out.println("Branch    : " + bank.branch());
        System.out.println("=================================");

        int choice;

        do
        {
            System.out.println("\n----- MENU -----");
            System.out.println("1. Open Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();

            switch (choice)
            {
                case 1 ->
                    System.out.println("Open Account - To be implemented in a later lab.");

                case 2 ->
                    System.out.println("Deposit - To be implemented in a later lab.");

                case 3 ->
                    System.out.println("Withdraw - To be implemented in a later lab.");

                case 4 ->
                    System.out.println("Transfer - To be implemented in a later lab.");

                case 5 ->
                    System.out.println("Thank you for using MiniBank!");

                default ->
                    System.out.println("Invalid Choice!");
            }

        } while (choice != 5);

        sc.close();
    }
}