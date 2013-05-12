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
class DbScriptExecutor {
    private static final String PROP_FILENAME = "env/db.properties";

    private Properties prop;
    private Statement statement;
    private String jdbcDriver;
    private String username, password;
    private String dbname;

    public DbScriptExecutor() {
        loadPropertiesFile();
    }

    public void executeScript(String script) {
        try (Connection con = DriverManager.getConnection(dbname, username, password)) {
            Class.forName(jdbcDriver);

            statement = con.createStatement();
            DbScriptExecutor.log.info("Database connection established");
            DbScriptExecutor.log.info("Passing script to database...");
            final int ROWS_COUNT = statement.executeUpdate(script, Statement.RETURN_GENERATED_KEYS);

            DbScriptExecutor.log.info("Script executed successfully.");
            DbScriptExecutor.log.info("Rows inserted: " + ROWS_COUNT);
            DbScriptExecutor.log.info("Connection is closed");
        } catch (Exception ex) {
            DbScriptExecutor.log.error("DataBase connection error", ex);
            DbScriptExecutor.log.error("Connection is closed");
        }
    }

    private void loadPropertiesFile() {
        prop = new Properties();
        try {
            prop.load(new FileInputStream(PROP_FILENAME));
            jdbcDriver = prop.getProperty("db.driver_class");
            dbname = prop.getProperty("db.url");
            username = prop.getProperty("db.username");
            password = prop.getProperty("db.password");
        } catch (IOException ex) {
            DbScriptExecutor.log.error("Error loading properties file", ex);
        }
    }
}
