package hlrv.flybook.db.containers;

import hlrv.flybook.FlybookUI;
import hlrv.flybook.auth.User;
import hlrv.flybook.db.DBConnection;
import hlrv.flybook.db.DBConstants;
import hlrv.flybook.db.items.AircraftItem;

import java.sql.SQLException;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.ui.UI;

/**
 * AircraftsContainer wraps primary Aircrafts SQLContainer and provides helper
 * methods.
 */
public class AircraftsContainer {

    /**
     * Primary container.
     */
    private SQLContainer aircraftsContainer;

    /**
     * Unfiltered container.
     */
    private SQLContainer unfilteredAircraftsContainer;

    /**
     * Custom permanent filter.
     */
    private Filter customFilter;

    public AircraftsContainer(DBConnection dbconn) throws SQLException {

        JDBCConnectionPool pool = dbconn.getPool();

        TableQuery tq = new TableQuery(DBConstants.TABLE_AIRCRAFTS, pool);
        tq.setVersionColumn(DBConstants.AIRCRAFTS_OPTLOCK);
        aircraftsContainer = new SQLContainer(tq);
        aircraftsContainer.setAutoCommit(false);

        unfilteredAircraftsContainer = new SQLContainer(tq);
        unfilteredAircraftsContainer.setAutoCommit(false);
    }

    /**
     * Returns the SQLContainer.
     */
    public SQLContainer getContainer() {
        return aircraftsContainer;
    }

    /**
     * Returns the SQLContainer.
     */
    public SQLContainer getUnfilteredContainer() {
        return unfilteredAircraftsContainer;
    }

    public boolean containsItem(String register) {

        Object[] pkey = { register };
        RowId id = new RowId(pkey);
        return aircraftsContainer.getItemUnfiltered(id) != null;
    }

    /**
     * Returns AircraftItem corresponding to register id.
     * 
     * @param username
     * @return
     */
    public AircraftItem getItem(String register) {

        Item item = null;
        Object id = null;
        if (register != null) {
            // setTemporaryFilter(new Equal(DBConstants.AIRCRAFTS_REGISTER,
            // register));
            unfilteredAircraftsContainer.addContainerFilter(new Equal(
                    DBConstants.AIRCRAFTS_REGISTER, register));
            id = unfilteredAircraftsContainer.firstItemId();
            item = unfilteredAircraftsContainer.getItem(id);
            unfilteredAircraftsContainer.removeAllContainerFilters();
            // restoreFilters();
        }
        return new AircraftItem(item, id);
    }

    /**
     * Add custom filter.
     */
    public void filterBy(Filter filter) {

        if (customFilter != null) {
            aircraftsContainer.removeContainerFilter(customFilter);
            customFilter = null;
        }

        if (filter != null) {
            aircraftsContainer.addContainerFilter(filter);
            customFilter = filter;
        }
    }

    /**
     * Creates a new row in container and initializes it with default values.
     * 
     * Note that new row is temporary only and commit() must be called in order
     * to finalize addition. Temporary row addition can be cancelled by calling
     * rollback() instead.
     * 
     * @return AircraftItem
     */
    public AircraftItem addEntry() {

        /**
         * Get first unused register.
         */

        /**
         * Create new item and get temporary row id.
         */
        Object tempItemId = aircraftsContainer.addItem();

        /**
         * Filtered objects are ignored by getItem(), so we must use this one.
         */
        AircraftItem acItem = new AircraftItem(
                aircraftsContainer.getItemUnfiltered(tempItemId), tempItemId);

        /**
         * Initialize item with some sane values.
         */
        User curUser = ((FlybookUI) UI.getCurrent()).getUser().getBean();

        acItem.setRegister(getUniqueRegister());
        acItem.setUsername(curUser.getUsername());
        acItem.setMakeAndModel("");
        acItem.setCapacity(1);
        acItem.setYear("");
        acItem.setEngineCount(1);
        acItem.setMaxWeight("");
        acItem.setOwner("");
        acItem.setAddress("");

        return acItem;
    }

    /**
     * Removes item from container.
     * 
     * @param item
     * @return true if entry successfully removed
     */
    public boolean removeEntry(AircraftItem item) {

        Object[] pkey = { item.getRegister() };
        RowId id = new RowId(pkey);

        return aircraftsContainer.removeItem(id);
    }

    /**
     * Commit changes to SQLContainer.
     */
    public void commit() throws SQLException {

        aircraftsContainer.commit();
    }

    /**
     * Rollback changes.
     */
    public void rollback() throws SQLException {

        aircraftsContainer.rollback();
    }

    /**
     * Helper method to replace all current filters with the one given as
     * argument. One should call resetFilters soon after.
     */
    private void setTemporaryFilter(Filter filter) {

        aircraftsContainer.removeAllContainerFilters();
        aircraftsContainer.addContainerFilter(filter);
    }

    /**
     * Restores permanent filters.
     */
    private void restoreFilters() {

        aircraftsContainer.removeAllContainerFilters();

        if (customFilter != null) {
            aircraftsContainer.addContainerFilter(customFilter);
        }
    }

    /**
     * Finds unique register.
     * 
     * @return
     */
    private String getUniqueRegister() {

        int regId = 1;
        while (true) {
            String reg = "REG-";
            if (regId < 10) {
                reg += "00";
            } else if (regId < 100) {
                reg += "0";
            }
            reg += regId;

            if (!containsItem(reg)) {
                return reg;
            }
            ++regId;
        }
    }
}
