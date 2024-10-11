package atm.utils; // Ensure the package structure matches the directory structure

import java.util.Scanner;

public class InputValidator {

    // Method to get a valid integer input from the user
    public static int getValidIntegerInput(Scanner sc) {
        while (true) {
            try {
                // Read input from scanner and parse to integer
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                // Handle invalid integer input
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    // Method to get a valid positive integer input from the user
    public static int getValidPositiveIntegerInput(Scanner sc) {
        while (true) {
            int input = getValidIntegerInput(sc);
            if (input > 0) {
                return input; // Return if positive
            } else {
                System.out.println("Please enter a positive integer.");
            }
        }
    }
}
