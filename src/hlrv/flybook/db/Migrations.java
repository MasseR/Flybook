package hlrv.flybook.db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.server.VaadinService;

/**
 * Extremely simple db migrations.
 * 
 * I had a nice clojure implementation, but didn't realize that for integrating
 * it to the project I would need to modify the build script on each of the
 * machines. The clj files would need to be included in the war if I'm not
 * mistaken. Or a better solution would be to compile the clj files, but as it's
 * special to clojure that too would need to modify build scripts
 */

public class Migrations {
    private JDBCConnectionPool pool;
    private List<String> migrations;

    public Migrations(JDBCConnectionPool pool) {
        this.pool = pool;
        this.migrations = new ArrayList<String>();

        // @formatter:off
        
        this.migrations.add("create table if not exists Aircrafts(register TEXT primary key, username TEXT, make_model TEXT, engine_count INTEGER, year TEXT, max_weight TEXT, capacity INTEGER, owner TEXT, address TEXT, optlock INTEGER default 0)");
        this.migrations.add("CREATE TABLE if not exists Airports(id INTEGER PRIMARY KEY, icao, iata, country, city, name, latitude REAL, longitude REAL, altitude, timezone, dst, optlock INTEGER DEFAULT 0)");
        this.migrations.add("CREATE TABLE if not exists FlightEntries(flight_id INTEGER PRIMARY KEY, username TEXT, date INTEGER , aircraft TEXT, departure_time INTEGER , departure_airport INTEGER , landing_time INTEGER , landing_airport INTEGER , onblock_time INTEGER , offblock_time INTEGER , flight_type INTEGER , ifr_time INTEGER , notes TEXT , optlock INTEGER DEFAULT 0)");
        this.migrations.add("CREATE TABLE if not exists Users(username TEXT PRIMARY KEY, password TEXT, firstname TEXT, lastname TEXT, admin INTEGER default 0, email TEXT, optlock INTEGER DEFAULT 0)");
        this.migrations.add("CREATE TRIGGER if not exists trigger_version_Aircrafts AFTER UPDATE ON Aircrafts FOR EACH ROW BEGIN UPDATE Aircrafts SET optlock = optlock + 1 WHERE register = OLD.register; END");
        this.migrations.add("CREATE TRIGGER if not exists trigger_version_Airports AFTER UPDATE ON Airports FOR EACH ROW BEGIN UPDATE Airports SET optlock = optlock + 1 WHERE id = OLD.id; END");
        this.migrations.add("CREATE TRIGGER if not exists trigger_version_FlightEntries AFTER UPDATE ON FlightEntries FOR EACH ROW BEGIN UPDATE FlightEntries SET optlock = optlock + 1 WHERE flight_id = OLD.flight_id; END");
        this.migrations.add("CREATE TRIGGER if not exists trigger_version_Users AFTER UPDATE ON Users FOR EACH ROW BEGIN UPDATE Users SET optlock = optlock + 1 WHERE username = OLD.username; END");
        this.migrations.add("create unique index if not exists icao_index on Airports (icao)");

        // @formatter:on

        // Import the airports
        try {

            String baseDir = VaadinService.getCurrent().getBaseDirectory()
                    .toString();

            BufferedReader in = new BufferedReader(new FileReader(baseDir + "/"
                    + "airports.sql"));
            String airport = null;
            while ((airport = in.readLine()) != null) {
                this.migrations.add(airport);
            }

            in.close();
        } catch (IOException e) {
            // Log errors
            System.out.println("Nope");
        }
    }

    public void runMigrations() throws SQLException {
        Connection conn = pool.reserveConnection();
        // try {
        conn.prepareStatement(
                "create table if not exists dbversion as select -1 as version")
                .execute();
        PreparedStatement latestVersionStmt = conn
                .prepareStatement("select version from dbversion");
        ResultSet rs = latestVersionStmt.executeQuery();
        // rs.first();
        rs.next();
        int latestVersion = rs.getInt(1);
        int i = 0;
        for (i = latestVersion + 1; i < this.migrations.size(); i++) {
            PreparedStatement stmt = conn.prepareStatement(this.migrations
                    .get(i));
            stmt.execute();
        }
        PreparedStatement updateVersionStmt = conn
                .prepareStatement("update dbversion set version=?");
        updateVersionStmt.setInt(1, i);
        updateVersionStmt.execute();
        conn.commit();
        pool.releaseConnection(conn);
        // } catch (SQLException e) {
        // conn.rollback();
        // }
    }
}
