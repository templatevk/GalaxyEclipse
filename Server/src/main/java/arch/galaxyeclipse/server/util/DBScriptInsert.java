package arch.galaxyeclipse.server.util;
import java.sql.*;
import javax.sql.*;

/**
 * Created with IntelliJ IDEA.
 * User: Spaun
 * Date: 09.05.13
 * Time: 15:57
 * To change this template use File | Settings | File Templates.
 */
public class DBScriptInsert {
    private Connection con = null;
    private Statement statement= null;
    private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private final String username = "root", password = "";
    private final String dbname = "jdbc:mysql://localhost/ge";

    public  DBScriptInsert(){
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(dbname,username,password);
            statement = con.createStatement();
            System.out.println ("Database connection established");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch(Exception ex){
            System.out.println("DataBase connection error." + ex.getMessage());
        }
    }

    public void executeScript(String script){
        try{
            System.out.println("Inserting script to database...");
            final int ROWS_COUNT = statement.executeUpdate(script, Statement.RETURN_GENERATED_KEYS);
            System.out.println("Script executed successfully.");
            System.out.println("Rows inserted: " + ROWS_COUNT);
        }
        catch(SQLException  e){
            System.out.println("Script insert error. " + e.getMessage());
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            try{
                if(statement != null)
                    con.close();
            }
            catch(SQLException se){
            }
            try{
                if(con != null){
                    con.close();
                    System.out.println("Connection is closed.");
                }
            }
            catch(SQLException se){
                se.printStackTrace();
            }
        }

    }
}
