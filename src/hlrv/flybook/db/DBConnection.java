package hlrv.flybook.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.server.VaadinService;

/**
 * DBConnection wraps JDBCConnectionPool that manages connections to Flybook
 * database.
 */
public class DBConnection {

    /**
     * Use single pool object.
     */
    private final SimpleJDBCConnectionPool pool;

    /**
     * Creates new DBConnection instance.
     * 
     * @throws SQLException
     */
    public DBConnection() throws SQLException {

        String filePath = getDatabaseFilePath();

        Logger logger = Logger.getLogger("DBConnection");
        logger.log(new LogRecord(Level.INFO, "Database: " + filePath));

        pool = new SimpleJDBCConnectionPool("org.sqlite.JDBC", "jdbc:sqlite:"
                + filePath, "anon", "", 2, 5);
    }

    /**
     * Returns JDBCConnectionPool object.
     * 
     * @return
     */
    public JDBCConnectionPool getPool() {
        return pool;
    }

    /**
     * Reserves JDBC Connection object and returns it.
     * 
     * @return
     */
    public Connection reserve() throws SQLException {
        return pool.reserveConnection();
    }

    /**
     * Releases JDBC Connection object.
     * 
     * @return
     */
    public void release(Connection conn) {
        pool.releaseConnection(conn);
    }

    /**
     * Returns absolute path to program database file.
     * 
     * @return
     */
    public String getDatabaseFilePath() {

        String baseDir = VaadinService.getCurrent().getBaseDirectory()
                .toString();
        String filePath = baseDir + "/" + DBConstants.FILENAME;

        return filePath;
    }

}
