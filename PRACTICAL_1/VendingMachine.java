//Practical 1_A_1

import java.util.Scanner;

enum Coin
{
    ONE, TWO, FIVE, TEN
}

public class VendingMachine
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);

        int price = 15;
        int total = 0;

        while (total < price)
        {
            System.out.print("Enter Coin (ONE, TWO, FIVE, TEN): ");
            Coin c = Coin.valueOf(sc.next().toUpperCase());

            int value = switch(c)
            {
                case ONE -> 1;
                case TWO -> 2;
                case FIVE -> 5;
                case TEN -> 10;
            };

            total = total + value;
            System.out.println("Total = " + total);
        }

        System.out.println("Paid. Change: " + (total - price));
    }
}