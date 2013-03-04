package hlrv.flybook;

import hlrv.flybook.db.DBConnection;
import hlrv.flybook.db.containers.AirportsContainer;
import hlrv.flybook.db.containers.FlightsContainer;
import hlrv.flybook.db.containers.UsersContainer;

import java.sql.SQLException;

import com.vaadin.server.VaadinSession;

public class SessionContext {

    /**
     * Database connections manager.
     */
    private final DBConnection dbconn;

    /**
     * Container for Users table.
     */
    private final UsersContainer usersContainer;

    /**
     * SQLContainer wrapper for FlightEntries.
     */
    private FlightsContainer flightsContainer;

    /**
     * SQLContainer wrapper for Airports.
     */
    private AirportsContainer airportsContainer;

    public SessionContext(VaadinSession session, DBConnection connection)
            throws Exception {

        dbconn = connection;

        // currentFlightEntry = new ObjectProperty<FlightItem>(null,
        // FlightItem.class, false);

        try {

            usersContainer = new UsersContainer(dbconn);

            airportsContainer = new AirportsContainer(dbconn);

            flightsContainer = new FlightsContainer(dbconn);

        } catch (SQLException e) {
            throw new Exception("Failed to create session container: "
                    + e.toString());
        }

        session.setAttribute("context", this);
    }

    /**
     * Returns DBConnection.
     */
    public DBConnection getDBConnection() {
        return dbconn;
    }

    public UsersContainer getUsersContainer() {
        return usersContainer;
    }

    public FlightsContainer getFlightsContainer() {
        return flightsContainer;
    }

    public AirportsContainer getAirportsContainer() {
        return airportsContainer;
    }

    public static SessionContext getCurrent() {

        return (SessionContext) VaadinSession.getCurrent().getAttribute(
                "context");
    }
}
