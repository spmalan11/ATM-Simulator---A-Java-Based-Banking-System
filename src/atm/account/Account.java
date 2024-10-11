package atm.account;

import atm.utils.DatabaseUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Account {
    private String name;
    private int pin;
    private int balance;
    private List<String> transactions;
    private SimpleDateFormat sdf;

    public Account(String name, int pin, int balance) {
        this.name = name;
        this.pin = pin;
        this.balance = balance;
        this.transactions = new ArrayList<>();
        this.sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    }

    public String getName() {
        return name;
    }

    public boolean verifyPin(int enteredPin) {
        return this.pin == enteredPin;
    }

    public void checkBalance() {
        System.out.println("Your current balance is: ₹" + balance);
    }

    public void deposit(int amount) {
        balance += amount;
        logTransaction("Deposit", amount);
        updateBalanceInDB();
        System.out.println("Successfully deposited ₹" + amount);
    }

    public void withdraw(int amount) {
        if (amount > balance) {
            System.out.println("Insufficient balance.");
        } else {
            balance -= amount;
            logTransaction("Withdrawal", amount);
            updateBalanceInDB();
            System.out.println("Successfully withdrew ₹" + amount);
        }
    }

    public void changePin(int currentPin, int newPin) {
        if (this.pin == currentPin) {
            this.pin = newPin;
            DatabaseUtils.updatePinInDB(name, newPin);
            System.out.println("PIN changed successfully.");
        } else {
            System.out.println("Incorrect current PIN.");
        }
    }


    public void printReceipt() {
        System.out.println("--- Receipt ---");
        System.out.println("Account Holder: " + name);
        System.out.println("Current Balance: ₹" + balance);
        for (String transaction : transactions) {
            System.out.println(transaction);
        }
        System.out.println("Thank you for using our ATM.");
    }

    private void logTransaction(String type, int amount) {
        String date = sdf.format(new Date());
        transactions.add(String.format("[%s] %s: ₹%-10d | Balance: ₹%-10d", date, type, amount, balance));
        DatabaseUtils.logTransactionInDB(name, type, amount, balance);
    }

    private void updateBalanceInDB() {
        DatabaseUtils.updateBalanceInDB(name, balance);
    }
}
