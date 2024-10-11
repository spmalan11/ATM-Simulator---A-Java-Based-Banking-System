package atm.utils;

import atm.account.Account;
import java.sql.*;

public class DatabaseUtils {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/atm";
    private static final String USER = "root";
    private static final String PASS = "11042002";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public static void initializeDatabase() {
        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement()) {

            String createAccountsTable = "CREATE TABLE IF NOT EXISTS accounts (" +
                    "account_number INTEGER PRIMARY KEY AUTO_INCREMENT, " + // AUTO_INCREMENT added for account_number
                    "account_name TEXT NOT NULL, " +
                    "pin INTEGER NOT NULL, " +
                    "balance REAL NOT NULL)";
            stmt.execute(createAccountsTable);

            String createTransactionsTable = "CREATE TABLE IF NOT EXISTS transactions (" +
                    "id INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                    "account_name TEXT NOT NULL, " +
                    "transaction_type TEXT NOT NULL, " +
                    "amount REAL NOT NULL, " +
                    "balance REAL NOT NULL, " +
                    "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)";
            stmt.execute(createTransactionsTable);
        } catch (SQLException e) {
            System.err.println("Error during database initialization: " + e.getMessage());
        }
    }

    public static Account getAccount(int accountNumber) {
        String query = "SELECT * FROM accounts WHERE account_number = ?";
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("account_name");
                int pin = rs.getInt("pin");
                int balance = rs.getInt("balance");
                return new Account(name, pin, balance);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving account: " + e.getMessage());
        }
        return null;
    }

    public static boolean logTransactionInDB(String name, String type, int amount, int balance) {
        String insertSQL = "INSERT INTO transactions (account_name, transaction_type, amount, balance) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {

            pstmt.setString(1, name);
            pstmt.setString(2, type);
            pstmt.setDouble(3, amount);
            pstmt.setDouble(4, balance);
            pstmt.executeUpdate();
            return true; // Indicate success
        } catch (SQLException e) {
            System.err.println("Error logging transaction: " + e.getMessage());
            return false; // Indicate failure
        }
    }

    public static boolean updateBalanceInDB(String name, int balance) {
        String updateSQL = "UPDATE accounts SET balance = ? WHERE account_name = ?";
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {

            pstmt.setInt(1, balance);
            pstmt.setString(2, name);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Return true if update was successful
        } catch (SQLException e) {
            System.err.println("Error updating balance: " + e.getMessage());
            return false; // Indicate failure
        }
    }

    public static boolean updatePinInDB(String name, int newPin) {
        String updateSQL = "UPDATE accounts SET pin = ? WHERE account_name = ?";
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {

            pstmt.setInt(1, newPin);
            pstmt.setString(2, name);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Return true if update was successful
        } catch (SQLException e) {
            System.err.println("Error updating PIN: " + e.getMessage());
            return false; // Indicate failure
        }
    }
}
