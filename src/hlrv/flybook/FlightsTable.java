package hlrv.flybook;

import hlrv.flybook.db.DBConstants;
import hlrv.flybook.db.containers.AirportsContainer;
import hlrv.flybook.db.containers.FlightsContainer;
import hlrv.flybook.db.containers.UsersContainer;
import hlrv.flybook.db.items.AirportItem;
import hlrv.flybook.db.items.UserItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.vaadin.data.Property;
import com.vaadin.server.Resource;
import com.vaadin.ui.Label;
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

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    // private DateFormat dateFormat = new
    // SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Table columns that are initially collapsed.
     */
    private String[] initialCollapsedColumns = {
            DBConstants.FLIGHTENTRIES_FLIGHT_ID,
            DBConstants.FLIGHTENTRIES_ONBLOCK_TIME,
            DBConstants.FLIGHTENTRIES_OFFBLOCK_TIME };

    // private static final String GEN_FULLNAME = "fullname";
    private static final String GEN_FLIGHT_TIME = "flight_time";

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

        TableColumnGenerator colGenerator = new TableColumnGenerator();

        // SQLContainer doesn't support column additions
        // addContainerProperty(GEN_FLIGHT_TIME, Integer.class, null,
        // "Flight Time", null, null);

        // addGeneratedColumn(GEN_FULLNAME, colGenerator);
        addGeneratedColumn(DBConstants.FLIGHTENTRIES_USERNAME, colGenerator);
        addGeneratedColumn(DBConstants.FLIGHTENTRIES_DEPARTURE_AIRPORT,
                colGenerator);
        addGeneratedColumn(DBConstants.FLIGHTENTRIES_LANDING_AIRPORT,
                colGenerator);
        addGeneratedColumn(GEN_FLIGHT_TIME, colGenerator);

        setSelectable(true);
        setImmediate(true);
        setNullSelectionAllowed(false);

    }

    @Override
    protected String formatPropertyValue(Object rowId, Object colId,
            Property<?> property) {

        /**
         * Because we have time values stored in table as unixtime integer we
         * convert it to string.
         * 
         * NOTE: time is in seconds, not milliseconds!
         */
        if (colId.equals(DBConstants.FLIGHTENTRIES_DATE)
                || colId.equals(DBConstants.FLIGHTENTRIES_DEPARTURE_TIME)
                || colId.equals(DBConstants.FLIGHTENTRIES_LANDING_TIME)) {
            long time = (Integer) property.getValue();
            return dateFormat.format(new Date(time * 1000L));
        }
        return super.formatPropertyValue(rowId, colId, property);
    }

    /**
     * Generator for columns.
     */
    private class TableColumnGenerator implements Table.ColumnGenerator {

        private UsersContainer usersContainer;
        private AirportsContainer airportsContainer;

        public TableColumnGenerator() {

            usersContainer = SessionContext.getCurrent().getUsersContainer();

            airportsContainer = SessionContext.getCurrent()
                    .getAirportsContainer();
        }

        @Override
        public Object generateCell(Table table, Object itemId, Object columnId) {

            Property prop = table.getItem(itemId).getItemProperty(columnId);

            if (columnId.equals(DBConstants.AIRCRAFTS_USERNAME)) {

                /**
                 * Get username and create fullname by accessing UsersContainer.
                 */
                String username = (String) prop.getValue();

                UserItem useritem = usersContainer.getItem(username);

                Label label = new Label(username);
                label.setDescription(useritem.getFirstname() + " "
                        + useritem.getLastname());
                return label;
            }

            if (columnId.equals(DBConstants.FLIGHTENTRIES_DEPARTURE_AIRPORT)
                    || columnId
                            .equals(DBConstants.FLIGHTENTRIES_LANDING_AIRPORT)) {

                /**
                 * Change airport id to nicer string form. To do this, we need
                 * to access AirportsContainer.
                 */
                Integer port = (Integer) prop.getValue();

                Label label = new Label();
                if (port != null) {

                    AirportItem apitem = airportsContainer.getItem(port);
                    String country = apitem.getCountry();
                    String name = apitem.getName();

                    StringBuilder sb = new StringBuilder();
                    sb.append("<ul><li><b>Airport</b>: ").append(name)
                            .append("</li>");
                    sb.append("<li><b>Country</b>: ").append(country)
                            .append("</li>");
                    sb.append("<li><b>City</b>: ").append(apitem.getCity())
                            .append("</li>");
                    sb.append("<li><b>ICAO</b>: ").append(apitem.getICAOCode())
                            .append("</li</ul>");

                    label.setValue(name);
                    label.setDescription(sb.toString());

                    Resource res = (Resource) airportsContainer
                            .getCountriesContainer()
                            .getItem(country)
                            .getItemProperty(
                                    AirportsContainer.PID_COUNTRIES_ICON)
                            .getValue();

                    label.setIcon(res);
                    label.setImmediate(true);
                }

                return label;
            }

            if (columnId.equals(GEN_FLIGHT_TIME)) {

                int depTime = (Integer) table
                        .getItem(itemId)
                        .getItemProperty(
                                DBConstants.FLIGHTENTRIES_DEPARTURE_TIME)
                        .getValue();

                int landTime = (Integer) table
                        .getItem(itemId)
                        .getItemProperty(DBConstants.FLIGHTENTRIES_LANDING_TIME)
                        .getValue();

                String timeText;

                int time_s = landTime - depTime;

                /**
                 * Flight time format H:mm
                 */
                if (time_s >= 0) {
                    int hours = time_s / (60 * 60);
                    int minutes = (time_s - hours * (60 * 60)) / 60;

                    timeText = String.valueOf(minutes);
                    if (timeText.length() == 1) {
                        timeText = "0" + timeText;
                    }
                    timeText = hours + ":" + timeText;
                } else {
                    timeText = "<E>";
                }

                Label label = new Label(timeText);
                return label;
            }

            return null;
        }
    }
}
