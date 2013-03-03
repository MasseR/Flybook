package hlrv.flybook.containers;

import hlrv.flybook.FlightItem;
import hlrv.flybook.FlybookUI;
import hlrv.flybook.SessionContext;
import hlrv.flybook.auth.User;
import hlrv.flybook.db.DBConnection;
import hlrv.flybook.db.DBConstants;

import java.sql.SQLException;
import java.util.Date;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

public class FlightsContainer {

    private final SQLContainer container;

    private Filter usernameFilter;

    public FlightsContainer(DBConnection dbconn) throws SQLException {

        JDBCConnectionPool pool = dbconn.getPool();

        FreeformQuery query = new FreeformQuery("SELECT * FROM FlightEntries",
                pool, DBConstants.FLIGHTENTRIES_FLIGHT_ID);

        FlightEntriesFSDeletegate delegate = new FlightEntriesFSDeletegate();

        query.setDelegate(delegate);

        container = new SQLContainer(query);

        container.setAutoCommit(false);

        // container.setPageLength(5 * container.size());
    }

    public SQLContainer getContainer() {
        return container;
    }

    public void commit() {

        try {
            container.commit();
        } catch (SQLException e) {
            Notification.show("FlightsContainer Commit Error", e.toString(),
                    Notification.TYPE_ERROR_MESSAGE);
        }
    }

    public void rollback() {

        try {
            container.rollback();
        } catch (SQLException e) {
            Notification.show("FlightsContainer Rollback Error", e.toString(),
                    Notification.TYPE_ERROR_MESSAGE);
        }
    }

    public void filterByUser(String username) {

        if (usernameFilter != null) {
            container.removeContainerFilter(usernameFilter);
            usernameFilter = null;
        }

        if (username != null) {
            /**
             * Must use table prefix for filter to work. Also requires some
             * trickery in FlightEntriesFSDelegate constructor (something to do
             * with quotes)
             */
            usernameFilter = new Equal("FlightEntries.username", username);
            container.addContainerFilter(usernameFilter);
        }
    }

    public FlightItem addEntry(SessionContext ctx) {

        Object obj = container.addItem(); // returns temporary row id

        // getItem() by ignores filtered objects, so must use this one.
        FlightItem flightItem = new FlightItem(container.getItemUnfiltered(obj));

        /**
         * Initialize item with some sane values.
         */
        User curUser = ((FlybookUI) UI.getCurrent()).getUser().getBean();

        Date curTime = new Date();
        Integer curTimeSecs = (int) (curTime.getTime() / 1000L);

        flightItem.setFlightID(-1);

        flightItem.setDate(curTimeSecs);
        flightItem.setPilot(curUser.getUsername());
        flightItem.setPilotFullname(curUser.getFirstname() + " "
                + curUser.getLastname());

        flightItem.setDepartureAirport(1000);
        flightItem.setDepartureAirportString("");
        flightItem.setDepartureTime(curTimeSecs);

        flightItem.setLandingAirport(2000);
        flightItem.setLandingAirportString("");
        flightItem.setLandingTime(curTimeSecs);

        flightItem.setAircraft("REG123");

        flightItem.setOnBlockTime(0);
        flightItem.setOffBlockTime(0);

        flightItem.setFlightType(0);

        flightItem.setIFRTime(0);

        flightItem.setNotes("");

        return flightItem;
    }

    public boolean removeEntry(FlightItem e) {

        Object[] pkey = { new Integer(e.getFlightID()) };
        RowId id = new RowId(pkey);

        return container.removeItem(id);
    }
}