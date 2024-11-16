
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

import schemaobjects.Account;
import schemaobjects.Courier;
import schemaobjects.Seller;
import schemaobjects.User;

public class Driver {
    public static Account selectAccountType(Scanner scn) {
        while (true) {
            System.out.print(
            "[1] Seller\n" +
            "[2] Courier\n" +
            "[3] User\n" +
            "Select account type: ");

            switch (scn.nextLine().trim()) {
                case "1": return new Seller();
                case "2": return new Courier();
                case "3": return new User();
                default:
                System.out.println("Error: Enter valid account type.");
            }
        }
    }

    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        boolean isLogin = false;

        try {

            String url = "jdbc:mysql://localhost:3306/mydb";

            System.out.print("Enter your username: ");
            String username = scn.nextLine();

            System.out.print("Enter your password: ");
            String password = scn.nextLine();

            Connection conn;
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection to the db
            conn = DriverManager.getConnection(url, username, password);

            outerLoop:
            while (true) {
                System.out.print(
                "[1] Login\n" +
                "[2] Sign up\n" +
                "Select option: ");

                switch (scn.nextLine().trim()) {
                    case "1":
                        isLogin = true;
                    case "2":
                        break outerLoop;
                    default:
                        System.out.println("Error: Enter valid option.");
                }
            }

            Account account = selectAccountType(scn);

            if (isLogin) {
                while (true) {
                    if (account.login(scn, conn)) {
                        // successful login
                        break;
                    } else {
                        System.out.println("HELLO THERE");
                        // give option to go back
                        // just do this in gui im too tired for this shi
                    }
                }
            } else {
                // Create a new account based on the account
                account.signUp(scn, conn);
            }

            account.displayView(scn, conn);
            System.out.println("Exiting...");
            scn.close();
            conn.close();

        } catch (Exception e) {
            System.out.println("Error: ");
        }
    }
}