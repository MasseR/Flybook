package hlrv.flybook;

import hlrv.flybook.db.DBConstants;

import com.vaadin.data.fieldgroup.FieldGroup;
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

public class FlightForm extends CustomComponent {

    private SessionContext context;

    private FieldGroup fieldGroup;

    private TextField fieldId;
    private DateField fieldDate;

    private TextField fieldPilotUsername;
    private TextField fieldPilotFullname;

    private TextField fieldDeparturePort;
    private DateField fieldDepartureTime;
    private ComboBox comboDeparturePortCountries;

    private TextField fieldLandingPort;
    private DateField fieldLandingTime;

    private TextArea fieldNotes;

    public FlightForm(SessionContext context) {
        super();

        this.context = context;

        /**
         * Create id and date
         */
        fieldId = new TextField("Flight ID");
        fieldId.setColumns(5);
        fieldId.setReadOnly(true);

        fieldDate = new DateField("Date Added");
        fieldDate.setResolution(Resolution.SECOND);
        fieldDate.setReadOnly(true);

        HorizontalLayout idAndDateLayout = new HorizontalLayout();
        idAndDateLayout.setSpacing(true);
        idAndDateLayout.addComponent(fieldId);
        idAndDateLayout.addComponent(fieldDate);
        idAndDateLayout.setSizeUndefined();

        /**
         * Create pilot name fields and add to panel
         */
        fieldPilotUsername = new TextField("Username");
        fieldPilotUsername.setColumns(10);
        fieldPilotUsername.setReadOnly(true);
        fieldPilotFullname = new TextField("Fullname");
        fieldPilotFullname.setColumns(20);
        fieldPilotFullname.setReadOnly(true);

        Panel pilotPanel = new Panel("Pilot");
        HorizontalLayout pilotLayout = new HorizontalLayout();
        pilotLayout.addComponent(fieldPilotUsername);
        pilotLayout.addComponent(fieldPilotFullname);
        // pilotLayout.setSizeFull();
        // pilotLayout.setExpandRatio(fieldPilotUsername, 1.0f);
        // pilotLayout.setExpandRatio(fieldPilotFullname, 5.0f);
        pilotLayout.setSpacing(true);
        pilotLayout.setMargin(new MarginInfo(false, true, true, true));
        pilotPanel.setContent(pilotLayout);

        /**
         * Create departure panel
         */
        Panel departurePanel = new Panel("Departure");
        VerticalLayout layoutDeparture = new VerticalLayout();
        HorizontalLayout layoutDeparturePort = new HorizontalLayout();
        fieldDeparturePort = new TextField("Port");
        fieldDeparturePort.setColumns(30);
        comboDeparturePortCountries = new ComboBox("Countries");
        comboDeparturePortCountries.setNewItemsAllowed(false);
        comboDeparturePortCountries.setContainerDataSource(context
                .getAirportsContainer().getCountriesContainer());
        comboDeparturePortCountries.setNullSelectionAllowed(false);
        // comboDeparturePortCountries
        // .setItemCaptionPropertyId(DBConstants.AIRPORTS_COUNTRY);
        layoutDeparturePort.addComponent(fieldDeparturePort);
        layoutDeparturePort.addComponent(comboDeparturePortCountries);
        layoutDeparturePort.setSpacing(true);
        fieldDepartureTime = new DateField("Time");
        fieldDepartureTime.setResolution(Resolution.SECOND);
        layoutDeparture.addComponent(layoutDeparturePort);
        layoutDeparture.addComponent(fieldDepartureTime);
        layoutDeparture.setSpacing(true);
        layoutDeparture.setMargin(new MarginInfo(false, true, true, true));
        departurePanel.setContent(layoutDeparture);

        /**
         * Create landing panel
         */
        Panel landingPanel = new Panel("Landing");
        VerticalLayout layoutLanding = new VerticalLayout();
        fieldLandingPort = new TextField("Port");
        fieldLandingPort.setColumns(30);
        fieldLandingTime = new DateField("Time");
        fieldLandingTime.setResolution(Resolution.SECOND);
        layoutLanding.addComponent(fieldLandingPort);
        layoutLanding.addComponent(fieldLandingTime);
        layoutLanding.setSpacing(true);
        layoutLanding.setMargin(new MarginInfo(false, true, true, true));
        landingPanel.setContent(layoutLanding);

        fieldNotes = new TextArea("Notes");
        fieldNotes.setColumns(40);

        VerticalLayout topLayout = new VerticalLayout();
        // layout0.setSpacing(true);
        topLayout.setMargin(true);
        topLayout.setSizeUndefined();
        topLayout.setWidth("100%");
        topLayout.addComponent(idAndDateLayout);
        topLayout.addComponent(pilotPanel);
        topLayout.addComponent(departurePanel);
        topLayout.addComponent(landingPanel);
        topLayout.addComponent(fieldNotes);
        // layout0.setExpandRatio(layout10, 0);
        // layout0.setExpandRatio(departurePanel, 0);
        // layout0.setExpandRatio(landingPanel, 0);
        // layout0.setExpandRatio(fieldNotes, 0);

        fieldGroup = createFieldGroup();

        setCompositionRoot(topLayout);
    }

