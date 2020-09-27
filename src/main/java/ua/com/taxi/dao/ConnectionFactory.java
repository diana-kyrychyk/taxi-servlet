package ua.com.taxi.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ua.com.taxi.util.FileIO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    private static final String PROPERTIES_FILE_NAME = "application.properties";
    private static final String KEY_DRIVER = "db.driver";
    private static final String KEY_URL = "db.url";
    private static final String KEY_USER = "db.user";
    private static final String KEY_PASSWORD = "db.password";

    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    private static FileIO fileIO = new FileIO();

    static {
        Properties properties = fileIO.readProperties(PROPERTIES_FILE_NAME);
        String driver = properties.getProperty(KEY_DRIVER);
        String url = properties.getProperty(KEY_URL);
        String user = properties.getProperty(KEY_USER);
        String password = properties.getProperty(KEY_PASSWORD);

        config.setDriverClassName(driver);
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);
    }

    private ConnectionFactory() {
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }


}


