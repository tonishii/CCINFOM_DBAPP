package model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public interface Account {
    boolean login(int id, Connection conn) throws SQLException;
    void displayView(Scanner scn, Connection conn);
    void updateAccount(Connection conn);

    String toString();
}