    public boolean isEditable() {
        return !fieldGroup.isReadOnly();
    }

    /**
     * Sets whether or not contained fields are editable.
     * 
     * @param editable
     */
    public void setEditable(boolean editable) {

        if (fieldGroup.getItemDataSource() != null) {
            fieldGroup.setReadOnly(!editable);
        }

        fieldId.setReadOnly(true);
        fieldDate.setReadOnly(true);
        fieldPilotUsername.setReadOnly(true);
        fieldPilotFullname.setReadOnly(true);
    }

    /**
     * Sets field data source.
     * 
     * @param flightItem
     */
    public void setDataSource(FlightItem flightItem) {

        if (flightItem != null) {
            fieldGroup.setItemDataSource(flightItem.getItem());
        } else {
            fieldGroup = createFieldGroup();
        }

        fieldId.setReadOnly(true);
        fieldDate.setReadOnly(true);
        fieldPilotUsername.setReadOnly(true);
        fieldPilotFullname.setReadOnly(true);
    }

    /**
     * Commits values to data source.
     */
    public void commit() {

        try {

            fieldGroup.commit();
        } catch (FieldGroup.CommitException e) {

            Notification.show("Commit Error", e.toString(),
                    Notification.TYPE_WARNING_MESSAGE);
        }
    }

    /**
     * Sets original values from time of last call of setDataSource()
     */
    public void reset() {
        fieldGroup.discard();
    }

    private FieldGroup createFieldGroup() {

        FieldGroup fg = new FieldGroup();

        fg.bind(fieldId, DBConstants.FLIGHTENTRIES_FLIGHT_ID);
        fg.bind(fieldDate, DBConstants.FLIGHTENTRIES_DATE);

        fg.bind(fieldPilotUsername, DBConstants.FLIGHTENTRIES_USERNAME);
        fg.bind(fieldPilotFullname, DBConstants.FLIGHTENTRIES_PILOT_FULLNAME);

        fg.bind(fieldDeparturePort,
                DBConstants.FLIGHTENTRIES_DEPARTURE_AIRPORT_STRING);
        fg.bind(fieldDepartureTime, DBConstants.FLIGHTENTRIES_DEPARTURE_TIME);

        fg.bind(fieldLandingPort,
                DBConstants.FLIGHTENTRIES_LANDING_AIRPORT_STRING);
        fg.bind(fieldLandingTime, DBConstants.FLIGHTENTRIES_LANDING_TIME);

        fg.bind(fieldNotes, DBConstants.FLIGHTENTRIES_NOTES);

        return fg;
    }

}
