package BankSystemApp;

import java.sql.*;
import java.util.Scanner;

public class AccountsManager {
    private Connection connection;
    private Scanner sc;

    public AccountsManager(Connection connection, Scanner sc) {
        this.connection = connection;
        this.sc = sc;
    }

    public void credit_money(Long account_no) throws SQLException {
        sc.nextLine();
        System.out.print("Enter Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine(); // consume newline left-over
        System.out.print("Enter Security Pin: ");
        String securty_pin = sc.nextLine();

        try {
            connection.setAutoCommit(false);
            if (account_no != 0) {
                String query = "SELECT * FROM accounts WHERE account_no = ? AND securty_pin = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setLong(1, account_no);
                preparedStatement.setString(2, securty_pin);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    String credit_query = "UPDATE accounts SET balance = balance + ? WHERE account_no = ? ";
                    PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);
                    preparedStatement1.setDouble(1, amount);
                    preparedStatement1.setLong(2, account_no);
                    int rowAffected = preparedStatement1.executeUpdate();
                    if (rowAffected > 0) {
                        System.out.println("Rs " + amount + " credited successfully");
                        connection.commit();
                        connection.setAutoCommit(true);
                        return;
                    } else {
                        System.out.println("Transaction failed!!");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }

                } else {
                    System.out.println("Invalid Security Pin");
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.setAutoCommit(true);

    }

    public void debit_money(Long account_no) throws SQLException {
        sc.nextLine();
        System.out.print("Enter Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine(); // consume newline left-over
        System.out.print("Enter Security Pin: ");
        String securty_pin = sc.nextLine();

        try {
            connection.setAutoCommit(false);
            if (account_no != 0) {
                String query = "SELECT * FROM accounts WHERE account_no = ? AND securty_pin = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setLong(1, account_no);
                preparedStatement.setString(2, securty_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    double current_balance = resultSet.getDouble("balance");
                    if (amount <= current_balance) {
                        String debit_query = "UPDATE accounts SET balance = balance - ? WHERE account_no = ?";
                        PreparedStatement preparedStatement1 = connection.prepareStatement(debit_query);
                        preparedStatement1.setDouble(1, amount);
                        preparedStatement1.setLong(2, account_no);
                        int rowAffected = preparedStatement1.executeUpdate();
                        if (rowAffected > 0) {
                            System.out.println("Rs " + amount + " debited successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        } else
                            System.out.println("Transaction failed !!!");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    } else {
                        System.out.println("Insufficient balance");
                    }
                } else {
                    System.out.println("Invalid Pin");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }

    public void transfer_money(Long sender_account_no) throws SQLException {
        sc.nextLine();
        System.out.print("Enter Receiver Account No: ");
        long receiver_account_no = sc.nextLong();
        sc.nextLine(); // consume newline left-over
        System.out.print("Enter Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine(); // consume newline left-over
        System.out.println("Enter Security Pin: ");
        String  securty_pin  = sc.nextLine();
        try {
            connection.setAutoCommit(false);
            if (sender_account_no != 0 && receiver_account_no != 0) {
                String query = "SELECT * FROM accounts WHERE account_no  = ? AND  securty_pin  = ? ";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setLong(1, sender_account_no);
                preparedStatement.setString(2,  securty_pin );
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    double current_balance = resultSet.getDouble("balance");
                    if (amount <= current_balance) {
                        String debit_money = "UPDATE accounts SET balance = balance - ? WHERE account_no = ?";
                        String credit_money = "UPDATE accounts SET balance = balance + ? WHERE account_no = ?";
                        PreparedStatement debitPreparedStatement = connection.prepareStatement(debit_money);
                        PreparedStatement creditPreparedStatement = connection.prepareStatement(credit_money);
                        creditPreparedStatement.setDouble(1, amount);
                        creditPreparedStatement.setLong(2, receiver_account_no);

                        debitPreparedStatement.setDouble(1, amount);
                        debitPreparedStatement.setLong(2, sender_account_no);

                        int rowAffected1 = debitPreparedStatement.executeUpdate();
                        int rowAffected2 = creditPreparedStatement.executeUpdate();

                        if (rowAffected1 > 0 && rowAffected2 > 0) {
                            System.out.println("Transaction Successfuly");
                            System.out.println("Rs " + amount + " Transaction Successfuly");
                            connection.commit();
                            connection.setAutoCommit(true);
                        } else {
                            System.out.println("Transaction failed!!!");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    } else {
                        System.out.println("Insufficient Balance");
                    }
                } else {
                    System.out.println("Invalid Security Pin");
                }
            } else {
                System.out.println("Invalid Account no");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.setAutoCommit(true);

    }

    public void get_balance(Long account_no) {
        sc.nextLine();
        System.out.println("Enter Security Pin");
        String securty_pin = sc.nextLine();
        try {
            String query = "SELECT balance FROM accounts WHERE account_no =? AND securty_pin =?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, account_no);
            preparedStatement.setString(2, securty_pin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                double balance = resultSet.getDouble("balance");
                System.out.println("Balance = " + balance);
            } else {
                System.out.println("Invalid Pin");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}