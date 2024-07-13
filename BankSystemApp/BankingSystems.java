package BankSystemApp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class BankingSystems {
    private static final String url = "jdbc:mysql://localhost:3306/banking_system";
    private static final String userName = "root";
    private static final String password = "Guddu@8102";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            Connection connection = DriverManager.getConnection(url, userName, password);
            Scanner sc = new Scanner(System.in);
            User user = new User(connection, sc);
            Accounts accounts = new Accounts(connection, sc);
            AccountsManager accountsmanger = new AccountsManager(connection, sc);

            String email;
            Long account_no;

            while (true) {
                System.out.println("**** WELCOME TO BANKING SYSTEM ****");
                System.out.println();
                System.out.println("1. Register ");
                System.out.println("2. Login ");
                System.out.println("3. Exit");
                System.out.println("Enter Your Choice");
                int choise1 = sc.nextInt();
                switch (choise1) {
                    case 1:
                        user.register();
                        break;
                    case 2:
                        email = user.login();
                        if (email != null) {
                            System.out.println();
                            System.out.println("User Login Successfuly");
                            if (!accounts.accountExists(email)) {
                                System.out.println();
                                System.out.println("1. Open New Bank Account ");
                                System.out.println("2. Exit");
                                if (sc.nextInt() == 1) {
                                    account_no = accounts.openAccount(email);
                                    System.out.println("Account Created Successfully");
                                    System.out.println("Your Account Number is " + account_no);
                                } else {
                                    break;
                                }
                            } else {
                                account_no = accounts.getAccountNo(email);
                            }
                            int choise2 = 0;
                            while (choise2 != 5) {
                                System.out.println();
                                System.out.println("1. Debit Money ");
                                System.out.println("2. Credit Money ");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance ");
                                System.out.println("5. Log out");
                                System.out.println("Enter your Choice ");
                                choise2 = sc.nextInt();
                                switch (choise2) {
                                    case 1:
                                        accountsmanger.debit_money(account_no);
                                        break;
                                    case 2:
                                        accountsmanger.credit_money(account_no);
                                        break;
                                    case 3:
                                        accountsmanger.transfer_money(account_no);
                                        break;
                                    case 4:
                                        accountsmanger.get_balance(account_no);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Invalid Choice");
                                }
                            }

                        } else {
                            System.out.println("Incorrect Email or Password ");
                        }
                        break;
                    case 3:
                        System.out.println("THANK YOU FOR USING BANK SYSTEM !!");
                        System.out.println("Exiting System");
                        return;
                    default:
                        System.out.println("Enter valid Choice ");
                        break;
                }

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}