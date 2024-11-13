package schemaobjects;
import java.sql.Date;
import java.util.Scanner;

public class User implements Account {
    private long    user_id;
    private String  user_name;
    private String  user_firstname;
    private String  user_lastname;

    private String  user_address;
    private String  user_phone_number;
    private Date    user_creation_date;
    private boolean user_verified_status;

    @Override
    public void signUp(Scanner scn) {
        // Auto generate user ID

        System.out.print("Enter user account name: ");
        user_name  = scn.nextLine();

        System.out.print("Enter user first name: ");
        user_firstname = scn.nextLine();

        System.out.print("Enter user last  name: ");
        user_lastname = scn.nextLine();

        System.out.print("Enter user address:  ");
        user_address = scn.nextLine();

        System.out.print("Enter phone number:  ");
        user_phone_number = scn.nextLine();

        // Auto generate account creation date
        user_verified_status = false;
    }

    @Override
    public void displayView(Scanner scn) {
        while (true) {
            System.out.print(
            "[1] Shopping (Anthony)\n" +
            "[2] View Shopping Cart (Jericho)\n" +
            "[3] Receive Order\n" +
            "[4] Request Return/Refund (Eara)\n" +
            "[5] Purchase History\n" +
            "[6] Rate a Product\n" +
            "[7] Edit Account\n" +
            "Select option: ");

            switch (scn.nextLine().trim()) {
                case "1":
                    break;
                case "2":
                    break;
                case "3":
                    break;
                case "4":
                    break;
                default:
                    break;
            }
        }
    }
}
