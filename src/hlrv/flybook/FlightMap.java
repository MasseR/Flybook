package hlrv.flybook;

import hlrv.flybook.db.DBConstants;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Image;
import com.vaadin.ui.VerticalLayout;

public class FlightMap extends CustomComponent implements Runnable,
        Property.ValueChangeListener {

    private int imageSize = 256;

    private Connection connection;

    private Image image;

    private ComboBox mapType;

    public FlightMap() throws Exception {

        SessionContext.getCurrent().getCurrentFlightEntry()
                .addValueChangeListener(this);

        connection = SessionContext.getCurrent().getDBConnection().reserve();
        connection.setAutoCommit(true);

        /**
         * Combobox to select terrain type for image
         */
        mapType = new ComboBox("Map type");
        mapType.addItem("Terrain");
        mapType.addItem("Satellite");
        mapType.setNullSelectionAllowed(false);
        mapType.setImmediate(true);

        mapType.setValue("Terrain");
        // mapType.setTextInputAllowed(false);

        mapType.addValueChangeListener(this);

        /**
         * Image panel
         */
        image = new Image();
        image.setWidth(imageSize, Unit.PIXELS);
        image.setHeight(imageSize, Unit.PIXELS);
        image.setAlternateText("No flight entries selected");

        // image.setImmediate(true);
        // image.setSizeUndefined();

        // /**
        // * Wrap image in Panel
        // */
        // Panel imagePanel = new Panel();
        // imagePanel.setContent(image);
        // imagePanel.setWidth(imageSize, Unit.PIXELS);
        // imagePanel.setHeight(imageSize, Unit.PIXELS);
        // imagePanel.setSizeFull();

        // VerticalLayout layout10 = new VerticalLayout();
        // layout10.addComponent(mapType);
        // // layout10.setComponentAlignment(mapType, Alignment.TOP_RIGHT);
        // // layout10.setSpacing(true);
        // layout10.setWidth("50px");
        // // layout10.setSizeFull();

        VerticalLayout topLayout = new VerticalLayout();
        // layout0.addComponent(imagePanel);
        topLayout.addComponent(mapType);
        topLayout.addComponent(image);
        // layout0.setExpandRatio(imagePanel, 1);
        // layout0.setExpandRatio(layout10, 0);
        // layout0.setComponentAlignment(imagePanel, Alignment.BOTTOM_LEFT);
        // layout0.setComponentAlignment(layout10, Alignment.BOTTOM_RIGHT);
        topLayout.setSpacing(true);
        topLayout.setMargin(true);
        topLayout.setSizeUndefined();

        setCompositionRoot(topLayout);
    }

    // public void setFlightItem(Item item) {
    //
    // flightItem = item;
    //
    // updateImageSource();
    // }

    private void updateImageSource() {

        ExternalResource res = null;

        FlightItem flightItem = SessionContext.getCurrent()
                .getCurrentFlightEntry().getValue();

        if (flightItem != null) {

            String[] ports = departureAndLandingPortStrings(flightItem);

            System.out.println("Departure: " + ports[0] + ", Landing: "
                    + ports[1]);

            res = new ExternalResource(createGoogleStaticMapApiURL(ports[0],
                    ports[1]), "image/png");
        }

        // Image newImage = new Image(null, res);
        // newImage.setAlternateText("No flight entries selected");
        // newImage.setImmediate(true);
        // replaceComponent(image, newImage);
        // this.image = newImage;

        image.markAsDirty();

        image.setSource(res);
    }

    private String[] departureAndLandingPortStrings(FlightItem flightItem) {

        String[] ports = new String[2];

        int departurePort = flightItem.getDepartureAirport();
        int landingPort = flightItem.getLandingAirport();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(DBConstants.AIRPORTS_ID).append(", ");
        sql.append(DBConstants.AIRPORTS_CITY).append(", ");
        sql.append(DBConstants.AIRPORTS_COUNTRY).append(" ");
        sql.append("FROM ").append(DBConstants.TABLE_AIRPORTS).append(" ");
        sql.append("WHERE ");
        sql.append(DBConstants.AIRPORTS_ID).append(" = ");
        sql.append(departurePort).append(" OR ");
        sql.append(DBConstants.AIRPORTS_ID).append(" = ");
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

    private String createGoogleStaticMapApiURL(String source, String destination) {

        StringBuilder sb = new StringBuilder(
                "http://maps.googleapis.com/maps/api/staticmap?");

        // sb.append("?center=");

        // sb.append("&size=640x640");
        // sb.append("&size=512x512");
        // sb.append("&size=256x256");
        sb.append("&size=").append(imageSize).append("x").append(imageSize);

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
    public void valueChange(ValueChangeEvent event) {

        updateImageSource();
    }

    @Override
    public void run() {

        // Image won't update immediately if setSource(res) called from
        // background thread
        // Some discussion of the problem here:
        // https://vaadin.com/forum/-/message_boards/view_message/231271

    }
}
