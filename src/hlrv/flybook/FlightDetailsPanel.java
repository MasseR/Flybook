package hlrv.flybook;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalSplitPanel;

public class FlightDetailsPanel extends CustomComponent implements
        Property.ValueChangeListener {

    private SessionContext ctx;

    private FlightForm flightForm;

    // private TextField fieldId;
    // private TextField fieldDate;
    //
    // private TextField fieldDeparturePort;
    // private TextField fieldDepartureTime;
    //
    // private TextField fieldLandingPort;
    // private TextField fieldLandingTime;
    //
    // private TextField fieldNotes;

    private FlightMap flightMap;

    // private DateFormat dateFormat = new
    // SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public FlightDetailsPanel(SessionContext ctx) throws Exception {

        this.ctx = ctx;

        ctx.getCurrentFlightEntry().addValueChangeListener(this);

        // fieldId = new TextField("Flight ID");
        // fieldId.setReadOnly(true);
        //
        // fieldDate = new TextField("Date Added");
        // fieldDate.setReadOnly(true);
        //
        // HorizontalLayout layout10 = new HorizontalLayout();
        // layout10.setSpacing(true);
        // layout10.addComponent(fieldId);
        // layout10.addComponent(fieldDate);
        //
        // /**
        // * Create departure panel
        // */
        // Panel departurePanel = new Panel("Departure");
        // VerticalLayout layoutDeparture = new VerticalLayout();
        // fieldDeparturePort = new TextField("Port");
        // fieldDepartureTime = new TextField("Time");
        // layoutDeparture.addComponent(fieldDeparturePort);
        // layoutDeparture.addComponent(fieldDepartureTime);
        // layoutDeparture.setSpacing(true);
        // // layoutDeparture.setMargin(true);
        // departurePanel.setContent(layoutDeparture);
        //
        // /**
        // * Create landing panel
        // */
        // Panel landingPanel = new Panel("Landing");
        // VerticalLayout layoutLanding = new VerticalLayout();
        // fieldLandingPort = new TextField("Port");
        // fieldLandingTime = new TextField("Time");
        // layoutLanding.addComponent(fieldLandingPort);
        // layoutLanding.addComponent(fieldLandingTime);
        // layoutLanding.setSpacing(true);
        // // layoutLanding.setMargin(true);
        // landingPanel.setContent(layoutLanding);
        //
        // fieldNotes = new TextField("Notes");
        //
        // VerticalLayout layout0 = new VerticalLayout();
        // // layout0.setSpacing(true);
        // layout0.setMargin(true);
        // layout0.setSizeUndefined();
        // layout0.addComponent(layout10);
        // layout0.addComponent(departurePanel);
        // layout0.addComponent(landingPanel);
        // layout0.addComponent(fieldNotes);
        // // layout0.setExpandRatio(layout10, 0);
        // // layout0.setExpandRatio(departurePanel, 0);
        // // layout0.setExpandRatio(landingPanel, 0);
        // // layout0.setExpandRatio(fieldNotes, 0);
        //
        // Panel panel = new Panel();
        // panel.setCaption("Flight Details");
        // panel.setSizeFull();
        // panel.setContent(layout0);

        flightForm = new FlightForm();
        flightForm.setSizeFull();

        flightMap = new FlightMap(ctx);
        flightMap.setSizeFull();

        VerticalSplitPanel verticalSplitPanel = new VerticalSplitPanel(
                flightForm, flightMap);
        verticalSplitPanel.setSplitPosition(50f);
        verticalSplitPanel.setSizeFull();

        setCompositionRoot(verticalSplitPanel);
    }

    // public String currentDateAsString() {
    // return dateFormat.format(new Date());
    // }

    @Override
    public void valueChange(ValueChangeEvent event) {

        flightForm.setDataSource(ctx.getCurrentFlightEntry().getValue());

        // Item fe = ctx.getCurrentFlightEntry().getValue();
        //
        // fieldId.setReadOnly(false);
        // fieldId.setValue(String.valueOf((Integer) fe.getItemProperty(
        // DBConstants.FLIGHTENTRIES_FLIGHT_ID).getValue()));
        // fieldId.setReadOnly(true);
        //
        // fieldDate.setReadOnly(false);
        // fieldDate.setValue(dateFormat.format(new Date((Integer) fe
        // .getItemProperty(DBConstants.FLIGHTENTRIES_DATE).getValue())));
        // fieldDate.setReadOnly(true);
        //
        // fieldDeparturePort.setValue((String) fe.getItemProperty(
        // "c_departure_airport_string").getValue());
        //
        // fieldDepartureTime.setValue(dateFormat.format(new Date((Integer) fe
        // .getItemProperty(DBConstants.FLIGHTENTRIES_DEPARTURE_TIME)
        // .getValue())));
        //
        // fieldLandingPort.setValue((String) fe.getItemProperty(
        // "c_landing_airport_string").getValue());
        //
        // fieldLandingTime.setValue(dateFormat.format(new Date((Integer) fe
        // .getItemProperty(DBConstants.FLIGHTENTRIES_LANDING_TIME)
        // .getValue())));
        //
        // fieldNotes.setValue((String) fe.getItemProperty(
        // DBConstants.FLIGHTENTRIES_NOTES).getValue());

        // FlightEntry fe = ctx.getCurrentFlightEntry().getValue();

        // fieldId.setValue(String.valueOf(fe.getFlightID()));
        //
        // fieldDate.setValue(dateFormat.format(fe.getDate()));
        //
        // fieldDeparturePort.setValue(fe.getDepartureAirportString());
        //
        // fieldDepartureTime.setValue(dateFormat.format(fe.getDepartureTime()));
        //
        // fieldLandingPort.setValue(fe.getLandingAirportString());
        //
        // fieldLandingTime.setValue(dateFormat.format(fe.getLandingTime()));
        //
        // fieldNotes.setValue(fe.getNotes());
    }
    // public void setItem(Item item) {
    //
    // mapView.setFlightItem(item);
    // }
}
