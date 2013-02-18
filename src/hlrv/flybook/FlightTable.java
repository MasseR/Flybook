package hlrv.flybook;

import java.sql.SQLException;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.ui.Table;

public class FlightTable extends Table {

    private final String[] columnIds = { "flight_id", "date", "username",
            "departure_airport", "departure_time", "landing_airport",
            "landing_time", "onblock_time", "offblock_time", "flight_type",
            "ifr_type", "flight_time", "notes" };

    private final String[] columnHeaders = { "Flight ID", "Pilot", "Date",
            "Aircraft", "Departure Time", "Departure Port", "Landing Time",
            "Landing Port", "On Block Time", "Off Block Time", "Flight Type",
            "IFR Time", "Notes", "VERSION" };

    public FlightTable() {

        setSelectable(true);

        setImmediate(true);

        // for (String id : columnIds) {
        // addContainerProperty(id, String.class, "xyz");
        // }
        //

    }

    @Override
    public void attach() {
        super.attach();

        try {

            DBConnection connection = (DBConnection) getSession().getAttribute(
                    "db");

            TableQuery tq = new TableQuery(DBConstants.TABLE_FLIGHTENTRIES,
                    connection.getPool());
            tq.setVersionColumn(DBConstants.COLUMN_FLIGHTENTRIES_OPTLOCK);

            SQLContainer container = new SQLContainer(tq);
            // container.setAutoCommit(true);

            container.setPageLength(5 * container.size());

            setContainerDataSource(container);

        } catch (SQLException e) {
            System.err.println(e.toString());
        }

        setColumnHeaders(columnHeaders);

        setNullSelectionAllowed(false);

        // Collection<?> itemIDs = getItemIds();
        // Iterator<?> it = itemIDs.iterator();
        // while (it.hasNext()) {
        // Object obj = it.next();
        // System.out.println(obj.getClass() + " : " + obj.toString());
        // }
    }
}
