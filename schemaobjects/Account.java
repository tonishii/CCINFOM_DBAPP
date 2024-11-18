package schemaobjects;

import java.sql.Connection;
import java.util.Scanner;

public interface Account {
    boolean login(int id, Connection conn);
    void displayView(Scanner scn, Connection conn);
    void updateAccount(Connection conn);

    String toString();
}
