package hlrv.flybook;

import java.sql.SQLException;
import java.util.Collection;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Table;

public class FlightsTable extends CustomComponent implements
        Property.ValueChangeListener {

    private SessionContext ctx;

    private final String[] columnIds = { "flight_id", "date", "username",
            "departure_airport", "departure_time", "landing_airport",
            "landing_time", "onblock_time", "offblock_time", "flight_type",
            "ifr_type", "flight_time", "notes" };

    private final String[] columnHeaders = { "Flight ID", "Pilot", "Date",
            "Aircraft", "Departure Time", "Departure Port", "Landing Time",
            "Landing Port", "On Block Time", "Off Block Time", "Flight Type",
            "IFR Time", "Notes", "VERSION" };

    private Table table;

    private Filter usernameFilter;

    public FlightsTable(SessionContext ctx) {

        this.ctx = ctx;

        table = new Table();
        table.setSelectable(true);
        table.setImmediate(true);
        table.setNullSelectionAllowed(false);
        table.setColumnCollapsingAllowed(true);
        table.addValueChangeListener(this);
        table.setSizeFull();

        // setSizeUndefined();

        try {

            DBConnection dbconn = ctx.getDBConnection();
            User user = ctx.getCurrentUser().getValue();

            FreeformQuery fq = new FreeformQuery("SELECT * FROM FlightEntries",
                    dbconn.getPool(), DBConstants.FLIGHTENTRIES_FLIGHT_ID);

            fq.setDelegate(new FlightEntriesFSDeletegate());

            // TableQuery tq = new TableQuery(DBConstants.TABLE_FLIGHTENTRIES,
            // connection.getPool());
            // tq.setVersionColumn(DBConstants.FLIGHTENTRIES_OPTLOCK);

            SQLContainer container = new SQLContainer(fq);

            // container.addContainerFilter(new Equal("fe.c_username", user
            // .getUsername()));

            container.setAutoCommit(true);

            container.setPageLength(5 * container.size());

            table.setContainerDataSource(container);

            filterByUser(user.getUsername());

            Collection<?> pids = table.getContainerPropertyIds();

        } catch (SQLException e) {
            System.err.println(e.toString());
        }

        setCompositionRoot(table);
    }

    public void filterByUser(String username) {

        SQLContainer container = (SQLContainer) table.getContainerDataSource();

        if (usernameFilter != null) {
            container.removeContainerFilter(usernameFilter);
            usernameFilter = null;
        }

        if (username != null) {
            usernameFilter = new Equal("fe.c_username", username);
            container.addContainerFilter(usernameFilter);
        }
    }

    @Override
    public void attach() {
        super.attach();

        // table.setColumnHeaders(columnHeaders);

        // Collection<?> itemIDs = getItemIds();
        // Iterator<?> it = itemIDs.iterator();
        // while (it.hasNext()) {
        // Object obj = it.next();
        // System.out.println(obj.getClass() + " : " + obj.toString());
        // }
    }

    // public void addSelectionChangeListener(Property.ValueChangeListener
    // listener) {
    // table.addValueChangeListener(listener);
    // }
    //
    // public Object getCurrentSelection() {
    // return table.getValue();
    // }
    //
    // public Item getFlightItem(Object iid) {
    // return table.getItem(iid);
    // }
    //
    // public Property getProperty(Object iid, Object pid) {
    // return table.getContainerProperty(iid, pid);
    // }

    @Override
    public void valueChange(ValueChangeEvent event) {

        /**
         * Reset FlightEntry property
         */

        Item currentItem = table.getItem(event.getProperty().getValue());

        ctx.getCurrentFlightEntry().setValue(currentItem);
    }

}
