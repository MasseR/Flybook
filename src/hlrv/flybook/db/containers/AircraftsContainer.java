package hlrv.flybook.db.containers;

import hlrv.flybook.db.DBConnection;
import hlrv.flybook.db.DBConstants;
import hlrv.flybook.db.items.AircraftItem;

import java.sql.SQLException;

import com.vaadin.data.Item;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;

public class AircraftsContainer {

    /**
     * Primary container.
     */
    private SQLContainer aircraftsContainer;

    public AircraftsContainer(DBConnection dbconn) throws SQLException {

        JDBCConnectionPool pool = dbconn.getPool();

        TableQuery tq = new TableQuery(DBConstants.TABLE_AIRCRAFTS, pool);
        tq.setVersionColumn(DBConstants.USERS_OPTLOCK);
        aircraftsContainer = new SQLContainer(tq);
        aircraftsContainer.setAutoCommit(false);
    }

    /**
     * Returns the SQLContainer.
     */
    public SQLContainer getContainer() {
        return aircraftsContainer;
    }

    /**
     * Returns AircraftItem corresponding to register id.
     * 
     * @param username
     * @return
     */
    public AircraftItem getItem(String register) {

        Item item = null;
        if (register != null) {
            aircraftsContainer.addContainerFilter(new Equal(
                    DBConstants.AIRCRAFTS_REGISTER, register));
            item = aircraftsContainer.getItem(aircraftsContainer.firstItemId());
            aircraftsContainer.removeAllContainerFilters();
        }
        return new AircraftItem(item);
    }

}
