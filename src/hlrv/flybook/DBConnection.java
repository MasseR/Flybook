package hlrv.flybook;

import java.sql.Connection;
import java.sql.SQLException;

import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.server.VaadinService;

public class DBConnection {

    SimpleJDBCConnectionPool pool;

    public DBConnection() throws SQLException {

        String baseDir = VaadinService.getCurrent().getBaseDirectory()
                .toString();
        String filePath = baseDir + "/" + DBConstants.FILENAME;

        System.out.println("Database: " + filePath);

        pool = new SimpleJDBCConnectionPool("org.sqlite.JDBC", "jdbc:sqlite:"
                + filePath, "anon", "", 2, 5);
    }

    public JDBCConnectionPool getPool() {
        return pool;
    }

    public Connection reserve() throws SQLException {
        return pool.reserveConnection();
    }

    public void release(Connection conn) {
        pool.releaseConnection(conn);
    }

}
