package schemaobjects;

import javax.swing.*;
import java.sql.Connection;
import java.util.Scanner;

public interface Account {
    public boolean login(int id, Connection conn);
    public void displayView(Scanner scn, Connection conn);
    public void signUp(Scanner scn, Connection conn);
    public void updateAccount(Connection conn);
    public JPanel displayPage();

    public String toString();
}
