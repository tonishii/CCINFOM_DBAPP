package schemaobjects;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.Scanner;

public interface Account {
    boolean login(int id, Connection conn);
    void displayView(Scanner scn, Connection conn);
    void signUp(Connection conn);
    void updateAccount(Connection conn);
    JPanel getPage();
    JPanel getSignUpPage();

    void initSignUpListeners(ActionListener signUpLtr);

    String toString();
}
