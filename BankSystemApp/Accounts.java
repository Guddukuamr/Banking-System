package BankSystemApp;

import java.sql.*;
import java.util.Scanner;

public class Accounts {
    private Connection connection;
    private Scanner sc;

    public Accounts(Connection connection, Scanner sc) {
        this.connection = connection;
        this.sc = sc;
    }

    public long openAccount(String email) {
        if (!accountExists(email)) {
            String openAccountQuery = "INSERT INTO accounts(account_no, full_name, email, balance, securty_pin) VALUES (?,?,?,?,?)";
            sc.nextLine();
            System.out.println("Enter Full name: ");
            String fullName = sc.nextLine();
            System.out.println("Enter Amount: ");
            double balance = sc.nextDouble();
            sc.nextLine(); // consume newline left-over
            System.out.print("Enter security pin: ");
            String securty_pin = sc.nextLine();

            try {
                long account_no = generateAccountNumber();
                PreparedStatement preparedStatement = connection.prepareStatement(openAccountQuery);
                preparedStatement.setLong(1, account_no);
                preparedStatement.setString(2, fullName);
                preparedStatement.setString(3, email);
                preparedStatement.setDouble(4, balance);
                preparedStatement.setString(5, securty_pin);

                int rowAffected = preparedStatement.executeUpdate();
                if (rowAffected > 0) {
                    return account_no;
                } else {
                    throw new RuntimeException("Account creation failed!!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Account creation failed!!");
            }
        }
        throw new RuntimeException("Account already exists");
    }

    public long getAccountNo(String email) {
        String query = "SELECT account_no from accounts WHERE email = ? ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong("account_no");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Account no does not exist!");
    }

    private long generateAccountNumber() {
        String query = "SELECT account_no FROM accounts ORDER BY account_no DESC LIMIT 1";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                long lastAccountNo = resultSet.getLong("account_no");
                return lastAccountNo + 1;
            } else {
                return 10000100;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 10000100;
        }
    }

    public boolean accountExists(String email) {
        String query = "SELECT account_no FROM accounts WHERE email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}