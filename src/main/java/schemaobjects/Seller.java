package schemaobjects;
import java.sql.*;

public class Seller implements Account {
    public long    seller_id;
    public String  seller_name;
    public String  seller_address;
    public String  seller_phone_number;
    public Date    seller_creation_date;
    public boolean seller_verified_status;

    @Override
    public int signUp() {
        try {
            String url= "jdbc:mysql://localhost:3306/mydb"; // table details
            String username = "root"; // MySQL credentials
            String password = ""; // put ur password here lol
            Connection conn;
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, password);
            
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO sellers (seller_id, seller_name, seller_address, seller_verified_status,seller_phone_number, seller_creation_date) VALUES(?, ?, ?, ?, ?, ?)");
            pstmt.setInt(1, 1);
            pstmt.setString(2, seller_name);
            pstmt.setString(3, seller_address);
            pstmt.setInt(4,1);
            pstmt.setString(5,seller_phone_number);
            pstmt.setString(6,"2004-2-2");
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
            return 1;
            
        } catch (Exception e){
            System.out.println(e.getMessage());
            return 0;
        }
    }

    @Override
    public int displayView() {
        return 0;
    }
}
