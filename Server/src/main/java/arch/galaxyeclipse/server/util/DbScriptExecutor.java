package arch.galaxyeclipse.server.util;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Spaun
 * Date: 09.05.13
 * Time: 15:57
 * To change this template use File | Settings | File Templates.
 */

@Slf4j
public class DbScriptExecutor {
    private static final String PROP_FILE_NAME = "local.properties";

    private Properties prop;
<<<<<<< HEAD
    private Statement statement;
=======
    private static final String PROP_FILENAME = "local.properties";
    private Statement statement = null;
>>>>>>> remotes/origin/dev
    private String jdbcDriver;
    private String username, password;
    private String dbname;

    protected DbScriptExecutor() {
        loadPropertiesFile();
    }

    private void loadPropertiesFile() {
        prop = new Properties();
        try {
<<<<<<< HEAD
            prop.load(new FileInputStream(PROP_FILE_NAME));
=======
            prop.load(new FileInputStream(PROP_FILENAME));
>>>>>>> remotes/origin/dev
            jdbcDriver = prop.getProperty("db.driver_class");
            dbname = prop.getProperty("db.url");
            username = prop.getProperty("db.username");
            password = prop.getProperty("db.password");
        } catch (IOException ex) {
            log.error("Error loading properties file", ex);
        }
    }

    protected void executeScript(String script) {
        try (Connection con = DriverManager.getConnection(dbname, username, password)) {
            Class.forName(jdbcDriver);

            statement = con.createStatement();
            log.info("Database connection established");
            log.info("Executing script to database...");
            final int ROWS_COUNT = statement.executeUpdate(script, Statement.RETURN_GENERATED_KEYS);

            log.info("Script executed successfully.");
            log.info("Rows inserted: " + ROWS_COUNT);
            log.info("Connection is closed");
        } catch (Exception ex) {
            log.error("DataBase connection error", ex);
            log.error("Connection is closed");
        }
    }
}
