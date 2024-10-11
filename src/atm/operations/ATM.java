package atm.operations;

import atm.account.Account;
import atm.utils.DatabaseUtils;
import atm.utils.InputValidator;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class ATM {
    private static Connection connection;

    public static void main(String[] args) {
        // Initialize the database
        DatabaseUtils.initializeDatabase();
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to the ATM System!");

        while (true) {
            System.out.println("\nEnter your Account Number:");
            int accountNumber = InputValidator.getValidIntegerInput(sc);

            // Retrieve account from the database
            Account userAccount = DatabaseUtils.getAccount(accountNumber);
            if (userAccount != null) {
                System.out.println("Enter your PIN:");
                int enteredPin = InputValidator.getValidIntegerInput(sc);

                if (userAccount.verifyPin(enteredPin)) {
                    System.out.println("Welcome, " + userAccount.getName() + "!");
                    if (!handleUserOptions(sc, userAccount)) {
                        break;
                    }
                } else {
                    System.out.println("Incorrect PIN. Access denied.");
                }
            } else {
                System.out.println("Account not found.");
            }
        }
        sc.close();
    }

    private static boolean handleUserOptions(Scanner sc, Account userAccount) {
        while (true) {
            displayMenu();
            int option = InputValidator.getValidIntegerInput(sc);

            switch (option) {
                case 1:
                    userAccount.checkBalance();
                    break;
                case 2:
                    System.out.println("Enter the amount to deposit:");
                    int depositAmount = InputValidator.getValidPositiveIntegerInput(sc);
                    userAccount.deposit(depositAmount);
                    break;
                case 3:
                    System.out.println("Enter the amount to withdraw:");
                    int withdrawAmount = InputValidator.getValidPositiveIntegerInput(sc);
                    userAccount.withdraw(withdrawAmount);
                    break;
                case 4:
                    userAccount.printReceipt();
                    break;
                case 5:
                    System.out.println("Enter current PIN:");
                    int currentPin = InputValidator.getValidIntegerInput(sc);
                    System.out.println("Enter new PIN:");
                    int newPin = InputValidator.getValidIntegerInput(sc);
                    userAccount.changePin(currentPin, newPin);
                    break;
                case 6:
                    System.out.println("Exiting the ATM. Thank you!");
                    return false; // Exit the loop and program
                default:
                    System.out.println("Invalid option. Try again.");
                    break;
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\nChoose an option:");
        System.out.println("1. Check your balance");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Print receipt");
        System.out.println("5. Change PIN");
        System.out.println("6. Exit ");
    }
}
