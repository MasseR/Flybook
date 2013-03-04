package hlrv.flybook.db.containers;

import hlrv.flybook.FlightType;
import hlrv.flybook.FlybookUI;
import hlrv.flybook.auth.User;
import hlrv.flybook.db.DBConnection;
import hlrv.flybook.db.DBConstants;
import hlrv.flybook.db.items.FlightItem;

import java.sql.SQLException;
import java.util.Date;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

/**
 * FlightsContainer abstracts SQLContainer to table "FlightEntries".
 */
public class FlightsContainer {

    /**
     * Primary container.
     */
    private SQLContainer flightsContainer;

    /**
     * Keep reference to filters so we can remove/add them from container.
     */
    private Filter usernameFilter;

    /**
     * Container that holds flight types.
     */
    private IndexedContainer flightTypesContainer;

    public static final String PID_FLIGHT_TYPE = "type";

    /**
     * Create new instance of FlightsContainer that uses DBConnection given as
     * argument.
     */
    public FlightsContainer(DBConnection dbconn) throws SQLException {

        JDBCConnectionPool pool = dbconn.getPool();

        TableQuery query = new TableQuery(DBConstants.TABLE_FLIGHTENTRIES, pool);
        query.setVersionColumn(DBConstants.FLIGHTENTRIES_OPTLOCK);

        // FreeformQuery query = new
        // FreeformQuery("SELECT * FROM FlightEntries",
        // pool, DBConstants.FLIGHTENTRIES_FLIGHT_ID);

        // FlightEntriesFSDeletegate delegate = new FlightEntriesFSDeletegate();

        // query.setDelegate(delegate);

        flightsContainer = new SQLContainer(query);
        flightsContainer.setAutoCommit(false);

        flightTypesContainer = createFlightTypesContainer();

        // container.setPageLength(5 * container.size());
    }

    /**
     * Returns the SQLContainer.
     */
    public SQLContainer getContainer() {
        return flightsContainer;
    }

    public IndexedContainer getFlightTypesContainer() {
        return flightTypesContainer;
    }

    /**
     * Commit changes to SQLContainer.
     */
    public void commit() throws SQLException {

        flightsContainer.commit();
    }

    /**
     * Rollback changes.
     */
    public void rollback() {

        try {
            flightsContainer.rollback();
        } catch (SQLException e) {
            Notification.show("FlightsContainer Rollback Error", e.toString(),
                    Notification.TYPE_ERROR_MESSAGE);
        }
    }

    /**
     * Add username filter. If null, removes filter.
     */
    public void filterByUser(String username) {

        if (usernameFilter != null) {
            flightsContainer.removeContainerFilter(usernameFilter);
            usernameFilter = null;
        }

        if (username != null) {
            /**
             * Must use table prefix for filter to work with custom implemented
             * FreeformQuery (NOTE: not used presently). Also requires some
             * trickery in FlightEntriesFSDelegate constructor (something to do
             * with quotes)
             */
            // usernameFilter = new Equal("FlightEntries.username", username);
            usernameFilter = new Equal(DBConstants.FLIGHTENTRIES_USERNAME,
                    username);
            flightsContainer.addContainerFilter(usernameFilter);
        }
    }

    /**
     * Creates a new row in container and initializes it with default values.
     * 
     * Note that new row is temporary only and commit() must be called in order
     * to finalize addition. Temporary row addition can be cancelled by calling
     * rollback() instead.
     * 
     * @return FlightItem
     */
    public FlightItem addEntry() {

        Object obj = flightsContainer.addItem(); // returns temporary row id

        /**
         * getItem() ignores filtered objects, so must use this one.
         */
        FlightItem flightItem = new FlightItem(
                flightsContainer.getItemUnfiltered(obj));

        /**
         * Initialize item with some sane values.
         */
        User curUser = ((FlybookUI) UI.getCurrent()).getUser().getBean();

        Date curTime = new Date();
        Integer curTimeSecs = (int) (curTime.getTime() / 1000L);

        /**
         * FlightID is special case, it should probably be null, so that when
         * commit is called, database can initialize id with unique value.
         */
        // flightItem.setFlightID(null);

        flightItem.setDate(curTimeSecs);
        flightItem.setUsername(curUser.getUsername());
        // flightItem.setPilotFullname(curUser.getFirstname() + " "
        // + curUser.getLastname());

        // TODO: Could be neat if new item was initialized to users current
        // physical location...
        flightItem.setDepartureAirport(null);
        // flightItem.setDepartureAirportString("");
        flightItem.setDepartureTime(curTimeSecs);

        flightItem.setLandingAirport(null);
        // flightItem.setLandingAirportString("");
        flightItem.setLandingTime(curTimeSecs);

        // TODO: This is temporary value
        flightItem.setAircraft("REG123");

        flightItem.setOnBlockTime(0);
        flightItem.setOffBlockTime(0);

        flightItem.setFlightType(0);

        flightItem.setIFRTime(0);

        flightItem.setNotes("");

        return flightItem;
    }

    /**
     * Removes item from container.
     * 
     * @param item
     * @return true if entry successfully removed
     */
    public boolean removeEntry(FlightItem item) {

        Object[] pkey = { new Integer(item.getFlightID()) };
        RowId id = new RowId(pkey);

        return flightsContainer.removeItem(id);
    }

    private IndexedContainer createFlightTypesContainer() {

        final String caption = PID_FLIGHT_TYPE;

        IndexedContainer flightTypeContainer = new IndexedContainer();
        flightTypeContainer.addContainerProperty(caption, String.class, null);

        for (FlightType type : FlightType.values()) {
            Item item = flightTypeContainer
                    .addItem((new Integer(type.ordinal())));
            item.getItemProperty(caption).setValue(type.getName());
        }

        // flightTypeContainer
        // .addItem(new Integer(FlightType.UNDEFINED.ordinal()))
        // .getItemProperty(caption).setValue("Undefined");
        //
        // flightTypeContainer.addItem(new
        // Integer(FlightType.DOMESTIC.ordinal()))
        // .getItemProperty(caption).setValue("Domestic");
        //
        // flightTypeContainer.addItem(new Integer(FlightType.HOBBY.ordinal()))
        // .getItemProperty(caption).setValue("Hobby");
        //
        // flightTypeContainer
        // .addItem(new Integer(FlightType.TRANSREGIONAL.ordinal()))
        // .getItemProperty(caption).setValue("Transregional");
        //
        // flightTypeContainer
        // .addItem(new Integer(FlightType.TRANSCONTINENTAL.ordinal()))
        // .getItemProperty(caption).setValue("Transcontinental");

        return flightTypeContainer;
    }
}
