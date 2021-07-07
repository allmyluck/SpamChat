package DatabaseHandler;
import java.sql.*;

public class DatabaseHandler {
    private Connection connection;

    public  DatabaseHandler() {
        try {
            String user = "root";
            String password = "darfghjjok89er";
            String settings = "?serverTimezone=UTC&useSSL=false";
            String host = "jdbc:mysql://localhost:3306/";
            String url = host + "SpamChat" + settings;
            connection = DriverManager.getConnection(url,user,password);
        } catch (Exception error) {
            System.out.println("Connection failed: " + error);
        }
    }
    //String function;
    private String GetTime(String message) {
        int index = message.indexOf("-[");
        index += 2;
        return message.substring(index, index + 19);
    }

    private Integer GetLoginId(String message) {
        int id = 0;
        int end = message.indexOf("-[");
        try {
        Statement stmt = connection.createStatement();
        String query = String.format("SELECT id FROM Users WHERE  login = '%s';", message.substring(0,end));
        ResultSet rs = stmt.executeQuery(query);
            if(rs.next()) {
                id = rs.getInt("id");
            }

        stmt.close();
        rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }


    private String GetText(String message) {
        String temp = message.replace("'","\\'");
        temp = temp.replace("\"","\\\"");
        int index = temp.indexOf("]");
        index = index + 2;
        return temp.substring(index);
    }


    public void AddMessageInDatabase(String message) {
        String time = GetTime(message);
        int id = GetLoginId(message);
        String text = GetText(message);
        try {
            Statement stmt = connection.createStatement();
            String query = String.format("INSERT INTO Data VALUES('%d','%s','%s');", id, text, time);
            stmt.executeUpdate(query);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean GetUser(String login, String password) {
        boolean check = false;
        try {
            Statement stmt = connection.createStatement();
            String query = String.format("SELECT * FROM Users WHERE  login = '%s' AND password = '%s';", login, password);
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next()) {
               check = true;
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return check;
    }
}
