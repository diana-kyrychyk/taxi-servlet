package ua.com.taxi.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.taxi.exception.DaoException;
import ua.com.taxi.util.FileIO;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    private static ConnectionFactory instance;

    private static final String PROPERTIES_FILE_NAME = "application.properties";
    private static final String KEY_DS_JNDI_NAME = "ds.jndi.name";
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionFactory.class);

    private static FileIO fileIO = new FileIO();
    private DataSource ds;

    public static Connection getConnection() throws SQLException {
        ConnectionFactory instance = getInstance();
        return instance.ds.getConnection();
    }

    private ConnectionFactory() {
        Properties properties = fileIO.readProperties(PROPERTIES_FILE_NAME);
        String dsName = properties.getProperty(KEY_DS_JNDI_NAME);
        try {
            InitialContext cxt = new InitialContext();
            ds = (DataSource) cxt.lookup(dsName);
        } catch (NamingException e) {
            LOGGER.error("Lookup DataSource failed");
            throw new DaoException("Lookup DataSource failed", e);
        }
    }

    private static ConnectionFactory getInstance() {
        if (instance == null) {
            synchronized (ConnectionFactory.class) {
                if (instance == null) {
                    instance = new ConnectionFactory();
                }
            }
        }
        return instance;
    }
}
