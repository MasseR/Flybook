package hlrv.flybook;

import hlrv.flybook.containers.AirportsContainer;
import hlrv.flybook.containers.FlightsContainer;
import hlrv.flybook.db.DBConnection;

import java.sql.SQLException;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

public class SessionContext {

    /**
     * Wrapper around jdbc connections.
     */
    private DBConnection dbconn;


    /**
     * Current selected FlightItem in table, can be changed so wrap in property
     * others can listen for changes.
     */
    private ObjectProperty<FlightItem> currentFlightEntry;

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

        currentFlightEntry = new ObjectProperty<FlightItem>(null,
                FlightItem.class, false);

        try {

            airportsContainer = new AirportsContainer(dbconn);

            flightsContainer = new FlightsContainer(dbconn);

        } catch (SQLException e) {
            throw new Exception("Failed to create session container: "
                    + e.toString());
        }

        session.setAttribute("context", this);
    }

    public ObjectProperty<FlightItem> getCurrentFlightEntry() {
        return currentFlightEntry;
    }

    public DBConnection getDBConnection() {
        return dbconn;
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

    /**
     * Helper method that returns true if current selected flight entry has been
     * created/owned by current user.
     */
    public boolean isCurrentFlightEntryCreatedByUser() {
        if (currentFlightEntry.getValue() == null) {
            return false;
        }

        return currentFlightEntry.getValue().getPilot()
                .equals(((FlybookUI) UI.getCurrent()).getUser().getBean().getUsername());
    }
}
