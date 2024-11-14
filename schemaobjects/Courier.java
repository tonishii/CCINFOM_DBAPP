package schemaobjects;

import java.util.Scanner;

public class Courier implements Account {
    private long     courier_id;
    private String   courier_name;
    private String   courier_email_address;
    private String   courier_address;
    private boolean  courier_verified_status;

//    public Courier(long courier_id, String courier_name, String courier_email_address, 
//            String courier_address, boolean courier_verified_status) {
//        this.courier_id = courier_id;
//        this.courier_name = courier_name;
//        this.courier_email_address = courier_email_address;
//        this.courier_address = courier_address;
//        this.courier_verified_status = courier_verified_status;
//    }
    
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
    
    public void setName(String courier_name) {
        this.courier_name = courier_name;
    }
    
    public void setEmailAddress(String courier_email_address) {
        this.courier_email_address = courier_email_address;
    }
    
    public void setAddress(String courier_address) {
        this.courier_address = courier_address;
    }
    
    public void updateStatus() {
        // checking fields
        this.courier_verified_status = true;
    }
    
    public long getID() {
        return this.courier_id;
    }
    
    public String getName() {
        return this.courier_name;
    }
    
    public String getEmailAddress() {
        return this.courier_email_address;
    }
    
    public String getAddress() {
        return this.courier_address;
    }
    
    public boolean getStatus() {
        return this.courier_verified_status;
    }
}
