package hlrv.flybook.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.server.VaadinService;

/**
 * Wrapper class of JDBCConnectionPool.
 */
public class DBConnection {

    private SimpleJDBCConnectionPool pool;

    public DBConnection() throws SQLException {

        String filePath = getDatabaseFilePath();
        System.out.println("Database: " + filePath);

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
