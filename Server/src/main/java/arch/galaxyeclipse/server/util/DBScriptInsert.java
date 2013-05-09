package arch.galaxyeclipse.server.util;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import javax.sql.*;

/**
 * Created with IntelliJ IDEA.
 * User: Spaun
 * Date: 09.05.13
 * Time: 15:57
 * To change this template use File | Settings | File Templates.
 */

@Slf4j
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
        catch (SQLException ex) {
            log.error(ex.getMessage());
        }
        catch(Exception ex){
            log.error("DataBase connection error." + ex.getMessage());
        }
    }

    public void executeScript(String script){
        try{
            log.info("Inserting script to database...");
            final int ROWS_COUNT = statement.executeUpdate(script, Statement.RETURN_GENERATED_KEYS);
            log.info("Script executed successfully.");
            log.info("Rows inserted: " + ROWS_COUNT);
        }
        catch(SQLException ex){
            log.error("Script insert error. " + ex.getMessage());
        }
        catch(Exception ex){
            log.error(ex.getMessage());
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
