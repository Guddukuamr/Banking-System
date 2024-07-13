package BankSystemApp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    private Connection connection;
    private Scanner sc;

    public User(Connection connection, Scanner sc) {
        this.connection = connection;
        this.sc = sc;
    }

    public void register(){
        sc.nextLine();
        System.out.println("Full Name: ");
        String full_name = sc.nextLine();
        System.out.println("Email: ");
        String email = sc.nextLine();
        System.out.println("Password: ");
        String password = sc.nextLine();

        if(user_exit(email)){
            System.out.println("User Already Exists for this email Address!! ");
            return;
        }

        String register_query = "INSERT INTO users(full_name, email, password) VALUES (?, ?, ?)";
        try {
            PreparedStatement preparedStatement =  connection.prepareStatement(register_query);

            preparedStatement.setString(1, full_name);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,password);
            int rowAffected = preparedStatement.executeUpdate();
            if (rowAffected > 0){
                System.out.println("Registration successful");
            }else {
                System.out.println("Registration failed!!");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }

    public String login(){
        sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Password: ");
        String password =  sc.nextLine();
        String login_query = "SELECT * FROM users WHERE email = ? AND password = ?";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(login_query);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                return email;
            }else {
                return null;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean user_exit(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
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
            System.out.println(e.getMessage());
        }
        return false;
    }
}