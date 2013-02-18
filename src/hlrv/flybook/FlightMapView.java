package hlrv.flybook;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Image;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class FlightMapView extends VerticalLayout implements Runnable,
        Property.ValueChangeListener {

    private FlightTable table;
    private Image image;

    private Connection connection;

    private ComboBox mapType;

    public FlightMapView(FlightTable table) {

        setSpacing(true);
        setMargin(true);

        this.table = table;
        this.image = new Image();
        Panel imagePanel = new Panel(image);

        // table.setImmediate(true);

        this.mapType = new ComboBox("Map type");

        addComponent(mapType);
        addComponent(imagePanel);

        image.setAlternateText("No flight entries selected");
        // image.setImmediate(true);

        mapType.addItem("Terrain");
        mapType.addItem("Satellite");
        mapType.setNullSelectionAllowed(false);
        mapType.setImmediate(true);

        mapType.setValue("Terrain");
        // mapType.setTextInputAllowed(false);

        mapType.addValueChangeListener(this);

        table.addValueChangeListener(this);

    }

    private String createGoogleStaticMapApiURL(String source, String destination) {

        StringBuilder sb = new StringBuilder(
                "http://maps.googleapis.com/maps/api/staticmap?");

        // sb.append("?center=");

        sb.append("&size=640x640");

        // sb.append("&markers=color:green%7Clabel:D%7CHelsinki,Finland");
        // sb.append("&markers=color:blue%7Clabel:L%7CLondon,England");
        //
        // sb.append("&path=weight:5%7Ccolor:red%7Cgeodesic:true"
        // + "%7CHelsinki,Finland" + "%7CBerlin,Germany" + "%7CRome,Italy"
        // + "%7CLondon,England");

        sb.append("&markers=color:green%7Clabel:D%7C").append(source);
        sb.append("&markers=color:blue%7Clabel:L%7C").append(destination);

        sb.append("&path=weight:5%7Ccolor:red%7Cgeodesic:true%7C");
        sb.append(source).append("%7C").append(destination);

        sb.append("&maptype=").append(getMapType());

        sb.append("&sensor=false");

        return sb.toString();
    }

    private String getMapType() {
        String type = (String) mapType.getValue();
        return type.toLowerCase();
    }

    @Override
    public void attach() {
        super.attach();

        try {
            DBConnection dbconn = (DBConnection) getSession()
                    .getAttribute("db");
            connection = dbconn.reserve();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            System.err.println(e.toString());
            System.exit(1);
        }
    }

    @Override
    // Event fired on table row change
    public void valueChange(ValueChangeEvent event) {

        if (event.getProperty() == table) {
            // Object rowid = (Object) event.getProperty().getValue();

            // new Thread(this).start();

        } else if (event.getProperty() == mapType) {
            // new Thread(this).start();
        }

        String[] ports = departureAndLandingPortStrings();

        System.out.println("Departure: " + ports[0] + ", Landing: " + ports[1]);

        ExternalResource res = new ExternalResource(
                createGoogleStaticMapApiURL(ports[0], ports[1]), "image/png");

        // Image newImage = new Image(null, res);
        // newImage.setAlternateText("No flight entries selected");
        // newImage.setImmediate(true);
        // replaceComponent(image, newImage);
        // this.image = newImage;

        image.markAsDirty();

        image.setSource(res);

    }

    private String[] departureAndLandingPortStrings() {

        String[] ports = new String[2];

        Object rowid = table.getValue(); // selected row

        int departurePort = (Integer) table.getContainerProperty(rowid,
                DBConstants.COLUMN_FLIGHTENTRIES_DEPARTURE_AIRPORT).getValue();

        int landingPort = (Integer) table.getContainerProperty(rowid,
                DBConstants.COLUMN_FLIGHTENTRIES_LANDING_AIRPORT).getValue();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(DBConstants.COLUMN_AIRPORTS_ID).append(", ");
        sql.append(DBConstants.COLUMN_AIRPORTS_CITY).append(", ");
        sql.append(DBConstants.COLUMN_AIRPORTS_COUNTRY).append(" ");
        sql.append("FROM ").append(DBConstants.TABLE_AIRPORTS).append(" ");
        sql.append("WHERE ");
        sql.append(DBConstants.COLUMN_AIRPORTS_ID).append(" = ");
        sql.append(departurePort).append(" OR ");
        sql.append(DBConstants.COLUMN_AIRPORTS_ID).append(" = ");
        sql.append(landingPort);

        System.out.println(sql.toString());

        try {

            Statement stmt = connection.createStatement();

            stmt.execute(sql.toString());

            ResultSet results = stmt.getResultSet();

            while (results.next()) {
                int airportId = results.getInt(1);
                if (airportId == departurePort) {
                    ports[0] = results.getString(2) + ","
                            + results.getString(3);
                } else {
                    ports[1] = results.getString(2) + ","
                            + results.getString(3);
                }
            }

            results.close(); // !!! release dblock !!!

        } catch (SQLException e) {
            System.err.println(e.toString());
        }

        return ports;
    }

    @Override
    public void run() {

        // Image won't update immediately if setSource(res) called from
        // background thread
        // Some discussion of the problem here:
        // https://vaadin.com/forum/-/message_boards/view_message/231271

    }
}
