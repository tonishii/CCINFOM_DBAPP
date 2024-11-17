package schemaobjects;

import java.sql.Connection;
import java.util.Scanner;

public interface Account {
    public boolean login(Scanner scn, Connection conn);
    public void displayView(Scanner scn, Connection conn);
    public void signUp(Scanner scn, Connection conn);
    public void updateAccount(Connection conn);
}
