package hlrv.flybook;

import hlrv.flybook.db.DBConnection;
import hlrv.flybook.db.DBConstants;
import hlrv.flybook.auth.User;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.server.VaadinSession;

public class SessionContext {

    /**
     * Wrapper around jdbc connections.
     */
    private DBConnection dbconn;

    /**
     * Current user item, won't be changing per session.
     */
    private BeanItem<User> currentUser;

    /**
     * Current selected FlightItem in table, can be changed so wrap in property
     * others can listen for changes.
     */
    private ObjectProperty<FlightItem> currentFlightEntry;

    /**
     * SQLContainer wrapper for FlightEntries.
     */
    private FlightsContainer flightsContainer;

    public SessionContext(VaadinSession session, DBConnection connection) throws Exception {

        dbconn = connection;

        // Add test user, assume login success

        User user = new User(
                "andven",
                "Andre", "Venter", "Andre.Venter@mail.com", false);

        currentUser = new BeanItem<User>(user);

        currentFlightEntry = new ObjectProperty<FlightItem>(null,
                FlightItem.class, false);

        flightsContainer = createFlightsContainer(dbconn);

        session.setAttribute("ctx", this);

    }

    // public BeanItem<User> getCurrentUser() {
    //     return currentUser;
    // }

    // public void setCurrentUser(User user)
    // {
    //     this.currentUser = new BeanItem<User>(user);
    // }

    public ObjectProperty<FlightItem> getCurrentFlightEntry() {
        return currentFlightEntry;
    }

    public DBConnection getDBConnection() {
        return dbconn;
    }

    public FlightsContainer getFlightsContainer() {
        return flightsContainer;
    }

    public static SessionContext getContext() {

        return (SessionContext) VaadinSession.getCurrent().getAttribute("ctx");
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
                .equals(currentUser.getBean().getUsername());
    }

    private FlightsContainer createFlightsContainer(DBConnection dbconn)
            throws Exception {

        FreeformQuery fq = new FreeformQuery("SELECT * FROM FlightEntries",
                dbconn.getPool(), DBConstants.FLIGHTENTRIES_FLIGHT_ID);

        FlightEntriesFSDeletegate delegate = new FlightEntriesFSDeletegate();

        fq.setDelegate(delegate);

        FlightsContainer container = new FlightsContainer(fq);

        return container;

    }
}
