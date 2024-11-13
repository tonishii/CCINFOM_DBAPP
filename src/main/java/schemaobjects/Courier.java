package schemaobjects;

import java.util.Scanner;

public class Courier implements Account {
    private long     courier_id;
    private String   courier_name;
    private String   courier_email_address;
    private String   courier_address;
    private boolean  courier_verified_status;

    @Override
    public int signUp() {
        return 0;
    }

    @Override
    public int displayView() {
        return 0;
    }
}
