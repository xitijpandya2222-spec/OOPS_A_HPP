import java.util.Scanner;

class MyDivideByZeroException extends Exception {
    MyDivideByZeroException(String message) {
        super(message);
    }
}

public class GuardedCalculator {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        boolean success = false;

        while (!success) {
            try {
                System.out.print("Enter first number: ");
                double num1 = sc.nextDouble();

                System.out.print("Enter second number: ");
                double num2 = sc.nextDouble();

                System.out.print("Enter operator (+, -, *, /): ");
                char op = sc.next().charAt(0);

                double result = 0;

                if (op == '+') {
                    result = num1 + num2;
                } 
                else if (op == '-') {
                    result = num1 - num2;
                } 
                else if (op == '*') {
                    result = num1 * num2;
                } 
                else if (op == '/') {
                    if (num2 == 0) {
                        throw new MyDivideByZeroException(
                            "Cannot divide by zero."
                        );
                    }
                    result = num1 / num2;
                } 
                else {
                    throw new Exception("Invalid operator.");
                }

                System.out.println("Result = " + result);
                success = true;
            }

            catch (MyDivideByZeroException e) {
                System.out.println(e.getMessage());
            }

            catch (Exception e) {
                System.out.println("Invalid input.");
                sc.nextLine();
            }

            finally {
                System.out.println("Attempt logged.");
                System.out.println();
            }
        }

        System.out.println("Calculation completed successfully.");

        sc.close();
    }
}