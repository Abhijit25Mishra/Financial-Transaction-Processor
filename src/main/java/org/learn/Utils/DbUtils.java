package org.learn.Utils;

import org.learn.AccountService;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbUtils {

    private static String DB_URL;

    static {
        try {
            Properties props = new Properties();
            InputStream inputStream = AccountService.class.getClassLoader().getResourceAsStream("config.properties");
            if (inputStream == null) {
                throw new RuntimeException("config.properties not found in the classpath");
            }
            props.load(inputStream);
            DB_URL = props.getProperty("db.url");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static Connection getDbConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}
