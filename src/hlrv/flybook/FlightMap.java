package hlrv.flybook;

import hlrv.flybook.db.items.AirportItem;

import com.vaadin.data.Property;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Image;
import com.vaadin.ui.VerticalLayout;

public class FlightMap extends CustomComponent implements
        Property.ValueChangeListener {

    private int imageWidth = 250;
    private int imageHeight = 150;

    /**
     * Component containing the image.
     */
    private Image image;

    /**
     * Combobox used to change map terrain type.
     */
    private ComboBox comboType;

    /**
     * We need departure and landing items.
     */
    private AirportItem departurePort;
    private AirportItem landingPort;

    /**
     * Creates new FlightMap
     * 
     * @param itemProperty
     * @throws Exception
     */
    public FlightMap() {

        departurePort = landingPort = new AirportItem(null);

        /**
         * Combobox to select terrain type for image
         */
        comboType = new ComboBox("Map type");
        comboType.addItem("Terrain");
        comboType.addItem("Satellite");
        comboType.setNullSelectionAllowed(false);
        comboType.setImmediate(true);
        comboType.setValue("Terrain");
        // mapType.setTextInputAllowed(false);
        comboType.addValueChangeListener(this);

        /**
         * Image panel
         */
        image = new Image();
        image.setWidth(imageWidth, Unit.PIXELS);
        image.setHeight(imageHeight, Unit.PIXELS);
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
        topLayout.addComponent(image);
        topLayout.addComponent(comboType);
        // layout0.setExpandRatio(imagePanel, 1);
        // layout0.setExpandRatio(layout10, 0);
        // layout0.setComponentAlignment(imagePanel, Alignment.BOTTOM_LEFT);
        // layout0.setComponentAlignment(layout10, Alignment.BOTTOM_RIGHT);
        topLayout.setSpacing(true);
        // topLayout.setMargin(true);
        topLayout.setSizeUndefined();

        setCompositionRoot(topLayout);
    }

    public void setPorts(AirportItem departurePort, AirportItem landingPort) {

        this.departurePort = departurePort;
        this.landingPort = landingPort;

        updateImageSource();
    }

    // public void setItem(FlightItem item) {
    //
    // this.flightItem = item;
    //
    // if (!flightItem.isNull()) {
    // Property<Integer> prop = flightItem.getItem().getItemProperty(
    // DBConstants.FLIGHTENTRIES_DEPARTURE_AIRPORT);
    // }
    //
    // updateImageSource();
    // }

    private void updateImageSource() {

        if (departurePort.isNull() || landingPort.isNull()) {
            image.setSource(null);
        } else {
            String[] ports = departureAndLandingPortStrings();

            System.out.println("Departure: " + ports[0] + ", Landing: "
                    + ports[1]);

            ExternalResource res = new ExternalResource(
                    createGoogleStaticMapApiURL(ports[0], ports[1]),
                    "image/png");

            image.setSource(res);
        }
    }

    private String[] departureAndLandingPortStrings() {

        String[] ports = new String[2];

        // int departurePort = flightItem.getDepartureAirport();
        // int landingPort = flightItem.getLandingAirport();
        //
        // AirportsContainer airportsContainer = SessionContext.getCurrent()
        // .getAirportsContainer();

        // AirportItem departureItem = airportsContainer.getItem(departurePort);
        // AirportItem landingItem = airportsContainer.getItem(landingPort);

        // if (departureItem.isNull() || landingItem.isNull()) {
        // ports[0] = ports[1] = "";
        // }

        ports[0] = departurePort.getCity() + "," + departurePort.getCountry();
        ports[1] = landingPort.getCity() + "," + landingPort.getCountry();

        // int departurePort = flightItem.getDepartureAirport();
        // int landingPort = flightItem.getLandingAirport();
        //
        // StringBuilder sql = new StringBuilder();
        // sql.append("SELECT ");
        // sql.append(DBConstants.AIRPORTS_ID).append(", ");
        // sql.append(DBConstants.AIRPORTS_CITY).append(", ");
        // sql.append(DBConstants.AIRPORTS_COUNTRY).append(" ");
        // sql.append("FROM ").append(DBConstants.TABLE_AIRPORTS).append(" ");
        // sql.append("WHERE ");
        // sql.append(DBConstants.AIRPORTS_ID).append(" = ");
        // sql.append(departurePort).append(" OR ");
        // sql.append(DBConstants.AIRPORTS_ID).append(" = ");
        // sql.append(landingPort);
        //
        // System.out.println(sql.toString());
        //
        // try {
        //
        // Statement stmt = connection.createStatement();
        //
        // stmt.execute(sql.toString());
        //
        // ResultSet results = stmt.getResultSet();
        //
        // while (results.next()) {
        // int airportId = results.getInt(1);
        // if (airportId == departurePort) {
        // ports[0] = results.getString(2) + ","
        // + results.getString(3);
        // } else {
        // ports[1] = results.getString(2) + ","
        // + results.getString(3);
        // }
        // }
        //
        // results.close(); // !!! release dblock !!!
        //
        // } catch (SQLException e) {
        // System.err.println(e.toString());
        // }

        return ports;
    }

    private String createGoogleStaticMapApiURL(String source, String destination) {

        StringBuilder sb = new StringBuilder(
                "http://maps.googleapis.com/maps/api/staticmap?");

        // sb.append("?center=");

        // sb.append("&size=640x640");
        // sb.append("&size=512x512");
        // sb.append("&size=256x256");
        sb.append("&size=").append(imageWidth).append("x").append(imageHeight);

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
        String type = (String) comboType.getValue();
        return type.toLowerCase();
    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {

        if (event.getProperty() == comboType) {
            updateImageSource();
        } else {

            /**
             * Departure/Landing port has changed.
             */
            updateImageSource();
        }
    }

}
