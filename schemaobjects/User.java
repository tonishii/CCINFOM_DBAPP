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

//    public User(long user_id, String user_name, String user_firstname, String user_lastname, String user_address, 
//            String user_phone_number, Date user_creation_date, boolean user_verified_status) {
//        this.user_id = user_id;
//        this.user_name = user_name;
//        this.user_firstname = user_firstname;
//        this.user_lastname = user_lastname;
//        this.user_address = user_address;
//        this.user_phone_number = user_phone_number;
//        this.user_creation_date = user_creation_date;
//        this.user_verified_status = user_verified_status;
//    }
    
    @Override
    public void signUp(Scanner scn) {
        // Auto generate user ID
        user_id = 0;

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

        long ms = System.currentTimeMillis();
        user_creation_date = new Date(ms);
        
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
    
    public void setUsername(String user_name) {
        this.user_name = user_name;
    }
    
    public void setFirstName(String user_firstname) {
        this.user_firstname = user_firstname;
    }
    
    public void setLastName(String user_lastname) {
        this.user_lastname = user_lastname;
    }
    
    public void setAddress(String user_address) {
        this.user_address = user_address;
    }
    
    public void setPhoneNumber(String user_phone_number) {
        this.user_phone_number = user_phone_number;
    }
    
    public void updateStatus() {
        // checking fields
        this.user_verified_status = true;
    }
    
    public long getID() {
        return this.user_id;
    }
    
    public String getUsername() {
        return this.user_name;
    }
    
    public String getFirstName() {
        return this.user_firstname;
    }
    
    public String getLastName() {
        return this.user_lastname;
    }
    
    public String getAddress() {
        return this.user_address;
    }
    
    public String getPhoneNumber() {
        return this.user_phone_number;
    }
    
    public Date getCreationDate() {
        return this.user_creation_date;
    }
    
    public boolean getStatus() {
        return this.user_verified_status;
    }
}
