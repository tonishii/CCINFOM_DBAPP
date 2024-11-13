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
    public int signUp() {
        return 0;
    }

    @Override
    public int displayView() {
        return 0;
    }
}
