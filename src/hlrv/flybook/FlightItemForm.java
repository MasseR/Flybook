package hlrv.flybook;

import hlrv.flybook.db.DBConstants;
import hlrv.flybook.db.containers.AirportsContainer;
import hlrv.flybook.db.containers.FlightsContainer;
import hlrv.flybook.db.items.FlightItem;

import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class FlightItemForm extends CustomComponent implements
        Property.ValueChangeListener {

    private FieldGroup fieldGroup;

    private TextField fieldId; // is this even needed ?
    private DateField fieldDate;

    // private TextField fieldPilotFullname;
    private TextField fieldPilotUsername;

    private TextField fieldAircraft;

    private AirportField fieldDepartureAirport;
    // private TextField fieldDeparturePort;
    private DateField fieldDepartureTime;
    // private ComboBox comboDeparturePortCountries;

    private AirportField fieldLandingAirport;
    // private TextField fieldLandingPort;
    private DateField fieldLandingTime;

    private TextField fieldFlightTime;

    private TextField fieldOnBlockTime;
    private TextField fieldOffBlockTime;
    private TextField fieldIFRTime;

    private ComboBox comboFlightType;

    private TextArea fieldNotes;

    private FlightMap flightMap;

    public FlightItemForm() {
        super();

        /**
         * Create id and date
         */
        fieldId = new TextField("Flight ID");
        fieldId.setColumns(5);

        fieldDate = new DateField("Date Added");
        fieldDate.setResolution(Resolution.SECOND);

        /**
         * Create pilot name fields and add to panel
         */
        // fieldPilotFullname = new TextField("Pilot");
        // fieldPilotFullname.setColumns(20);
        fieldPilotUsername = new TextField("Username");
        fieldPilotUsername.setColumns(10);

        fieldAircraft = new TextField("Aircraft");

        /**
         * Create departure panel
         */
        Panel departurePanel = new Panel("Departure");
        VerticalLayout layoutDeparture = new VerticalLayout();

        // fieldDeparturePort = new TextField("Port");
        // fieldDeparturePort.setColumns(30);
        fieldDepartureAirport = new AirportField();
        fieldDepartureAirport.setValidationVisible(true);
        fieldDepartureAirport.addValidator(new NullValidator(
                "Departure Airport must be selected", false));
        fieldDepartureAirport.addValueChangeListener(this);
        fieldDepartureAirport.setSizeFull();

        // HorizontalLayout layoutDeparturePort = new HorizontalLayout();

        // fieldDeparturePort = new TextField("Port");
        // fieldDeparturePort.setColumns(30);
        // layoutDeparturePort.addComponent(fieldDeparturePort);
        // layoutDeparturePort.addComponent(comboDeparturePortCountries);
        // layoutDeparturePort.setSpacing(true);

        fieldDepartureTime = new DateField("Time");
        fieldDepartureTime.setResolution(Resolution.MINUTE);
        fieldDepartureTime.addValueChangeListener(this);
        fieldDepartureTime.setImmediate(true);

        // layoutDeparture.addComponent(layoutDeparturePort);
        layoutDeparture.addComponent(fieldDepartureAirport);
        // layoutDeparture.addComponent(fieldDeparturePort);
        layoutDeparture.addComponent(fieldDepartureTime);
        layoutDeparture.setSpacing(true);
        layoutDeparture.setMargin(new MarginInfo(false, true, true, true));
        departurePanel.setContent(layoutDeparture);

        /**
         * Create landing panel
         */
        Panel landingPanel = new Panel("Landing");
        VerticalLayout layoutLanding = new VerticalLayout();

        // fieldLandingPort = new TextField("Port");
        // fieldLandingPort.setColumns(30);

        fieldLandingAirport = new AirportField();
        fieldLandingAirport.setValidationVisible(true);
        fieldLandingAirport.addValidator(new NullValidator(
                "Landing Airport must be selected", false));
        fieldLandingAirport.addValueChangeListener(this);
        fieldLandingAirport.setSizeFull();

        fieldLandingTime = new DateField("Time");
        fieldLandingTime.setResolution(Resolution.MINUTE);
        fieldLandingTime.addValueChangeListener(this);
        fieldLandingTime.setImmediate(true);

        layoutLanding.addComponent(fieldLandingAirport);
        // layoutLanding.addComponent(fieldLandingPort);
        layoutLanding.addComponent(fieldLandingTime);
        layoutLanding.setSpacing(true);
        layoutLanding.setMargin(new MarginInfo(false, true, true, true));
        landingPanel.setContent(layoutLanding);

        fieldFlightTime = new TextField("Flight Time (HH:MM:SS)");
        // fieldFlightTime.setResolution(Resolution.SECOND);
        // /**
        // * Need to have timezone +-0, because the date object used is a
        // * difference of two dates.
        // */
        // fieldFlightTime.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        // fieldFlightTime.setDateFormat("HH:mm:ss");

        fieldOnBlockTime = new TextField("On-Block Time");
        fieldOffBlockTime = new TextField("Off-Block Time");
        fieldIFRTime = new TextField("IFR Time");

        comboFlightType = new ComboBox("Flight Type", SessionContext
                .getCurrent().getFlightsContainer().getFlightTypesContainer());
        comboFlightType
                .setItemCaptionPropertyId(FlightsContainer.PID_FLIGHT_TYPE);
        comboFlightType.setNullSelectionAllowed(false);
        comboFlightType.setInputPrompt("Select Flight Type");
        comboFlightType.setImmediate(true);

        fieldNotes = new TextArea("Notes");
        // fieldNotes.setColumns(30);

        flightMap = new FlightMap();

        /**
         * Layout created components.
         */
        HorizontalLayout idAndDateLayout = new HorizontalLayout();
        idAndDateLayout.setSpacing(true);
        idAndDateLayout.setSizeUndefined();
        idAndDateLayout.addComponent(fieldPilotUsername);
        // idAndDateLayout.addComponent(fieldPilotFullname);
        idAndDateLayout.addComponent(fieldDate);
        idAndDateLayout.addComponent(fieldId);

        // Panel pilotPanel = new Panel("Pilot");
        // HorizontalLayout pilotLayout = new HorizontalLayout();
        // pilotLayout.addComponent(fieldPilotFullname);
        // pilotLayout.addComponent(fieldPilotUsername);
        // // pilotLayout.setSizeFull();
        // // pilotLayout.setExpandRatio(fieldPilotUsername, 1.0f);
        // // pilotLayout.setExpandRatio(fieldPilotFullname, 5.0f);
        // pilotLayout.setSpacing(true);
        // pilotLayout.setSizeUndefined();
        // pilotLayout.setMargin(new MarginInfo(false, true, true, true));
        // pilotPanel.setContent(pilotLayout);

        HorizontalLayout typeAndAircraftLayout = new HorizontalLayout();
        typeAndAircraftLayout.setSpacing(true);
        typeAndAircraftLayout.setSizeUndefined();
        typeAndAircraftLayout.addComponent(comboFlightType);
        typeAndAircraftLayout.addComponent(fieldAircraft);

        HorizontalLayout airportSelectorLayout = new HorizontalLayout();
        airportSelectorLayout.setSpacing(true);
        airportSelectorLayout.setSizeUndefined();
        airportSelectorLayout.addComponent(departurePanel);
        airportSelectorLayout.addComponent(landingPanel);

        VerticalLayout airportLayout = new VerticalLayout();
        airportLayout.setSpacing(true);
        airportLayout.setSizeUndefined();
        airportLayout.addComponent(airportSelectorLayout);
        // airportLayout.addComponent(fieldFlightTime);

        // HorizontalLayout blockTimeLayout = new HorizontalLayout();
        // blockTimeLayout.setSpacing(true);
        // blockTimeLayout.setSizeUndefined();
        // blockTimeLayout.addComponent(fieldOnBlockTime);
        // blockTimeLayout.addComponent(fieldOffBlockTime);

        VerticalLayout bottomLeftLayout = new VerticalLayout();
        bottomLeftLayout.setSpacing(true);
        bottomLeftLayout.setSizeUndefined();

        bottomLeftLayout.addComponent(fieldOnBlockTime);
        bottomLeftLayout.addComponent(fieldOffBlockTime);
        bottomLeftLayout.addComponent(fieldIFRTime);
        bottomLeftLayout.addComponent(fieldNotes);

        VerticalLayout bottomRightLayout = new VerticalLayout();
        bottomRightLayout.setSpacing(true);
        bottomRightLayout.setSizeUndefined();
        bottomRightLayout.addComponent(fieldFlightTime);
        bottomRightLayout.addComponent(flightMap);

        HorizontalLayout bottomLayout = new HorizontalLayout();
        bottomLayout.setSpacing(true);
        bottomLayout.setSizeUndefined();
        bottomLayout.addComponent(bottomLeftLayout);
        bottomLayout.addComponent(bottomRightLayout);
        // bottomLayout.setComponentAlignment(flightMap, Alignment.TOP_LEFT);

        VerticalLayout topLayout = new VerticalLayout();
        topLayout.setSpacing(true);
        topLayout.setMargin(true);
        topLayout.setSizeUndefined();
        // topLayout.setSizeUndefined();
        // topLayout.setWidth("100%");
        topLayout.addComponent(idAndDateLayout);
        topLayout.addComponent(typeAndAircraftLayout);
        topLayout.addComponent(airportSelectorLayout);
        // topLayout.addComponent(fieldFlightTime);
        // topLayout.addComponent(blockTimeLayout);
        // topLayout.addComponent(fieldIFRTime);
        // topLayout.addComponent(comboFlightType);
        topLayout.addComponent(bottomLayout);
        // topLayout.addComponent(fieldNotes);
        // layout0.setExpandRatio(layout10, 0);
        // layout0.setExpandRatio(departurePanel, 0);
        // layout0.setExpandRatio(landingPanel, 0);
        // layout0.setExpandRatio(fieldNotes, 0);

        /**
         * Creates new FieldGroup and pre-binds.
         */
        fieldGroup = createFieldGroup();
        fieldGroup.setItemDataSource(FlightItem.createNullItem().getItem());
        setReadOnlyComponents();

        setCompositionRoot(topLayout);
    }

    // private IndexedContainer createFlightTypeContainer(String caption) {
    //
    // IndexedContainer flightTypeContainer = new IndexedContainer();
    // flightTypeContainer.addContainerProperty(caption, String.class, null);
    //
    // flightTypeContainer.addItem(new Integer(FlightType.UNDEFINED.ordinal()))
    // .getItemProperty(caption).setValue("Unknown");
    //
    // flightTypeContainer.addItem(new Integer(FlightType.DOMESTIC.ordinal()))
    // .getItemProperty(caption).setValue("Domestic");
    //
    // flightTypeContainer.addItem(new Integer(FlightType.HOBBY.ordinal()))
    // .getItemProperty(caption).setValue("Hobby");
    //
    // flightTypeContainer
    // .addItem(new Integer(FlightType.TRANSREGIONAL.ordinal()))
    // .getItemProperty(caption).setValue("Transregional");
    //
    // flightTypeContainer
    // .addItem(new Integer(FlightType.TRANSCONTINENTAL.ordinal()))
    // .getItemProperty(caption).setValue("Transcontinental");
    //
    // return flightTypeContainer;
    // }

    public boolean isEditable() {
        return !fieldGroup.isReadOnly();
    }

    /**
     * Sets whether or not contained fields are editable.
     * 
     * @param editable
     */
    public void setEditable(boolean editable) {

        // if (fieldGroup.getItemDataSource() != null) {
        fieldGroup.setReadOnly(!editable);
        // } else {
        // setReadOnly(!editable);
        // }

        setReadOnlyComponents();
    }

    /**
     * Sets item to edit.
     * 
     * @param flightItem
     */
    public void setItem(FlightItem flightItem) {

        if (flightItem.isNull()) {
            flightItem = FlightItem.createNullItem();
        }

        /**
         * If id is null, there is no why id field should be visible at all.
         */
        if (flightItem.getFlightID() == null) {
            fieldId.setVisible(false);
        } else {
            fieldId.setReadOnly(false);
            fieldId.setValue(flightItem.getFlightID().toString());
            fieldId.setVisible(true);
        }

        /**
         * We set time fields to null to make sure valueChange() works right
         * when fieldGroup sets values from source and change events fire again.
         */
        fieldDepartureTime.setReadOnly(false);
        fieldDepartureTime.setValue(null);

        fieldLandingTime.setReadOnly(false);
        fieldLandingTime.setValue(null);

        fieldGroup.setItemDataSource(flightItem.getItem());

        // if (flightItem.isNull()) {
        //
        // // for (Field<?> f : fieldGroup.getFields()) {
        // // fieldGroup.unbind(f);
        // // }
        // fieldGroup.setReadOnly(true);
        // fieldGroup.discard();
        // fieldGroup.setItemDataSource(null);
        //
        // } else {
        //
        // /**
        // * We set time fields to null to make sure valueChange() works right
        // * when fieldGroup sets values from source and change events fire
        // * again.
        // */
        // fieldDepartureTime.setReadOnly(false);
        // fieldDepartureTime.setValue(null);
        //
        // fieldLandingTime.setReadOnly(false);
        // fieldLandingTime.setValue(null);
        //
        // fieldGroup.setItemDataSource(flightItem.getItem());
        // }

        /**
         * Must reset read-only status, because fieldGroup removes them on call
         * to setItemDataSource().
         */
        setReadOnlyComponents();
    }

    /**
     * Commits values to data source.
     * 
     * NOTE: This commits changes to datasource (FlightItem) only. Because Item
     * belongs to non-autocommit SQLContainer, one must call commit for the
     * container as well so that changes are updated to the database.
     */
    public boolean commit() {

        try {

            fieldGroup.commit();

            return true;
        } catch (FieldGroup.CommitException e) {
            Notification.show("Commit Warning", e.toString(),
                    Notification.TYPE_HUMANIZED_MESSAGE);
        }
        return false;
    }

    /**
     * Sets original values from time of last call of setDataSource()
     */
    public void reset() {
        fieldGroup.discard();
    }

    /**
     * Creates field group.
     * 
     * Note: fields are not bound. Bindings are done automatically after first
     * call to setItemDataSource().
     * 
     * @return FieldGroup
     */
    private FieldGroup createFieldGroup() {

        FieldGroup fg = new FieldGroup();

        // fg.bind(fieldId, DBConstants.FLIGHTENTRIES_FLIGHT_ID);

        fg.bind(fieldDate, DBConstants.FLIGHTENTRIES_DATE);

        fg.bind(fieldPilotUsername, DBConstants.FLIGHTENTRIES_USERNAME);
        // fg.bind(fieldPilotFullname,
        // DBConstants.FLIGHTENTRIES_PILOT_FULLNAME);

        fg.bind(comboFlightType, DBConstants.FLIGHTENTRIES_FLIGHT_TYPE);
        fg.bind(fieldAircraft, DBConstants.FLIGHTENTRIES_AIRCRAFT);

        fg.bind(fieldDepartureAirport,
                DBConstants.FLIGHTENTRIES_DEPARTURE_AIRPORT);
        fg.bind(fieldDepartureTime, DBConstants.FLIGHTENTRIES_DEPARTURE_TIME);

        fg.bind(fieldLandingAirport, DBConstants.FLIGHTENTRIES_LANDING_AIRPORT);
        fg.bind(fieldLandingTime, DBConstants.FLIGHTENTRIES_LANDING_TIME);

        // fg.bind(fieldFlightTime, DBConstants.FLIGHTENTRIES_FLIGHT_TIME);

        fg.bind(fieldOnBlockTime, DBConstants.FLIGHTENTRIES_ONBLOCK_TIME);
        fg.bind(fieldOffBlockTime, DBConstants.FLIGHTENTRIES_OFFBLOCK_TIME);

        fg.bind(fieldIFRTime, DBConstants.FLIGHTENTRIES_IFR_TIME);

        fg.bind(fieldNotes, DBConstants.FLIGHTENTRIES_NOTES);

        return fg;
    }

    private void setReadOnlyComponents() {

        fieldId.setReadOnly(true);
        fieldDate.setReadOnly(true);
        fieldPilotUsername.setReadOnly(true);
        // fieldPilotFullname.setReadOnly(true);
        fieldFlightTime.setReadOnly(true);
    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {

        if (event.getProperty() == fieldDepartureTime
                || event.getProperty() == fieldLandingTime) {

            Date departureDate = fieldDepartureTime.getValue();
            Date landingDate = fieldLandingTime.getValue();

            /**
             * Ignore valuechange if either of dates has not been set.
             */
            if (departureDate == null || landingDate == null) {
                return;
            }

            /**
             * Check if current departure/landing times make sense. If not, then
             * set the changed date equal to the other.
             */
            if (departureDate.after(landingDate)) {
                if (event.getProperty() == fieldDepartureTime) {
                    fieldDepartureTime.setValue(landingDate);
                } else {
                    fieldLandingTime.setValue(departureDate);
                }

                return;
            }

            /**
             * Get flight time in milliseconds.
             */
            long time_ms = landingDate.getTime() - departureDate.getTime();

            // if (time_ms >= 24 * 60 * 60 * 1000) {
            // fieldFlightTime.setDateFormat("d 'days' HH:mm:ss");
            // } else {
            // fieldFlightTime.setDateFormat("HH:mm:ss");
            // }

            /**
             * In following we need to have timezone +-0, because the date
             * object used is a difference of two dates.
             */
            DateFormat formatter = DateFormat
                    .getTimeInstance(DateFormat.MEDIUM);
            formatter.setTimeZone(TimeZone.getTimeZone("GMT+0"));
            String timeString = formatter.format(new Date(time_ms));

            long days = time_ms / (24 * 60 * 60 * 1000);
            if (days > 0) {
                timeString = days + " days " + timeString;
            }

            // DateFormat.getTimeInstance(DateFormat.FULL);
            // String timeString = formatter.format(new Date(time_ms));
            // formatter = DateFormat.getTimeInstance(DateFormat.LONG);
            // timeString = formatter.format(new Date(time_ms));

            // formatter = DateFormat.getTimeInstance(DateFormat.SHORT);
            // timeString = formatter.format(new Date(time_ms));

            /**
             * We want the flight time in form HH:MM:SS
             */
            // Calendar cal = Calendar.getInstance();
            // cal.setTimeInMillis(time_ms);
            //
            // String hours = cal.getDisplayName(Calendar.HOUR_OF_DAY,
            // Calendar.LONG, Locale.getDefault());
            // String mins = cal.getDisplayName(Calendar.MINUTE, Calendar.LONG,
            // Locale.getDefault());
            // String secs = "00";

            // long time_s = time_ms / 1000L;
            // long time_min = time_s / 60L;
            // long time_h = time_min / 60L;

            fieldFlightTime.setReadOnly(false);
            fieldFlightTime.setValue(timeString);
            fieldFlightTime.setReadOnly(true);
        } else if (event.getProperty() == fieldDepartureAirport
                || event.getProperty() == fieldLandingAirport) {

            Integer departurePort = fieldDepartureAirport.getValue();
            Integer landingPort = fieldLandingAirport.getValue();

            AirportsContainer container = SessionContext.getCurrent()
                    .getAirportsContainer();

            flightMap.setPorts(container.getItem(departurePort),
                    container.getItem(landingPort));

        }
    }

}
