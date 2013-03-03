package hlrv.flybook;

import hlrv.flybook.containers.FlightsContainer;
import hlrv.flybook.db.DBConstants;

import com.vaadin.ui.Table;

public class FlightsTable extends Table {

    /**
     * Which columns are shown in table?
     */
    private String[] visibleColumns = {
            DBConstants.FLIGHTENTRIES_FLIGHT_ID,
            DBConstants.FLIGHTENTRIES_USERNAME,
            DBConstants.FLIGHTENTRIES_DATE,
            DBConstants.FLIGHTENTRIES_DEPARTURE_AIRPORT,
            DBConstants.FLIGHTENTRIES_DEPARTURE_TIME,
            DBConstants.FLIGHTENTRIES_LANDING_AIRPORT,
            DBConstants.FLIGHTENTRIES_LANDING_TIME,
            // DBConstants.FLIGHTENTRIES_FLIGHT_TIME,
            DBConstants.FLIGHTENTRIES_AIRCRAFT,
            DBConstants.FLIGHTENTRIES_FLIGHT_TYPE,
            DBConstants.FLIGHTENTRIES_ONBLOCK_TIME,
            DBConstants.FLIGHTENTRIES_OFFBLOCK_TIME };

    /**
     * Columns headers matching to visible columns.
     */
    private String[] headers = { "Id", "Pilot", "Date", "Departure Airport",
            "Departure Time", "Landing Airport", "Landing Time", /*
                                                                  * "Flight Time",
                                                                  */
            "Aircraft", "Type", "On-Block Time", "Off-Block Time" };

    /**
     * Table columns that are initially collapsed.
     */
    private String[] initialCollapsedColumns = {
            DBConstants.FLIGHTENTRIES_FLIGHT_ID,
            DBConstants.FLIGHTENTRIES_ONBLOCK_TIME,
            DBConstants.FLIGHTENTRIES_OFFBLOCK_TIME };

    public FlightsTable() {

        FlightsContainer flightsContainer = SessionContext.getCurrent()
                .getFlightsContainer();

        // Remember to set data source before setVisibleColumns() etc.
        setContainerDataSource(flightsContainer.getContainer());
        setVisibleColumns(visibleColumns);
        setColumnHeaders(headers);

        setColumnCollapsingAllowed(true);
        for (String col : initialCollapsedColumns) {
            setColumnCollapsed(col, true);
        }

        setSelectable(true);
        setImmediate(true);
        setNullSelectionAllowed(false);
    }

    // @Override
    // public void valueChange(ValueChangeEvent event) {
    // }

    // @Override
    // public void containerItemSetChange(ItemSetChangeEvent event) {
    //
    // // Item selectedItem = table.getItem(table.getValue());
    // // if (selectedItem == null) {
    // // ctx.getCurrentFlightEntry().setValue(null);
    // // } else {
    // // FlightItem fe = ctx.getCurrentFlightEntry().getValue();
    // // if (fe == null || fe.getItem() != selectedItem) {
    // // ctx.getCurrentFlightEntry().setValue(
    // // new FlightItem(selectedItem));
    // // }
    // // }
    // }

    // @Override
    // public void rowIdChange(QueryDelegate.RowIdChangeEvent event) {
    //
    // System.err.println("Old ID: " + event.getOldRowId());
    // System.err.println("New ID: " + event.getNewRowId());
    //
    // // table.setValue(event.getNewRowId());
    // }
}
