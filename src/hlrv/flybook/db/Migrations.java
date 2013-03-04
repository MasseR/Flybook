package hlrv.flybook.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.CallableStatement;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import java.util.List;
import java.util.ArrayList;

/** Extremely simple db migrations.
 *
 * I had a nice clojure implementation, but didn't realize that for integrating
 * it to the project I would need to modify the build script on each of the
 * machines. The clj files would need to be included in the war if I'm not
 * mistaken. Or a better solution would be to compile the clj files, but as
 * it's special to clojure that too would need to modify build scripts
 */

public class Migrations
{
    private JDBCConnectionPool pool;
    private List<String> migrations;

    public Migrations(JDBCConnectionPool pool)
    {
        this.pool = pool;
        this.migrations = new ArrayList<String>();
        this.migrations.add("create table test (id)");
    }

    public void runMigrations() throws SQLException
    {
        Connection conn = pool.reserveConnection();
        CallableStatement latestVersionStmt = conn.prepareCall("select version from dbversion");
        latestVersionStmt.execute();
        int latestVersion = latestVersionStmt.getInt(0);
        int i = 0;
        for(i = latestVersion + 1; i < this.migrations.size(); i++)
        {
            CallableStatement stmt = conn.prepareCall(this.migrations.get(i));
            stmt.execute();
        }
        CallableStatement updateVersionStmt = conn.prepareCall("update dbversion set version=:version");
        updateVersionStmt.setInt(":version", i);
        updateVersionStmt.execute();
    }
}
