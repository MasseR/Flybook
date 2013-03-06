package hlrv.flybook;

import hlrv.flybook.db.DBConstants;
import hlrv.flybook.db.containers.AirportsContainer;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.vaadin.data.Property;
import com.vaadin.ui.Table;

public class AirportsTable extends Table {

    /**
     * Which columns are shown in table?
     */
    private String[] visibleColumns = { DBConstants.AIRPORTS_ICAO,
            DBConstants.AIRPORTS_IATA, DBConstants.AIRPORTS_NAME,
            DBConstants.AIRPORTS_CITY, DBConstants.AIRPORTS_COUNTRY,
            DBConstants.AIRPORTS_LATITUDE,
            DBConstants.AIRPORTS_LONGITUDE
    };

    /**
     * Columns headers matching to visible columns.
     */
    private String[] headers = { "ICAO", "IATA", "Airport Name", "City",
            "Country", "Latitude", "Longitude"};

    private NumberFormat numberFormat = new DecimalFormat("#.###");

    /**
     * Creates new AirportsTable.
     */
    public AirportsTable(AirportsContainer airportsContainer) {

        setContainerDataSource(airportsContainer.getContainer());

        setVisibleColumns(visibleColumns);
        setColumnHeaders(headers);
    }

    @Override
    protected String formatPropertyValue(Object rowId, Object colId,
            Property<?> property) {

        if (colId.equals(DBConstants.AIRPORTS_LOCATION)) {

            String[] parts = ((String) property.getValue()).split(":");

            double latitude = Double.valueOf(parts[0]);
            double longitude = Double.valueOf(parts[1]);

            return "Latitude: " + numberFormat.format(latitude)
                    + " Longitude: " + numberFormat.format(longitude);
        }

        return super.formatPropertyValue(rowId, colId, property);
    }
}
