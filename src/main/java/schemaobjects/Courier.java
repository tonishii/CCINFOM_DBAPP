package schemaobjects;
import java.sql.*;

public class Courier implements Account {
    public long     courier_id;
    public String   courier_name;
    public String   courier_email_address;
    public String   courier_address;
    public boolean  courier_verified_status;

    @Override
    public int login(int id){
        try {
            String url= "jdbc:mysql://localhost:3306/mydb"; // table details
            String username = "root"; // MySQL credentials
            String password = ""; // put ur password here lol
            Connection conn;
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, password);
            
            Statement statement = conn.createStatement();
            String query = "select user_id from users";
            
            ResultSet result = statement.executeQuery(query);
            
            while (result.next()) {
                if(id==result.getInt("user_id")){
                    return 1;
                }
            }
            return 0;
            
        } catch (Exception e){
            System.out.println(e.getMessage());
            return 0;
        }
    }
    
    @Override
    public int signUp() {
        try {
            String url= "jdbc:mysql://localhost:3306/mydb"; // table details
            String username = "root"; // MySQL credentials
            String password = "guycool123"; // put ur password here lol
            int id=0;
            Connection conn;
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, password);
            
            PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(courier_id) AS Id FROM mydb.couriers");
            ResultSet myRs = pstmt.executeQuery();
            while (myRs.next()) {
                id = myRs.getInt("Id");
            }
            if (id==0){
                pstmt = conn.prepareStatement("INSERT INTO couriers (courier_id, courier_name, courier_email_address, courier_address, courier_verified_status) VALUES(?, ?, ?, ?, ?)");
                pstmt.setInt(1, 1);
                pstmt.setString(2, courier_name);
                pstmt.setString(3, courier_email_address);
                pstmt.setString(4, courier_address);
                pstmt.setInt(5, 1);
                pstmt.executeUpdate();           
            }
            else{
                pstmt = conn.prepareStatement("INSERT INTO couriers (courier_id, courier_name, courier_email_address, courier_address, courier_verified_status) VALUES(?, ?, ?, ?, ?)");
                pstmt.setInt(1, id+1);
                pstmt.setString(2, courier_name);
                pstmt.setString(3, courier_email_address);
                pstmt.setString(4, courier_address);
                pstmt.setInt(5, 1);
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
