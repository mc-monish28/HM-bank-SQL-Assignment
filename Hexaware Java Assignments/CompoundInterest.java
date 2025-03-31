import java.util.Scanner;

public class CompoundInterest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of customers: ");
        int numCustomers = scanner.nextInt();

        for (int i = 1; i <= numCustomers; i++) {
            System.out.println("Customer " + i + ":");
            System.out.print("Enter initial balance: ");
            double initialBalance = scanner.nextDouble();

            System.out.print("Enter annual interest rate (in %): ");
            double annualRate = scanner.nextDouble();

            System.out.print("Enter number of years: ");
            int years = scanner.nextInt();

            double futureBalance = initialBalance * Math.pow((1 + annualRate / 100), years);
            System.out.println("Future balance after " + years + " years: $" + futureBalance);
        }

        scanner.close();
    }
}
