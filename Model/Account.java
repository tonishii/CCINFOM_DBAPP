package Model;

import java.sql.Connection;
import java.sql.SQLException;

// Account is a superclass representing an account (created by a person) that can be made in a database
public interface Account {

    // You can login and update an accounts information
    boolean login(int id, Connection conn) throws SQLException;
    void updateAccount(Connection conn) throws SQLException;
}