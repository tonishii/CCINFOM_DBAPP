package schemaobjects;
import java.sql.*;

public class User implements Account {
    public long    user_id;
    public String  user_name;
    public String  user_firstname;
    public String  user_lastname;

    public String  user_address;
    public String  user_phone_number;
    public Date    user_creation_date;
    public boolean user_verified_status;

    @Override
    public int signUp() {
        try {
            String url= "jdbc:mysql://localhost:3306/mydb"; // table details
            String username = "root"; // MySQL credentials
            String password = ""; // put ur password here lol
            int id=0;
            Connection conn;
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, password);
            
            PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(user_id) AS Id FROM mydb.users");
            ResultSet myRs = pstmt.executeQuery();
            while (myRs.next()) {
                id = myRs.getInt("Id");
            }
            if (id==0){
                pstmt = conn.prepareStatement("INSERT INTO users (user_id, user_name, user_phone_number, user_address, user_verified_status, user_creation_date, user_firstname, user_lastname) "
                                               + "VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
                pstmt.setInt(1, 1);
                pstmt.setString(2, user_name);
                pstmt.setString(3, user_phone_number);
                pstmt.setString(4, user_address);
                pstmt.setInt(5, 1);
                pstmt.setTimestamp(6, new java.sql.Timestamp(System.currentTimeMillis()));
                pstmt.setString(7, user_firstname);
                pstmt.setString(8, user_lastname);
                pstmt.executeUpdate();           
            }
            else{
                pstmt = conn.prepareStatement("INSERT INTO users (user_id, user_name, user_phone_number, user_address, user_verified_status, user_creation_date, user_firstname, user_lastname) "
                                               + "VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
                pstmt.setInt(1, id+1);
                pstmt.setString(2, user_name);
                pstmt.setString(3, user_phone_number);
                pstmt.setString(4, user_address);
                pstmt.setInt(5, 1);
                pstmt.setTimestamp(6, new java.sql.Timestamp(System.currentTimeMillis()));
                pstmt.setString(7, user_firstname);
                pstmt.setString(8, user_lastname);
                pstmt.executeUpdate();
            }
            
            pstmt.close();
            conn.close();
            
            return id+1;
            
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
