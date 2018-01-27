package ru.ezhov.tabledestructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Подключение к базе
 * <p>
 *
 * @author ezhov_da
 */
public class ConnectionToBase {
    private static final Logger LOG = Logger.getLogger(ConnectionToBase.class.getName());
    private static ConnectionToBase connectionToBase;

    private ConnectionToBase() {
        ActiveLibraryPath.setPath();
    }


    public static Connection getConnection() throws Exception {
        if (Objects.isNull(connectionToBase)) {
            Class.forName(PropertiesHolder.getProperty("class.for.name"));
            connectionToBase = new ConnectionToBase();
        }
        return connectionToBase.createNewConnection();
    }

    private Connection createNewConnection() throws Exception {
        String url = PropertiesHolder.getProperty("url.connection");
        LOG.info("start connection...");
        Connection connection = DriverManager.getConnection(url);
        LOG.info("connection create");
        return connection;
    }


}
