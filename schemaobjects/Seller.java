package schemaobjects;
import java.sql.Date;
import java.util.Scanner;

public class Seller implements Account {
    private long    seller_id;
    private String  seller_name;
    private String  seller_address;
    private String  seller_phone_number;
    private Date    seller_creation_date;
    private boolean seller_verified_status;

    @Override
    public void signUp(Scanner scn) {
        // Auto generate seller ID

        System.out.print("Enter seller name: ");
        seller_name  = scn.nextLine();

        System.out.print("Enter seller address:  ");
        seller_address = scn.nextLine();

        System.out.print("Enter phone number:  ");
        seller_phone_number = scn.nextLine();

        // Auto generate creation date

        seller_verified_status = false;
    }

    @Override
    public void displayView(Scanner scn) {
        while (true) {
            System.out.print(
            "[1] Add Product\n" +
            "[2] Edit Product\n" +
            "[3] Generate Report\n" +
            "[4] Edit Account\n" +
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
