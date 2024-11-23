package Model;

import java.sql.Connection;
import java.sql.SQLException;

public interface Account {
    boolean login(int id, Connection conn) throws SQLException;
    void updateAccount(Connection conn) throws SQLException;
}