
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

        outerLoop:
        while(true) {
            System.out.print(
            "[1] Login\n" +
            "[2] Sign up\n" +
            "Select option: ");

            switch (scn.nextLine().trim()) {
                case "1": isLogin = true;
                case "2":
                    break outerLoop;
                default:
                System.out.println("Error: Enter valid option.");
            }
        }

        Account account = selectAccountType(scn);

        if (isLogin == true) {
            System.out.print("Enter username: ");
            String id = scn.nextLine();

            // account =            GET THE ACCOUNT FROM THE DB USING ID
        } else {
            // Create a new account based on the account
            account.signUp(scn);
        }

        account.displayView(scn);
        System.out.println("Exiting...");
        scn.close();
    }
}