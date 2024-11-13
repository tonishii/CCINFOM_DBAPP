package schemaobjects;

import java.util.Scanner;

public class Courier implements Account {
    private long     courier_id;
    private String   courier_name;
    private String   courier_email_address;
    private String   courier_address;
    private boolean  courier_verified_status;

    @Override
    public void signUp(Scanner scn) {
        // Auto generate courierID

        System.out.print("Enter courier name: ");
        courier_name = scn.nextLine();

        System.out.print("Enter email address:  ");
        courier_email_address = scn.nextLine();

        System.out.print("Enter courier address: ");
        courier_address = scn.nextLine();

        courier_verified_status = false;
    }

    @Override
    public void displayView(Scanner scn) {
        while (true) {
            System.out.print(
            "[1] Ongoing Orders\n" +
            "[2] Generate Activity Report\n" +
            "[3] Edit Account\n" +
            "Select option: ");

            switch (scn.nextLine().trim()) {
                case "1":
                    break;
                case "2":
                    break;
                case "3":
                    break;
                default:
                    break;
            }
        }
    }
}
